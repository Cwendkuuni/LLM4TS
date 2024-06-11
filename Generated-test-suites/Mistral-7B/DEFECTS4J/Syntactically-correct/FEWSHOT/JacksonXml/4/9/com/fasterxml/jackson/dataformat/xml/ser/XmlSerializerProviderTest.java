package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.NodeProcessor;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.databind.ser.impl.ValueSerializerBase;
import com.fasterxml.jackson.databind.ser.impl.ValueSerializerBase. NullSerializer;
import com.fasterxml.jackson.databind.util.RootNameLookup;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class XmlSerializerProviderTest {

    private static final String ROOT_NAME = "root";
    private static final String NAMESPACE = "http://example.com/";
    private XmlSerializerProvider xmlProvider;
    private XmlRootNameLookup rootNameLookup;
    private SerializationConfig config;
    private SerializerFactory serializerFactory;
    private TokenBuffer tokenBuffer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        config = mock(SerializationConfig.class);
        serializerFactory = mock(SerializerFactory.class);
        rootNameLookup = spy(new XmlRootNameLookup() {
            @Override
            public QName findRootName(JavaType rootType, SerializationConfig config) {
                return new QName(NAMESPACE, ROOT_NAME);
            }

            @Override
            public QName findRootName(Class<?> cls, SerializationConfig config) {
                return new QName(NAMESPACE, ROOT_NAME);
            }

            @Override
            public QName findRootName(String rootName, SerializationConfig config) {
                return new QName(NAMESPACE, rootName);
            }
        });
        xmlProvider = new XmlSerializerProvider(rootNameLookup, config, serializerFactory);
        tokenBuffer = new TokenBuffer();
    }

    @Test
    public void testCreateInstance() {
        SerializerProvider result = xmlProvider.createInstance(config, serializerFactory);
        assert result instanceof XmlSerializerProvider;
    }

    @Test
    public void testSerializeValueWithNullRootName() throws IOException {
        JsonSerializer<Object> nullSerializer = xmlProvider.findValueSerializer(Object.class, null);
        jsonSerializer.serialize(null, new JsonGenerator(tokenBuffer), xmlProvider);
        verify(tokenBuffer).writeNull();
    }

    @Test
    public void testSerializeValue() throws IOException {
        Object value = new Object();
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object.class, null);
        jsonSerializer.serialize(value, new JsonGenerator(tokenBuffer), xmlProvider);

        ArgumentCaptor<ToXmlGenerator> xgenCaptor = ArgumentCaptor.forClass(ToXmlGenerator.class);
        verify(tokenBuffer).setRootElementWriter(xgenCaptor.capture());

        ToXmlGenerator xgen = xgenCaptor.getValue();
        QName rootName = xmlProvider._rootNameFromConfig();
        xmlProvider._initWithRootName(xgen, rootName);
        xmlProvider._serializeXmlNull(xgen);

        xmlProvider.serializeValue(xgen, value);
        verify(xgen).serializeContentAsXml(value);
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        Object value = new Object();
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object.class, null);

        QName rootName = new QName(NAMESPACE, ROOT_NAME);
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.getRootElementWriter()).thenReturn(mock(ToXmlGenerator.class));
        when(gen.setRootElementName(any(QName.class))).thenReturn(gen);

        xmlProvider.serializeValue(gen, value, rootName);
        verify(gen).setRootElementName(rootName);
        verify(gen.getRootElementWriter()).then().serializeContentAsXml(value);
    }

    @Test
    public void testSerializeValueWithJavaType() throws IOException {
        Object value = new Object();
        JsonSerializer<Object> serializer = xmlProvider.findValueSerializer(Object.class, null);
        JavaType rootType = new SimpleType(Object.class);

        xmlProvider.serializeValue(new JsonGenerator(tokenBuffer), value, rootType, serializer);
        verify(tokenBuffer).setRootElementWriter(any(ToXmlGenerator.class));
        verify(tokenBuffer.getRootElementWriter()).writeStartObject();
        verify(tokenBuffer).writeFieldName("item");
        xmlProvider.serializeValue(tokenBuffer.getRootElementWriter(), value);
        verify(tokenBuffer.getRootElementWriter()).writeEndObject();
    }

    @Test
    public void testSerializeValueWithJavaTypeAndSerializer() throws IOException {
        Object value = new Object();
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        JavaType rootType = new SimpleType(Object.class);

        xmlProvider.serializeValue(new JsonGenerator(tokenBuffer), value, rootType, serializer);
        verify(tokenBuffer).setRootElementWriter(any(ToXmlGenerator.class));
        verify(tokenBuffer.getRootElementWriter()).writeStartObject();
        verify(tokenBuffer.getRootElementWriter()).writeFieldName("item");
        serializer.serialize(value, tokenBuffer.getRootElementWriter(), xmlProvider);
        verify(tokenBuffer.getRootElementWriter()).writeEndObject();
    }

    @Test
    public void testFindTypedValueSerializerWithNullRootType() {
        JsonSerializer<Object> serializer = xmlProvider.findTypedValueSerializer(null, true, null);
        assert serializer instanceof NullSerializer;
    }

    @Test
    public void testFindTypedValueSerializer() {
        ValueSerializerBase<Object> serializer = mock(ValueSerializerBase.class);
        when(serializer.getSerializerProvider()).thenReturn(xmlProvider);
        when(serializer.handleNullValue()).thenReturn(null);

        JsonSerializer<Object> result = xmlProvider.findTypedValueSerializer(Object.class, true, null);
        assert result == serializer;
    }

    @Test
    public void testFindTypedValueSerializerWithFilterProvider() {
        ValueSerializerBase<Object> serializer = mock(ValueSerializerBase.class);
        when(serializer.getSerializerProvider()).thenReturn(xmlProvider);
        when(serializer.handleNullValue()).thenReturn(null);

        SerializerProvider filterProvider = new DefaultSerializerProvider();
        SimpleFilterProvider filter = new SimpleFilterProvider();
        filter.setFilterInstance(SimpleBeanPropertyFilter.filterOutAllExcept("foo"));
        filterProvider.setFilterProvider(filter);

        JsonSerializer<Object> result = xmlProvider.findTypedValueSerializer(Object.class, true, filterProvider);
        assert result == serializer;
    }

    @Test(expected = JsonMappingException.class)
    public void testFindTypedValueSerializerWithInvalidGenerator() throws IOException {
        Object value = new Object();
        JsonSerializer<Object> serializer = xmlProvider.findTypedValueSerializer(Object.class, true, tokenBuffer);
        serializer.serialize(value, tokenBuffer, xmlProvider);
    }
}
package com.fasterxml.jackson.dataformat.xml.ser; // make sure the test class is in the same package as the tested class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializers;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.dataformat.xml.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.impl.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XmlSerializerProviderTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final SerializationConfig CONFIG = MAPPER.getSerializationConfig();
    private static final SerializerProvider DEFAULT_SERIALIZER_PROVIDER = MAPPER.getDefaultSerializerProvider();
    private static final SerializerFactory DEFAULT_SERIALIZER_FACTORY = MAPPER.getDefaultSerializerFactory();
    private static final XmlRootNameLookup ROOT_NAME_LOOKUP = new XmlRootNameLookup();

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        xmlSerializerProvider = new XmlSerializerProvider(ROOT_NAME_LOOKUP);
    }

    @Test
    public void testCreateInstance() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig(MAPPER.getFactory());
        SerializerProvider provider = xmlSerializerProvider.createInstance(config, DEFAULT_SERIALIZER_FACTORY);
        assertEquals(XmlSerializerProvider.class, provider.getClass());
    }

    @Test
    public void testCreateInstanceWithSrc() throws JsonMappingException {
        DefaultSerializerProvider src = mock(DefaultSerializerProvider.class);
        SerializationConfig config = new SerializationConfig(MAPPER.getFactory());
        SerializerFactory factory = DEFAULT_SERIALIZER_FACTORY;
        XmlSerializerProvider provider = xmlSerializerProvider.createInstance(src, config, factory);
        assertEquals(XmlSerializerProvider.class, provider.getClass());
        assertEquals(src._rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testSerializeValueWithNull() throws JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("item")).thenReturn(gen);
        when(gen.writeNull()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        xmlSerializerProvider._serializeXmlNull(gen);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("item");
        verify(gen, times(1)).writeNull();
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithObject() throws JsonMappingException, IOException {
        String expectedXml = "<rootElement><propertyName>value</propertyName></rootElement>";

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("item")).thenReturn(gen);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("propertyName")).thenReturn(gen);
        when(gen.writeString("value")).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        Object value = new Object() {{
            @SuppressWarnings("unused")
            String propertyName = "propertyName";
            String value = "value";
        }};
        Class<?> cls = value.getClass();
        JsonSerializer<Object> serializer = new SimpleBeanPropertySerializerBase() {};

        xmlSerializerProvider.serializeValue(gen, value, new JavaType(cls), serializer);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("item");
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("propertyName");
        verify(gen, times(1)).writeString("value");
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(1)).writeEndObject();

        String actualXml = StaxUtil.convertToText(gen);
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueWithArray() throws JsonMappingException, IOException {
        String expectedXml = "<rootElement><item><item1></item><item><item2></item2></item></item></rootElement>";

        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("item")).thenReturn(gen);
        when(gen.writeStartArray()).thenReturn(gen);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeFieldName("item1")).thenReturn(gen);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);
        when(gen.writeEndArray()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        Object[] values = new Object[] { new Object() {}, new Object() {} };
        Class<?> elementType = values[0].getClass();
        JavaType rootType = TypeUtil.constructType(XmlSerializerProvider.class, new TypeSerializer[] { ClassUtil.simpleSerializableType(elementType) });
        JsonSerializer<Object> serializer = new SimpleBeanPropertySerializerBase() {};

        xmlSerializerProvider.serializeValue(gen, values, rootType, serializer);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("item");
        verify(gen, times(1)).writeStartArray();
        verify(gen, times(2)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(1)).writeEndArray();
        verify(gen, times(1)).writeEndObject();

        String actualXml = StaxUtil.convertToText(gen);
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeValueWithNullRootType() throws JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        when(gen.writeStartObject()).thenReturn(gen);
        when(gen.writeNull()).thenReturn(gen);
        when(gen.writeEndObject()).thenReturn(gen);

        xmlSerializerProvider.serializeValue(gen, null, null, null);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeNull();
        verify(gen, times(1)).writeEndObject();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithIOException() throws JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        doThrow(IOException.class).when(gen).serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class));

        xmlSerializerProvider.serializeValue(gen, new Object() {}, new JavaType(Object.class), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithJsonMappingException() throws JsonMappingException, IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        doThrow(JsonMappingException.class).when(gen).serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class));

        xmlSerializerProvider.serializeValue(gen, new Object() {}, new JavaType(Object.class), null);
    }

    @Test
    public void testInitWithRootName() throws IOException {
        QName rootName = new QName("http://example.com/", "rootElement");
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        when(xgen.writeStartObject()).thenReturn(xgen);
        when(xgen.setNextName(any(QName.class))).thenReturn(xgen);
        when(xgen.initGenerator()).thenReturn(xgen);

        xmlSerializerProvider._initWithRootName(xgen, rootName);

        verify(xgen, times(1)).writeStartObject();
        verify(xgen, times(1)).setNextName(rootName);
        verify(xgen, times(1)).initGenerator();
    }

    @Test
    public void testInitWithRootNameWhenRootIsSet() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(false);
        when(xgen.writeStartObject()).thenReturn(xgen);
        when(xgen.initGenerator()).thenReturn(xgen);

        xmlSerializerProvider._initWithRootName(xgen, null);

        verifyNoMoreInteractions(xgen);
    }

    @Test
    public void testSetDefaultNamespace() throws IOException, XMLStreamException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        when(xgen.getStaxWriter()).thenReturn(mock(javax.xml.stream.XMLStreamWriter.class));

        xmlSerializerProvider._initWithRootName(xgen, new QName("http://example.com/", "rootElement"));

        xmlSerializerProvider.setDefaultNamespace("http://example.com/");

        verify(xgen.getStaxWriter()).setDefaultNamespace("http://example.com/");
    }
}
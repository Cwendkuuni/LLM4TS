
package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

public class XmlSerializerProviderTest {

    private ToXmlGenerator gen;
    private XmlRootNameLookup rootNameLookup; 

    @Before
    public void setup() {
        gen = mock(ToXmlGenerator.class);
        rootNameLookup = mock(XmlRootNameLookup.class);
    }

    @Test
    public void testConstructor() {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        assertEquals(rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testConstructorWithConfigAndFactory() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        XmlSerializerProvider src = mock(XmlSerializerProvider.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(src, config, factory);
        assertEquals(rootNameLookup, provider._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        XmlSerializerProvider src = mock(XmlSerializerProvider.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(src, config, factory);
        DefaultSerializerProvider instance = provider.createInstance(config, factory);
        assertEquals(rootNameLookup, instance._serializerProvider._rootNameLookup);
    }

    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(gen, null);
        verify(gen).writeFieldName("null");
    }

    @Test
    public void testSerializeValueWithNonNullValue() throws IOException {
        Object value = new Object();
        Class<?> cls = value.getClass();

        when(rootNameLookup.findRootName(cls, provider._config)).thenReturn(new QName("root"));

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(gen, value);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("root");
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueWithArrayValue() throws IOException {
        Object[] value = new Object[2];
        Class<?> cls = value.getClass();

        when(rootNameLookup.findRootName(cls, provider._config)).thenReturn(new QName("root"));

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(gen, value);
        verify(gen, times(2)).writeStartObject();
        verify(gen, times(2)).writeFieldName("item");
        verify(gen, times(2)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithNonArrayType() throws IOException {
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        when(rootType.getRawClass()).thenReturn(rawClass);

        when(rootNameLookup.findRootName(rootType, provider._config)).thenReturn(new QName("root"));

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(gen, value, rootType);
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("root");
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueWithArrayRootType() throws IOException {
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        when(rootType.getRawClass()).thenReturn(rawClass);

        when(rootNameLookup.findRootName(rootType, provider._config)).thenReturn(new QName("root"));

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(gen, value, rootType);
        verify(gen, times(2)).writeStartObject();
        verify(gen, times(2)).writeFieldName("item");
        verify(gen, times(2)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithSerializer() throws IOException {
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        when(rootType.getRawClass()).thenReturn(rawClass);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);

        when(rootNameLookup.findRootName(rootType, provider._config)).thenReturn(new QName("root"));

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider.serializeValue(gen, value, rootType, serializer);
        verify(serializer).serialize(value, gen, provider);
    }

    @Test
    public void testInitWithRootName() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        QName rootName = new QName("root");
        provider._initWithRootName(gen, rootName);
        verify(gen).setNextNameIfMissing(rootName);
        verify(gen).initGenerator();

        String ns = rootName.getNamespaceURI();
        if (ns != null && ns.length() > 0) {
            verify(gen.getStaxWriter()).setDefaultNamespace(ns);
        }
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = mock(PropertyName.class);
        when(provider._config.getFullRootName()).thenReturn(name);

        QName rootName = provider._rootNameFromConfig();

        assertNull(rootName.getNamespace());
        assertEquals(name.getSimpleName(), rootName.getLocalPart());
    }

    @Test
    public void testAsXmlGeneratorWhenGeneratorIsXmlGenerator() throws JsonMappingException {
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        ToXmlGenerator result = provider._asXmlGenerator(gen);

        assertEquals(gen, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWhenGeneratorIsNotXmlGeneratorOrTokenBuffer() throws JsonMappingException {
        JsonGenerator invalidGen = mock(JsonGenerator.class);

        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        provider._asXmlGenerator(invalidGen);
    }
}


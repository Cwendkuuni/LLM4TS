package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    private XmlRootNameLookup rootNameLookup;
    private XmlSerializerProvider serializerProvider;

    @Before
    public void setUp() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        serializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider copy = serializerProvider.copy();
        assertNotNull(copy);
        assertEquals(serializerProvider.getClass(), copy.getClass());
        assertSame(serializerProvider.getConfig(), copy.getConfig());
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory jsf = mock(SerializerFactory.class);
        XmlSerializerProvider instance = serializerProvider.createInstance(config, jsf);
        assertNotNull(instance);
        assertEquals(config, instance.getConfig());
        assertEquals(jsf, instance.getFactory());
    }

    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        serializerProvider.serializeValue(gen, value);
        // Verify that the expected methods are called
        verify(gen, times(1)).writeFieldName(anyString());
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootType() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        serializerProvider.serializeValue(gen, value, rootType, ser);
        // Verify that the expected methods are called
        verify(gen, times(1)).writeFieldName(anyString());
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator jgen = mock(JsonGenerator.class);
        serializerProvider.serializeXmlNull(jgen);
        // Verify that the expected methods are called
        verify(jgen, times(1)).writeFieldName(anyString());
        verify(jgen, times(1)).writeStartObject();
        verify(jgen, times(1)).writeEndObject();
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");
        serializerProvider.startRootArray(xgen, rootName);
        // Verify that the expected methods are called
        verify(xgen, times(1)).writeStartObject();
        verify(xgen, times(1)).writeFieldName(anyString());
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");
        serializerProvider.initWithRootName(xgen, rootName);
        // Verify that the expected methods are called
        verify(xgen, times(1)).setNextName(any(QName.class));
        verify(xgen, times(1)).initGenerator();
        verify(xgen, times(1)).getStaxWriter();
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName("root");
        serializationConfig = mock(SerializationConfig.class);
        when(serializerProvider.getConfig()).thenReturn(serializationConfig);
        when(serializationConfig.getFullRootName()).thenReturn(name);
        QName rootName = serializerProvider.rootNameFromConfig();
        assertNotNull(rootName);
        assertEquals("root", rootName.getLocalPart());
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        ToXmlGenerator xgen = serializerProvider.asXmlGenerator(gen);
        assertNull(xgen);
        when(gen instanceof ToXmlGenerator).thenReturn(true);
        xgen = serializerProvider.asXmlGenerator(gen);
        assertNotNull(xgen);
    }

    @Test
    public void testWrapAsIOE() {
        JsonGenerator g = mock(JsonGenerator.class);
        Exception e = new Exception();
        IOException ioe = serializerProvider.wrapAsIOE(g, e);
        assertNotNull(ioe);
        assertEquals(e, ioe.getCause());
    }
}
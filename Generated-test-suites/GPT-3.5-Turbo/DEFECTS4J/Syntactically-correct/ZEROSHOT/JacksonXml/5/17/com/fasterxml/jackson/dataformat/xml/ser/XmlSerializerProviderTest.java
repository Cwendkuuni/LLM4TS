package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {
    
    @Test
    public void testCopy() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        
        XmlSerializerProvider result = src.copy();
        
        assertNotNull(result);
        assertNotSame(src, result);
    }
    
    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        
        XmlSerializerProvider result = src.createInstance(config, factory);
        
        assertNotNull(result);
        assertNotSame(src, result);
    }
    
    @Test
    public void testSerializeValue_Null() throws IOException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        
        provider.serializeValue(gen, null);
        
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("item");
        verify(gen, times(1)).writeEndObject();
    }
    
    @Test
    public void testSerializeValue_AsArray() throws IOException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new ArrayList<>();
        
        provider.serializeValue(gen, value);
        
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("item");
        verify(gen, times(1)).writeEndObject();
    }
    
    @Test
    public void testSerializeValue_WithSerializer() throws IOException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);
        
        provider.serializeValue(gen, value, serializer);
        
        verify(serializer, times(1)).serialize(value, gen, provider);
    }
    
    @Test
    public void test_startRootArray() throws IOException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("item");
        
        provider._startRootArray(xgen, rootName);
        
        verify(xgen, times(1)).writeStartObject();
        verify(xgen, times(1)).writeFieldName("item");
    }
    
    @Test
    public void test_initWithRootName() throws IOException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");
        
        provider._initWithRootName(xgen, rootName);
        
        verify(xgen, times(1)).setNextNameIfMissing(rootName);
    }
    
    @Test
    public void test_rootNameFromConfig() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        PropertyName name = mock(PropertyName.class);
        
        when(name.getNamespace()).thenReturn("ns");
        when(name.getSimpleName()).thenReturn("root");
        
        provider._config = mock(SerializationConfig.class);
        when(provider._config.getFullRootName()).thenReturn(name);
        
        QName result = provider._rootNameFromConfig();
        
        assertNotNull(result);
        assertEquals("ns", result.getNamespaceURI());
        assertEquals("root", result.getLocalPart());
    }
    
    @Test
    public void test_asXmlGenerator() throws JsonMappingException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = mock(JsonGenerator.class);
        
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> {
            provider._asXmlGenerator(gen);
        });
        
        assertEquals("XmlMapper does not with generators of type other than ToXmlGenerator; got: " + gen.getClass().getName(), exception.getMessage());
    }
    
    @Test
    public void test_wrapAsIOE_RuntimeException() throws IOException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator g = mock(JsonGenerator.class);
        RuntimeException e = mock(RuntimeException.class);
        
        IOException result = provider._wrapAsIOE(g, e);
        
        assertNotNull(result);
        assertSame(e, result.getCause());
    }
    
    @Test
    public void test_wrapAsIOE_Exception() throws IOException {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator g = mock(JsonGenerator.class);
        Exception e = mock(Exception.class);
        
        IOException result = provider._wrapAsIOE(g, e);
        
        assertNotNull(result);
        assertSame(e, result.getCause());
    }
}
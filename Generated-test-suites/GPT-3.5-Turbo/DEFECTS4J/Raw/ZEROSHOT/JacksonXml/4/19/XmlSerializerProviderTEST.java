package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import javax.xml.stream.XMLStreamException;
import javax.xml.namespace.QName;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlSerializerProviderTest {

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider src = new XmlSerializerProvider(new XmlRootNameLookup());
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory f = mock(SerializerFactory.class);
        XmlSerializerProvider provider = src.createInstance(config, f);
        assertNotNull(provider);
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = null;
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value);
        
        verify(gen).writeStartObject();
        verify(gen).writeFieldName("item");
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueWithNotNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = "Test";
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value);
        
        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeFieldName("item");
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootType() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = "Test";
        JavaType rootType = mock(JavaType.class);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value, rootType);
        
        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeFieldName("item");
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = "Test";
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value, rootType, ser);
        
        verify(gen, never()).writeStartObject();
        verify(gen, never()).writeFieldName("item");
        verify(gen, never()).writeEndObject();
    }

    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        JsonGenerator jgen = mock(JsonGenerator.class);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._serializeXmlNull(jgen);
        
        verify(jgen).writeStartObject();
        verify(jgen).writeFieldName("item");
        verify(jgen).writeEndObject();
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._startRootArray(xgen, rootName);
        
        verify(xgen).writeStartObject();
        verify(xgen).writeFieldName("item");
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);

        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._initWithRootName(xgen, rootName);

        verify(xgen, never()).setNextNameIfMissing(rootName);
        verify(xgen, never()).setNextName(rootName);
        verify(xgen).initGenerator();
        
        String ns = rootName.getNamespaceURI();
        if (ns != null && ns.length() > 0) {
            try {
                verify(xgen.getStaxWriter()).setDefaultNamespace(ns);
            } catch (XMLStreamException e) {
                // Ignore
            }
        }
    }

    @Test
    public void testRootNameFromConfig() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        PropertyName name = null;
        QName result = provider._rootNameFromConfig();
        assertEquals(null, result);
        
        name = mock(PropertyName.class);
        result = provider._rootNameFromConfig();
        assertEquals(null, result);
        
        when(name.getNamespace()).thenReturn("http://www.example.com");
        when(name.getSimpleName()).thenReturn("test");
        result = provider._rootNameFromConfig();
        assertEquals(new QName("http://www.example.com", "test"), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithWrongType() throws JsonMappingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._asXmlGenerator(gen);
    }

    @Test
    public void testAsXmlGeneratorWithXmlGeneratorType() throws JsonMappingException {
        ToXmlGenerator gen = mock(ToXmlGenerator.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator result = provider._asXmlGenerator(gen);
        assertNotNull(result);
    }

    @Test
    public void testAsXmlGeneratorWithTokenBufferType() throws JsonMappingException {
        TokenBuffer gen = mock(TokenBuffer.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator result = provider._asXmlGenerator(gen);
        assertNotNull(result);
    }
}
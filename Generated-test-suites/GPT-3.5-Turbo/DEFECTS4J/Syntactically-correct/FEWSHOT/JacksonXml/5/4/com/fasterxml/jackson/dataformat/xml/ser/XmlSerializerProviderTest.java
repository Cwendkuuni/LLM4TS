package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider provider;

    @Before
    public void setup() {
        XmlRootNameLookup lookup = mock(XmlRootNameLookup.class);
        provider = new XmlSerializerProvider(lookup);
    }

    @Test
    public void testCopy() {
        DefaultSerializerProvider copy = provider.copy();
        assertEquals(copy.getClass(), XmlSerializerProvider.class);
    }

    @Test
    public void testCreateInstance() {
        SerializerFactory factory = mock(SerializerFactory.class);
        SerializationConfig config = mock(SerializationConfig.class);
        DefaultSerializerProvider instance = provider.createInstance(config, factory);
        assertNotNull(instance);
    }

    @Test
    public void testSerializeValue_WithObject() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValue_WithNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        provider.serializeValue(gen, null);
    }

    @Test
    public void testSerializeValue_WithObjectAndRootType() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void test_startRootArray() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("test");
        provider._startRootArray(xgen, rootName);
    }

    @Test
    public void test_initWithRootName() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = new QName("test");
        provider._initWithRootName(xgen, rootName);
    }

    @Test
    public void test_rootNameFromConfig() {
        PropertyName name = new PropertyName("test");
        provider._config = mock(SerializationConfig.class);
        provider._config.setFullRootName(name);
        QName rootName = provider._rootNameFromConfig();
        assertEquals(rootName, new QName("test"));
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGenerator_WithInvalidGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        provider._asXmlGenerator(gen);
    }

    @Test
    public void test_asXmlGenerator_WithValidGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(ToXmlGenerator.class);
        ToXmlGenerator xgen = provider._asXmlGenerator(gen);
        assertNotNull(xgen);
    }

    @Test
    public void test_wrapAsIOE_WithIOException() {
        IOException e = new IOException();
        JsonGenerator g = mock(JsonGenerator.class);
        IOException ioe = provider._wrapAsIOE(g, e);
        assertEquals(e, ioe);
    }

    @Test
    public void test_wrapAsIOE_WithException() {
        Exception e = new Exception();
        JsonGenerator g = mock(JsonGenerator.class);
        IOException ioe = provider._wrapAsIOE(g, e);
        assertNotNull(ioe);
        assertEquals(e.getMessage(), ioe.getMessage());
    }

    @Test
    public void test_serializeXmlNull_WithRootNameFromConfig() throws IOException {
        JsonGenerator jgen = mock(JsonGenerator.class);
        PropertyName name = new PropertyName("test");
        provider._config = mock(SerializationConfig.class);
        provider._config.setFullRootName(name);
        provider._serializeXmlNull(jgen);
    }

    @Test
    public void test_serializeXmlNull_WithoutRootNameFromConfig() throws IOException {
        JsonGenerator jgen = mock(JsonGenerator.class);
        provider._serializeXmlNull(jgen);
    }

}
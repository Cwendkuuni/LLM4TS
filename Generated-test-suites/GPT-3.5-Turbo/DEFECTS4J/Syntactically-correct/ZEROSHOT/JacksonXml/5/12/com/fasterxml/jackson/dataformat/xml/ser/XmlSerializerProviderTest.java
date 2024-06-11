package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class XmlSerializerProviderTest {
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() {
        xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
    }

    @After
    public void tearDown() {
        xmlSerializerProvider = null;
    }

    @Test
    public void testCopy() {
        DefaultSerializerProvider provider = xmlSerializerProvider.copy();
        assertEquals(provider.getClass(), XmlSerializerProvider.class);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new SerializerFactory();
        DefaultSerializerProvider provider = xmlSerializerProvider.createInstance(config, factory);
        assertEquals(provider.getClass(), XmlSerializerProvider.class);
    }

    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        xmlSerializerProvider.serializeValue(gen, value);
        //perform assertion
    }

    @Test
    public void testSerializeValueWithType() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        JsonSerializer<Object> ser = new JsonSerializer<>();
        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);
        //perform assertion
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator jgen = new JsonGenerator();
        xmlSerializerProvider._serializeXmlNull(jgen);
        //perform assertion
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName();
        xmlSerializerProvider._startRootArray(xgen, rootName);
        //perform assertion
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName();
        xmlSerializerProvider._initWithRootName(xgen, rootName);
        //perform assertion
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName();
        QName rootName = xmlSerializerProvider._rootNameFromConfig(name);
        //perform assertion
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = new JsonGenerator();
        ToXmlGenerator xmlGen = xmlSerializerProvider._asXmlGenerator(gen);
        //perform assertion
    }

    @Test
    public void testWrapAsIOE() {
        JsonGenerator g = new JsonGenerator();
        Exception e = new Exception();
        IOException ioe = xmlSerializerProvider._wrapAsIOE(g, e);
        //perform assertion
    }
}
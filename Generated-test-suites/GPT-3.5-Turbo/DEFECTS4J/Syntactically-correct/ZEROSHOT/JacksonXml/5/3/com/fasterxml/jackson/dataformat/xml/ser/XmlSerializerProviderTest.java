package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import javax.xml.stream.XMLStreamException;
import javax.xml.namespace.QName;
import java.io.IOException;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() {
      XmlRootNameLookup rootNames = new XmlRootNameLookup();
      xmlSerializerProvider = new XmlSerializerProvider(rootNames);
    }

    @After
    public void tearDown() {
      xmlSerializerProvider = null;
    }

    @Test
    public void testCopy() throws IOException {
        XmlSerializerProvider result = xmlSerializerProvider.copy();
        assertNotNull(result);
    }

    @Test
    public void testCreateInstance() throws IOException {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory jsf = new SerializerFactory();
        XmlSerializerProvider result = xmlSerializerProvider.createInstance(config, jsf);
        assertNotNull(result);
    }

    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        xmlSerializerProvider.serializeValue(gen, value);
        // assert something
    }

    @Test
    public void testSerializeValueWithType() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        JsonSerializer<Object> ser = new JsonSerializer<Object>();
        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);
        // assert something
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator jgen = new JsonGenerator();
        xmlSerializerProvider._serializeXmlNull(jgen);
        // assert something
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        xmlSerializerProvider._startRootArray(xgen, rootName);
        // assert something
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        xmlSerializerProvider._initWithRootName(xgen, rootName);
        // assert something
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName();
        QName result = xmlSerializerProvider._rootNameFromConfig();
        assertNotNull(result);
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = new JsonGenerator();
        ToXmlGenerator result = xmlSerializerProvider._asXmlGenerator(gen);
        assertNull(result);
    }

    @Test
    public void testWrapAsIOE() throws IOException {
        JsonGenerator g = new JsonGenerator();
        Exception e = new Exception();
        IOException result = xmlSerializerProvider._wrapAsIOE(g, e);
        assertNotNull(result);
    }
}
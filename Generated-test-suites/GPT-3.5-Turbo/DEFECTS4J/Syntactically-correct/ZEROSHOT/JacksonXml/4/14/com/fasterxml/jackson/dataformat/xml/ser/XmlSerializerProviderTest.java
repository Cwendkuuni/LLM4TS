package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testCreateInstance() {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        DefaultSerializerProvider result = xmlSerializerProvider.createInstance(null, null);
        // asserts
        org.junit.Assert.assertNotNull(result);
    }

    @Test
    public void testSerializeValue() throws IOException {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        jsonGenerator jgen = null;
        Object value = null;
        xmlSerializerProvider.serializeValue(jgen, value);
        // asserts
        org.junit.Assert.assertTrue(jgen == null);
    }

    @Test
    public void testSerializeValue_1() throws IOException {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        jsonGenerator jgen = null;
        Object value = null;
        JavaType rootType = null;
        xmlSerializerProvider.serializeValue(jgen, value, rootType);
        // asserts
        org.junit.Assert.assertTrue(jgen == null);
    }

    @Test
    public void testSerializeValue_2() throws IOException {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        jsonGenerator jgen = null;
        Object value = null;
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;
        xmlSerializerProvider.serializeValue(jgen, value, rootType, ser);
        // asserts
        org.junit.Assert.assertTrue(jgen == null);
    }

    @Test
    public void test_serializeXmlNull() throws IOException {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        jsonGenerator jgen = null;
        xmlSerializerProvider._serializeXmlNull(jgen);
        // asserts
        org.junit.Assert.assertTrue(jgen == null);
    }

    @Test
    public void test_startRootArray() throws IOException {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        ToXmlGenerator xgen = null;
        QName rootName = null;
        xmlSerializerProvider._startRootArray(xgen, rootName);
        // asserts
        org.junit.Assert.assertTrue(xgen == null);
    }

    @Test
    public void test_initWithRootName() throws IOException {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        ToXmlGenerator xgen = null;
        QName rootName = null;
        xmlSerializerProvider._initWithRootName(xgen, rootName);
        // asserts
        org.junit.Assert.assertTrue(xgen == null);
    }

    @Test
    public void test_rootNameFromConfig() {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        QJsonName qJsonName = new PropertyName(null);
        xmlSerializerProvider._rootNameFromConfig(qJsonName);
        // asserts
    }

    @Test
    public void test_asXmlGenerator() throws JsonMappingException {
        // test implementation
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
        JsonGenerator gen = null;
        xmlSerializerProvider._asXmlGenerator(gen);
        // asserts
        org.junit.Assert.assertTrue(gen == null);
    }
}
package com.fasterxml.jackson.dataformat.xml.ser;

import java.io.IOException;

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider provider;

    @Before
    public void setup() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        provider = new XmlSerializerProvider(rootNames);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = null;
        SerializerFactory jsf = null;
        DefaultSerializerProvider result = provider.createInstance(config, jsf);
        assertNotNull(result);
    }

    @Test
    public void testSerializeValue_Null() throws IOException {
        JsonGenerator gen = null;
        Object value = null;
        provider.serializeValue(gen, value);
        //TODO: validate the result
    }

    @Test
    public void testSerializeValue_Object() throws IOException {
        JsonGenerator gen = null;
        Object value = new Object();
        provider.serializeValue(gen, value);
        //TODO: validate the result
    }

    @Test
    public void testSerializeValue_JavaType_Null() throws IOException {
        JsonGenerator gen = null;
        Object value = null;
        JavaType rootType = null;
        provider.serializeValue(gen, value, rootType);
        //TODO: validate the result
    }

    @Test
    public void testSerializeValue_JavaType_Object() throws IOException {
        JsonGenerator gen = null;
        Object value = new Object();
        JavaType rootType = null;
        provider.serializeValue(gen, value, rootType);
        //TODO: validate the result
    }

    @Test
    public void testSerializeValue_JavaType_Serializer_Null() throws IOException {
        JsonGenerator gen = null;
        Object value = null;
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;
        provider.serializeValue(gen, value, rootType, ser);
        //TODO: validate the result
    }

    @Test
    public void testSerializeValue_JavaType_Serializer_Object() throws IOException {
        JsonGenerator gen = null;
        Object value = new Object();
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;
        provider.serializeValue(gen, value, rootType, ser);
        //TODO: validate the result
    }

    @Test
    public void test_serializeXmlNull() throws IOException {
        JsonGenerator jgen = null;
        provider._serializeXmlNull(jgen);
        //TODO: validate the result
    }

    @Test
    public void test_startRootArray() throws IOException {
        ToXmlGenerator xgen = null;
        QName rootName = null;
        provider._startRootArray(xgen, rootName);
        //TODO: validate the result
    }

    @Test
    public void test_initWithRootName() throws IOException {
        ToXmlGenerator xgen = null;
        QName rootName = null;
        provider._initWithRootName(xgen, rootName);
        //TODO: validate the result
    }

    @Test
    public void test_rootNameFromConfig() {
        PropertyName name = null;
        QName result = provider._rootNameFromConfig(name);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGenerator_InvalidType() throws JsonMappingException {
        JsonGenerator gen = JsonFactory.createGenerator();
        provider._asXmlGenerator(gen);
    }

    @Test
    public void test_asXmlGenerator_ValidType() throws JsonMappingException {
        JsonGenerator gen = new ToXmlGenerator();
        ToXmlGenerator result = provider._asXmlGenerator(gen);
        assertNotNull(result);
    }
}
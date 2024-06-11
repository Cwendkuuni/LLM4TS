package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        SerializerFactory serializerFactory = new SerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        XmlSerializerProvider instance = provider.createInstance();
        assertEquals(instance.getRootNameLookup(), rootNames);
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorThrowsException() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._asXmlGenerator(new JsonGenerator());
    }

    @Test
    public void testSerializeValue() {
        JsonGenerator gen = new ToXmlGenerator();
        Object value = new Object();
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value);
    }

    @Test
    public void testSerializeValueWithRootType() {
        JsonGenerator gen = new ToXmlGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value, rootType);
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() {
        JsonGenerator gen = new ToXmlGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        JsonSerializer<Object> ser = new JsonSerializer<>();
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(gen, value, rootType, ser);
    }

    @Test
    public void testSerializeXmlNull() {
        JsonGenerator gen = new ToXmlGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._serializeXmlNull(gen);
    }

    @Test
    public void testStartRootArray() {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("test");
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._startRootArray(xgen, rootName);
    }

    @Test
    public void testInitWithRootName() {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("test");
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._initWithRootName(xgen, rootName);
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName propertyName = new PropertyName("test");
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        config.setFullRootName(propertyName);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        QName rootName = provider._rootNameFromConfig();
        assertEquals(rootName, new QName("test"));
    }

}
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    @Test
    public void testConstructor1() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        assertNotNull(provider);
    }

    @Test
    public void testConstructor2() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        SerializationConfig config = new SerializationConfig();
        SerializerFactory f = new SerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(null, config, f);
        assertNotNull(provider);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory jsf = new SerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(null, config, jsf);
        XmlSerializerProvider instance = provider.createInstance(config, jsf);
        assertNotNull(instance);
    }

    @Test
    public void testSerializeValue() {
        //TODO: Implement test method
    }

    @Test
    public void testSerializeValue2() {
        //TODO: Implement test method
    }

    @Test
    public void testSerializeValue3() {
        //TODO: Implement test method
    }

    @Test
    public void test_serializeXmlNull() {
        //TODO: Implement test method
    }

    @Test
    public void test_startRootArray() {
        //TODO: Implement test method
    }

    @Test
    public void test_initWithRootName() {
        //TODO: Implement test method
    }

    @Test
    public void test_rootNameFromConfig() {
        //TODO: Implement test method
    }

    @Test
    public void test_asXmlGenerator() {
        //TODO: Implement test method
    }
}
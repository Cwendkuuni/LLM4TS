package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Assert;
import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testCopy() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        
        XmlSerializerProvider copy = provider.copy();
        Assert.assertNotNull(copy);
        Assert.assertNotSame(provider, copy);
    }
    
    @Test
    public void testCreateInstance() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        SerializerFactory factory = new SerializerFactory();
        SerializationConfig config = new SerializationConfig();
        
        XmlSerializerProvider instance = provider.createInstance(config, factory);
        Assert.assertNotNull(instance);
        Assert.assertNotSame(provider, instance);
    }
    
    @Test
    public void testSerializeValue() throws IOException {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        
        provider.serializeValue(gen, value);
        // TODO: Validate the serialized value
        
        gen.close();
    }
    
    @Test
    public void testSerializeValueWithRootType() throws IOException {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        JsonSerializer<Object> ser = new JsonSerializer<>();
        
        provider.serializeValue(gen, value, rootType, ser);
        // TODO: Validate the serialized value
        
        gen.close();
    }
    
    @Test
    public void testSerializeXmlNull() throws IOException {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator gen = new JsonGenerator();
        
        provider._serializeXmlNull(gen);
        // TODO: Validate the serialized null value
        
        gen.close();
    }
    
    @Test
    public void testStartRootArray() throws IOException {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        
        provider._startRootArray(xgen, rootName);
        // TODO: Validate the start of the root array
        
        xgen.close();
    }

    @Test
    public void testInitWithRootName() throws IOException {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        
        provider._initWithRootName(xgen, rootName);
        // TODO: Validate the initialization with root name
        
        xgen.close();
    }
    
    @Test
    public void testRootNameFromConfig() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        
        QName rootName = provider._rootNameFromConfig();
        // TODO: Validate the root name from config
    }
    
    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator gen = new JsonGenerator();
        
        ToXmlGenerator xgen = provider._asXmlGenerator(gen);
        Assert.assertNull(xgen);
        
        gen.close();
    }    
    
    @Test
    public void testWrapAsIOE() {
        XmlRootNameLookup lookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        JsonGenerator gen = new JsonGenerator();
        Exception e = new Exception("Test Exception");
        
        IOException ioe = provider._wrapAsIOE(gen, e);
        Assert.assertNotNull(ioe);
        Assert.assertEquals(e.getMessage(), ioe.getMessage());
        
        gen.close();
    }
}
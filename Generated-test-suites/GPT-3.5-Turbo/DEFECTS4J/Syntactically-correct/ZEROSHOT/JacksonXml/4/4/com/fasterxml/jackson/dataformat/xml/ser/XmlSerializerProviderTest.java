package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Assert;
import org.junit.Test;

public class XmlSerializerProviderTest {
    
    @Test
    public void testXmlSerializerProvider() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        
        Assert.assertEquals(rootNames, provider._rootNameLookup);
    }
    
    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory jsf = new SerializerFactory();
        XmlSerializerProvider src = new XmlSerializerProvider();
        
        XmlSerializerProvider provider = src.createInstance(config, jsf);
        
        Assert.assertEquals(src._rootNameLookup, provider._rootNameLookup);
        Assert.assertEquals(config, provider.getConfig());
        Assert.assertEquals(jsf, provider.getFactory());
    }
    
    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        provider.serializeValue(gen, value);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testSerializeValueWithRootType() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        provider.serializeValue(gen, value, rootType);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testSerializeValueWithSerializer() throws IOException {
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        JsonSerializer<Object> ser = new JsonSerializer<>();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        provider.serializeValue(gen, value, rootType, ser);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator jgen = new JsonGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        provider._serializeXmlNull(jgen);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        provider._startRootArray(xgen, rootName);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        provider._initWithRootName(xgen, rootName);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName();
        SerializationConfig config = new SerializationConfig();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        config.setFullRootName(name);
        
        QName result = provider._rootNameFromConfig();
        
        // TODO: Add assertions
    }
    
    @Test
    public void testAsXmlGenerator() {
        JsonGenerator gen = new JsonGenerator();
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        ToXmlGenerator result = provider._asXmlGenerator(gen);
        
        // TODO: Add assertions
    }

}
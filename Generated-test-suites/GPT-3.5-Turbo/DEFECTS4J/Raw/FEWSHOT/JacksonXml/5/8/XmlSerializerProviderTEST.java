package com.fasterxml.jackson.dataformat.xml.ser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
    @Test
    public void testCopy() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNameLookup);
        DefaultSerializerProvider copy = src.copy();
        
        assertEquals(rootNameLookup, copy._rootNameLookup);
    }
    
    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNameLookup);
        SerializerFactory serializerFactory = new SerializerFactory();
        SerializationConfig serializationConfig = new SerializationConfig();
        
        DefaultSerializerProvider instance = src.createInstance(serializationConfig, serializerFactory);
        
        assertEquals(rootNameLookup, instance._rootNameLookup);
    }
    
    @Test
    public void testSerializeValue() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator jsonGenerator = new ToXmlGenerator();
        Object value = new Object();
        
        serializerProvider.serializeValue(jsonGenerator, value);
        
        // TODO: Assert the behavior
    }
    
    @Test
    public void testSerializeValueWithType() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator jsonGenerator = new ToXmlGenerator();
        Object value = new Object();
        JavaType rootType = JavaType.fromClass(Object.class);
        JsonSerializer<Object> serializer = new JsonSerializer<>();
        
        serializerProvider.serializeValue(jsonGenerator, value, rootType, serializer);

        // TODO: Assert the behavior
    }
    
    @Test
    public void testSerializeXmlNull() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator jsonGenerator = new ToXmlGenerator();
        
        serializerProvider._serializeXmlNull(jsonGenerator);
        
        // TODO: Assert the behavior
    }
    
    @Test
    public void testStartRootArray() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        ToXmlGenerator xmlGenerator = new ToXmlGenerator();
        QName rootName = new QName("root");
        
        serializerProvider._startRootArray(xmlGenerator, rootName);
        
        // TODO: Assert the behavior
    }
    
    @Test
    public void testInitWithRootName() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        ToXmlGenerator xmlGenerator = new ToXmlGenerator();
        QName rootName = new QName("root");
        
        serializerProvider._initWithRootName(xmlGenerator, rootName);
        
        // TODO: Assert the behavior
    }
    
    @Test
    public void testRootNameFromConfig() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        serializerProvider._config = new SerializationConfig();
        serializerProvider._config.setFullRootName(new PropertyName("root"));
        
        QName rootName = serializerProvider._rootNameFromConfig();
        
        assertEquals("root", rootName.getLocalPart());
        assertNull(rootName.getNamespaceURI());
    }
    
    @Test
    public void testAsXmlGenerator() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator generator = new ToXmlGenerator();
        
        ToXmlGenerator xmlGenerator = serializerProvider._asXmlGenerator(generator);
        
        assertEquals(generator, xmlGenerator);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithTokenBuffer() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator generator = new TokenBuffer();
        
        serializerProvider._asXmlGenerator(generator);
    }
    
    @Test
    public void testWrapAsIOE() throws Exception {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator generator = new ToXmlGenerator();
        Exception exception = new Exception("Test");
        
        IOException ioe = serializerProvider._wrapAsIOE(generator, exception);
        
        assertEquals(exception.getMessage(), ioe.getMessage());
    }
}
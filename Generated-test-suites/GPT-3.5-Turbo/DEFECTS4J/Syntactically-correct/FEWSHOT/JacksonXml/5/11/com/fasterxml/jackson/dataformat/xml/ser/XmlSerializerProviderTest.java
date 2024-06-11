package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviderImpl;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class XmlSerializerProviderTest {
    
    private XmlRootNameLookup rootNameLookup;
    private QName rootName;
    private XmlSerializerProvider xmlSerializerProvider;
    private JsonGenerator jsonGenerator;
    private Object value;
    private Class<?> cls;
    private boolean asArray;
    private ToXmlGenerator toXmlGenerator;
    private JavaType rootType;
    private JsonSerializer<Object> ser;
    
    @Before
    public void setup() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        rootName = new QName("test");
        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        jsonGenerator = mock(JsonGenerator.class);
        value = new Object();
        cls = Object.class;
        asArray = true;
        toXmlGenerator = mock(ToXmlGenerator.class);
        rootType = mock(JavaType.class);
        ser = mock(JsonSerializer.class);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(rootNameLookup, xmlSerializerProvider._rootNameLookup);
    }
    
    @Test
    public void testCopy() {
        XmlSerializerProvider copiedXmlSerializerProvider = xmlSerializerProvider.copy();
        assertEquals(xmlSerializerProvider._rootNameLookup, copiedXmlSerializerProvider._rootNameLookup);
    }
    
    @Test
    public void testCreateInstance() {
        SerializerFactory sf = mock(SerializerFactory.class);
        SerializationConfig sc = mock(SerializationConfig.class);
        XmlSerializerProvider instance = xmlSerializerProvider.createInstance(sc,sf);
        assertEquals(sc, instance.getSerializationConfig());
        assertEquals(sf, instance._serializerFactory);
    }
    
    @Test
    public void testSerializeValueReturnsVoid() throws Exception {
        xmlSerializerProvider.serializeValue(jsonGenerator, null);
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, ser);
    }
    
    @Test
    public void testSerializeValueWithJsonGenerator() throws Exception {
        xmlSerializerProvider._serializeXmlNull(jsonGenerator);
    }
    
    @Test
    public void testStartRootArray() throws Exception {
        xmlSerializerProvider._startRootArray(toXmlGenerator, rootName);
    }
    
    @Test
    public void testInitWithRootName() throws Exception {
        xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);
    }
    
    @Test
    public void testRootNameFromConfig() throws Exception {
        QName rootNameFromConfig = xmlSerializerProvider._rootNameFromConfig();
        assertNull(rootNameFromConfig);
    }
    
    @Test
    public void testAsXmlGenerator() throws Exception {
        assertNull(xmlSerializerProvider._asXmlGenerator(jsonGenerator));
        assertEquals(toXmlGenerator, xmlSerializerProvider._asXmlGenerator(toXmlGenerator));
    }
    
    @Test
    public void testWrapAsIOE() throws Exception {
        Exception e = new Exception();
        xmlSerializerProvider._wrapAsIOE(jsonGenerator, e);
    }
    
}
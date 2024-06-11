package com.fasterxml.jackson.dataformat.xml.ser;


import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    
    @Before
    public void setup() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        xmlSerializerProvider = new XmlSerializerProvider(rootNames);
    }
    
    @Test
    public void testCreateInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory sf = mock(SerializerFactory.class);
        
        xmlSerializerProvider.createInstance(config, sf);
        
        verify(config, times(1)).getFullRootName();
        verify(config, times(1)).getClass();
        verify(sf, times(1)).getClass();
        verify(sf, times(1)).getClass();
    }
    
    @Test
    public void testSerializeValue() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = mock(Object.class);
        
        xmlSerializerProvider.serializeValue(gen, value);
        
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(1)).getClass();
        verify(value, times(1)).getClass();
    }
    
    @Test
    public void testSerializeValueRootType() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = mock(Object.class);
        JavaType rootType = mock(JavaType.class);
        
        xmlSerializerProvider.serializeValue(gen, value, rootType);
        
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(1)).getClass();
        verify(value, times(1)).getClass();
        verify(rootType, times(1)).getClass();
    }
    
    @Test
    public void testSerializeValueRootTypeCustomSer() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = mock(Object.class);
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        
        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);
        
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(1)).getClass();
        verify(value, times(1)).getClass();
        verify(rootType, times(1)).getClass();
        verify(ser, times(1)).getClass();
    }
    
    @Test
    public void testSerializeXmlNull() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        
        xmlSerializerProvider._serializeXmlNull(gen);
        
        verify(gen, times(1)).writeEndObject();
        verify(gen, times(1)).getClass();
    }
    
    @Test
    public void testStartRootArray() throws Exception {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        
        xmlSerializerProvider._startRootArray(xgen, rootName);
        
        verify(xgen, times(1)).writeEndObject();
        verify(xgen, times(1)).writeFieldName("item");
        verify(xgen, times(1)).getClass();
        verify(rootName, times(1)).getClass();
    }
    
    @Test
    public void testInitWithRootName() throws Exception {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        
        xmlSerializerProvider._initWithRootName(xgen, rootName);
        
        verify(xgen, times(1)).setNextNameIfMissing(rootName);
        verify(xgen, times(1)).inRoot();
        verify(xgen, times(1)).setNextName(rootName);
        verify(xgen, times(1)).initGenerator();
        verify(rootName, times(1)).getNamespaceURI();
        verify(xgen, times(1)).getStaxWriter();
        verify(xgen.getStaxWriter(), times(1)).setDefaultNamespace(rootName.getNamespaceURI());
    }
    
    @Test
    public void testRootNameFromConfig() {
        PropertyName propertyName = mock(PropertyName.class);
        
        xmlSerializerProvider._rootNameFromConfig();
        
        verify(propertyName, times(1)).getNamespace();
        verify(propertyName, times(1)).getSimpleName();
        verify(propertyName, times(1)).getClass();
    }
    
    @Test
    public void testAsXmlGenerator() throws Exception {
        JsonGenerator gen = mock(JsonGenerator.class);
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        
        xmlSerializerProvider._asXmlGenerator(gen);
        xmlSerializerProvider._asXmlGenerator(tokenBuffer);
        
        verify(gen, times(1)).getClass();
        verify(gen, times(1)).getClass();
        verify(tokenBuffer, times(1)).getClass();
    }
}
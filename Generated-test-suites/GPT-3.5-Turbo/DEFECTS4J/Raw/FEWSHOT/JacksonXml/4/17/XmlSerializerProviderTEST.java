
package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class XmlSerializerProviderTest {
    
    @Test
    public void testConstructor() {
        XmlRootNameLookup lookup = mock(XmlRootNameLookup.class);
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(lookup);
        
        assertEquals(lookup, serializerProvider._rootNameLookup);
    }
    
    @Test
    public void testOverriddenCreateInstance() {
        DefaultSerializerProvider defaultSerializerProvider = mock(DefaultSerializerProvider.class);
        XmlRootNameLookup lookup = mock(XmlRootNameLookup.class);
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory serializerFactory = mock(SerializerFactory.class);
        
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(defaultSerializerProvider, config, serializerFactory);
        
        assertEquals(defaultSerializerProvider, serializerProvider._base._wrapped);
        assertEquals(config, serializerProvider._config);
        assertEquals(serializerFactory, serializerProvider._serializerFactory);
        assertEquals(lookup, serializerProvider._rootNameLookup);
    }
    
    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        DefaultSerializerProvider provider = mock(DefaultSerializerProvider.class);
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        serializerProvider._base = provider;
        
        serializerProvider.serializeValue(gen, value);
        
        // TODO: Verify that super method serializeValue is called correctly
    }
    
    @Test
    public void testSerializeValueWithType() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        DefaultSerializerProvider provider = mock(DefaultSerializerProvider.class);
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        serializerProvider._base = provider;
        
        serializerProvider.serializeValue(gen, value, rootType);
        
        // TODO: Verify that super method serializeValue is called correctly
    }
    
    @Test
    public void testSerializeValueWithTypeAndSerializer() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = mock(JavaType.class);
        JsonSerializer<Object> ser = mock(JsonSerializer.class);
        DefaultSerializerProvider provider = mock(DefaultSerializerProvider.class);
        TokenBuffer tokenBuffer = mock(TokenBuffer.class);
        
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        serializerProvider._base = provider;
        
        serializerProvider.serializeValue(gen, value, rootType, ser);
        
        // TODO: Verify that super method serializeValue is called correctly
    }
    
    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        serializerProvider._serializeXmlNull(gen);
        
        // TODO: Verify that super method serializeValue is called correctly
    }
    
    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        serializerProvider._startRootArray(xgen, rootName);
        
        // TODO: Verify that correct methods are called on xgen
    }
    
    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator xgen = mock(ToXmlGenerator.class);
        QName rootName = mock(QName.class);
        
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        serializerProvider._initWithRootName(xgen, rootName);
        
        // TODO: Verify that correct methods are called on xgen
    }
    
    @Test
    public void testRootNameFromConfig() {
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        QName rootName = serializerProvider._rootNameFromConfig();
        
        assertNull(rootName);
        
        // TODO: Test with a non-empty config
    }
    
    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithInvalidGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(JsonGenerator.class);
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        
        serializerProvider._asXmlGenerator(gen);
    }
    
    @Test
    public void testAsXmlGeneratorWithValidGenerator() throws JsonMappingException {
        JsonGenerator gen = mock(ToXmlGenerator.class);
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(mock(XmlRootNameLookup.class));
        
        ToXmlGenerator result = serializerProvider._asXmlGenerator(gen);
        
        assertEquals(gen, result);
    }
}

package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SerializerCache;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlTestHelper;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeXmlSerializers;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {
    
    @Mock
    private XmlRootNameLookup rootNames;

    @Mock
    private XmlSerializerProvider src;

    @Mock
    private SerializationConfig config;

    @Mock
    private SerializerFactory f;

    @Mock
    private JsonGenerator gen;

    @Mock
    private ToXmlGenerator xgen;

    @Mock
    private JavaType javaType;

    @Mock
    private JsonSerializer<Object> ser;

    private ContentHandler ch;

    private XmlSerializerProvider xmlSerializerProvider;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(src._rootNameLookup).thenReturn(rootNames);
        
        xmlSerializerProvider = new XmlSerializerProvider(src, config, f);
    }

    @Test
    public void testCreateInstance() {
        DefaultSerializerProvider provider = xmlSerializerProvider.createInstance(config, f);
        
        assertEquals(xmlSerializerProvider, provider);
    }

    @Test(expected = IOException.class)
    public void testSerializeValueWithNullValue() throws IOException {
        xmlSerializerProvider.serializeValue(gen, null);
        
        verify(xmlSerializerProvider, never())._serializeXmlNull(gen); // should never be called
        verify(src, never()).findTypedValueSerializer(null, true, null); // should never be called
        verify(ser, never()).serialize(any(), eq(gen), eq(xmlSerializerProvider)); // should never be called
    }
    
    @Test
    public void testSerializeValue() throws IOException, XMLStreamException, JsonMappingException {
        QName rootName = new QName("test");
        
        when(src._asXmlGenerator(gen)).thenReturn(xgen);
        when(rootNames.findRootName(eq(javaType), eq(config))).thenReturn(rootName);
        when(ser.findTypedValueSerializer(eq(javaType), eq(true), eq(null))).thenReturn(ser);
        
        xmlSerializerProvider.serializeValue(gen, new Object(), javaType);
        
        verify(src).findTypedValueSerializer(eq(javaType), eq(true), eq(null));
        verify(ser).serialize(any(), eq(gen), eq(xmlSerializerProvider));
        verify(xgen).initGenerator();
        verify(xgen).setNextNameIfMissing(eq(rootName));
    }

    @Test
    public void testSerializeValueWithSerializer() throws IOException, XMLStreamException, JsonMappingException {
        QName rootName = new QName("test");
        
        when(src._asXmlGenerator(gen)).thenReturn(xgen);
        when(rootNames.findRootName(eq(javaType), eq(config))).thenReturn(rootName);
        
        xmlSerializerProvider.serializeValue(gen, new Object(), javaType, ser);
        
        verify(ser).serialize(any(), eq(gen), eq(xmlSerializerProvider));
        verify(xgen).initGenerator();
        verify(xgen).setNextNameIfMissing(eq(rootName));
    }
    
    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithJsonException() throws IOException {
        when(src._asXmlGenerator(gen)).thenReturn(xgen);

        doThrow(JsonMappingException.class).when(ser).serialize(any(), eq(gen), eq(xmlSerializerProvider));
        
        xmlSerializerProvider.serializeValue(gen, new Object(), javaType, ser);
    }

    @Test
    public void testSerializeValueWhenGenNotXmlGenerator() throws IOException {
        when(src._asXmlGenerator(gen)).thenReturn(null);
        
        xmlSerializerProvider.serializeValue(gen, new Object(), javaType, ser);
        
        verify(src, never()).findTypedValueSerializer(eq(javaType), eq(true), eq(null)); // should never be called
        verify(ser, never()).serialize(any(), eq(gen), eq(xmlSerializerProvider)); // should never be called
    }
    
    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWhenGenIsNotXmlGenerator() throws IOException, XMLStreamException, JsonMappingException {
        when(src._asXmlGenerator(gen)).thenReturn(null);
        
        xmlSerializerProvider._asXmlGenerator(gen); // simulate _asXmlGenerator called before
        xmlSerializerProvider.serializeValue(gen, new Object(), javaType, ser);
    }

}
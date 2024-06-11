package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.xml.namespace.QName;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class XmlSerializerProviderTest {
    
    private XmlSerializerProvider provider;
    
    @Mock
    private DefaultSerializerProvider src;
    
    @Mock
    private SerializationConfig config;
    
    @Mock
    private SerializerFactory f;
    
    @Mock
    private JsonGenerator gen;
    
    @Mock
    private ToXmlGenerator xgen;
    
    @Mock
    private JsonSerializer<Object> ser;
    
    @Mock
    private IOException e;
    
    @Mock
    private Object value;
    
    @Mock
    private Class<?> cls;
    
    @Mock
    private JavaType rootType;
    
    @Mock
    private QName rootName;
    
    @Mock
    private QName rootNameForNull;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        provider = new XmlSerializerProvider(null);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(new XmlSerializerProvider(src, config, f));
    }
    
    @Test
    public void testCopy() {
        assertNotNull(provider.copy());
    }
    
    @Test
    public void testInitWithRootName() throws IOException {
        xgen = mock(ToXmlGenerator.class);
        when(rootName.getNamespaceURI()).thenReturn("");
        provider._initWithRootName(xgen, rootName);
        verify(xgen, times(2)).initGenerator();
        verify(xgen).getStaxWriter();
        verifyNoMoreInteractions(xgen);
    }
    
    @Test
    public void testSerializValue() throws IOException {
        when(gen.getClass()).thenReturn((Class<? extends JsonGenerator>) ToXmlGenerator.class);
        when(xgen.inRoot()).thenReturn(true);
        when(ser.serialize(value, gen, provider)).thenReturn(true);
        when(provider.findTypedValueSerializer(cls, true, null)).thenReturn(ser);
        provider.serializeValue(gen, value);
        verify(provider).findTypedValueSerializer(cls, true, null);
        verify(ser).serialize(value, gen, provider);
        verify(xgen).setNextNameIfMissing(null);
        verify(xgen).inRoot();
        verify(xgen).initGenerator();
        verify(xgen).writeStartObject();
        verify(xgen).getDefaultNamespace();
        verify(xgen).writeEndObject();
        verifyNoMoreInteractions(gen, xgen, ser);
    }
    
    @Test
    public void testSerializValueWithRootType() throws IOException {
        when(gen.getClass()).thenReturn((Class<? extends JsonGenerator>) ToXmlGenerator.class);
        when(xgen.inRoot()).thenReturn(true);
        when(ser.serialize(value, gen, provider)).thenReturn(true);
        when(provider.findTypedValueSerializer(rootType, true, null)).thenReturn(ser);
        provider.serializeValue(gen, value, rootType, ser);
        verify(provider).findTypedValueSerializer(rootType, true, null);
        verify(ser).serialize(value, gen, provider);
        verify(xgen).setNextNameIfMissing(rootName);
        verify(xgen).inRoot();
        verify(xgen).initGenerator();
        verify(xgen).writeStartObject();
        verify(xgen).getDefaultNamespace();
        verify(xgen).writeEndObject();
        verifyNoMoreInteractions(gen, xgen, ser);
    }
    
    @Test
    public void testSerializeXmlNull() throws IOException {
        when(gen.getClass()).thenReturn((Class<? extends JsonGenerator>) ToXmlGenerator.class);
        when(xgen.inRoot()).thenReturn(true);
        provider._serializeXmlNull(gen);
        verify(provider).serializeValue(gen, null);
        verify(xgen).inRoot();
        verify(xgen).initGenerator();
        verify(xgen).writeStartObject();
        verify(xgen).getDefaultNamespace();
        verify(xgen).writeEndObject();
        verifyNoMoreInteractions(gen, xgen);
    }
    
    @Test
    public void testStartRootArray() throws IOException {
        provider._startRootArray(xgen, rootName);
        verify(xgen).writeStartObject();
        verify(xgen).writeFieldName("item");
        verifyNoMoreInteractions(xgen);
    }
    
    @Test
    public void testSerializValueException() throws IOException {
        when(gen.getClass()).thenReturn((Class<? extends JsonGenerator>) ToXmlGenerator.class);
        when(xgen.inRoot()).thenReturn(true);
        when(ser.serialize(value, gen, provider)).thenThrow(e);
        when(provider._wrapAsIOE(gen, e)).thenReturn(e);
        try {
            provider.serializeValue(gen, value);
        } catch (IOException ex) {
            assertEquals(e, ex);
        }
        verify(provider).findTypedValueSerializer(cls, true, null);
        verify(ser).serialize(value, gen, provider);
        verify(provider)._wrapAsIOE(gen, e);
        verifyNoMoreInteractions(gen, xgen, ser);
    }
    
    @Test
    public void testSerializValueNotToXmlGenerator() throws IOException {
        when(gen.getClass()).thenReturn((Class<? extends JsonGenerator>) TokenBuffer.class);
        try {
            provider.serializeValue(gen, value);
        } catch (JsonMappingException ex) {
            assertEquals("XmlMapper does not with generators of type other than ToXmlGenerator; got: "+gen.getClass().getName(), ex.getMessage());
        }
        verify(gen).getClass();
        verifyNoMoreInteractions(gen, xgen, ser);
    }
    
    @Test
    public void testSerializValueNull() throws IOException {
        when(gen.getClass()).thenReturn((Class<? extends JsonGenerator>) ToXmlGenerator.class);
        when(xgen.inRoot()).thenReturn(true);
        provider.serializeValue(gen, null);
        verify(provider).serializeValue(gen, null);
        verify(xgen).inRoot();
        verify(xgen).initGenerator();
        verify(xgen).writeStartObject();
        verify(xgen).getDefaultNamespace();
        verify(xgen).writeEndObject();
        verifyNoMoreInteractions(gen, xgen);
    }
    
    @Test
    public void testSerializValueNoToXmlGenerator() throws IOException {
        provider.serializeValue(gen, value);
        verify(gen).getClass();
        verifyNoMoreInteractions(gen, xgen, ser);
    }

}
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FromStringDeserializerTest {

    @Test
    public void testFromStringDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = mock(FromStringDeserializer.Std.class, CALLS_REAL_METHODS);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.getCurrentToken()).thenReturn(null);
        
        deserializer.deserialize(jp, ctxt);
        
        verify(jp, times(1)).getCurrentToken();
    }
    
    @Test
    public void testDeserialize() throws Exception {
        FromStringDeserializer.Std deserializer = mock(FromStringDeserializer.Std.class, CALLS_REAL_METHODS);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String value = "testValue";
        when(jp.getCurrentToken()).thenReturn(null);
        when(jp.getValueAsString()).thenReturn(value);
        when(deserializer._deserialize(value, ctxt)).thenReturn(value);
        
        String result = deserializer.deserialize(jp, ctxt);
        
        verify(jp, times(1)).getCurrentToken();
        verify(jp, times(1)).getValueAsString();
        verify(deserializer, times(1))._deserialize(value, ctxt);
        assertEquals(value, result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_emptyString() throws Exception {
        FromStringDeserializer.Std deserializer = mock(FromStringDeserializer.Std.class, CALLS_REAL_METHODS);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String value = "";
        when(jp.getCurrentToken()).thenReturn(null);
        when(jp.getValueAsString()).thenReturn(value);
        when(deserializer._deserializeFromEmptyString()).thenThrow(new JsonMappingException("Invalid"));
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testDeserialize_EmbeddedObject() throws Exception {
        FromStringDeserializer.Std deserializer = mock(FromStringDeserializer.Std.class, CALLS_REAL_METHODS);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object ob = new Object();
        when(jp.getCurrentToken()).thenReturn(JsonParser.Token.VALUE_EMBEDDED_OBJECT);
        when(jp.getEmbeddedObject()).thenReturn(ob);
        when(deserializer._deserializeEmbedded(any(), any())).thenReturn(ob);
        
        Object result = deserializer.deserialize(jp, ctxt);
        
        verify(jp, times(1)).getCurrentToken();
        verify(jp, times(1)).getEmbeddedObject();
        verify(deserializer, times(1))._deserializeEmbedded(ob, ctxt);
        assertEquals(ob, result);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidToken() throws Exception {
        FromStringDeserializer.Std deserializer = mock(FromStringDeserializer.Std.class, CALLS_REAL_METHODS);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.getCurrentToken()).thenReturn(JsonParser.Token.START_OBJECT);
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_UnknownKind() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 13);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String value = "testValue";
        when(jp.getCurrentToken()).thenReturn(null);
        when(jp.getValueAsString()).thenReturn(value);
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void testStd_deserialize() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, 1);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String value = "testValue";
        when(jp.getCurrentToken()).thenReturn(null);
        when(jp.getValueAsString()).thenReturn(value);
        when(ctxt.getTypeFactory()).thenReturn(mock(JavaType.class));
        when(ctxt.getTypeFactory().constructFromCanonical(value)).thenReturn(File.class);
        
        File result = (File) deserializer.deserialize(jp, ctxt);
        
        verify(jp, times(1)).getCurrentToken();
        verify(jp, times(1)).getValueAsString();
        verify(ctxt, times(1)).getTypeFactory();
        verify(ctxt.getTypeFactory(), times(1)).constructFromCanonical(value);
        assertEquals(File.class, result.getClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStd_deserialize_unknownKind() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Object.class, 13);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String value = "testValue";
        when(jp.getCurrentToken()).thenReturn(null);
        when(jp.getValueAsString()).thenReturn(value);
        
        deserializer.deserialize(jp, ctxt);
    }
    
    @Test
    public void test_std_deserializeEmptyString() throws Exception {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, 3);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        URI result = (URI) deserializer._deserializeFromEmptyString();
        
        assertEquals(URI.create(""), result);
    }
}
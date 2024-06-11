package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.mockito.Mockito.*;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_withString_shouldReturnString() throws IOException {
        String key = "test";
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, String.class);
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_withKeyDeserializerFactory_shouldReturnObject() throws IOException, Exception {
        String key = "test";
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> keyDeserializer = mock(JsonDeserializer.class);
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonParser p = mock(JsonParser.class);
        when(ctxt.getParser()).thenReturn(p);
        when(p.nextToken()).thenReturn(null);
        when(keyDeserializer.deserialize(p, ctxt)).thenReturn(key);
        when(tb.asParser()).thenReturn(p);
        
        StdKeyDeserializer.DelegatingKD delegatingKD = new StdKeyDeserializer.DelegatingKD(String.class, keyDeserializer);
        
        Object result = delegatingKD.deserializeKey(key, ctxt);
        
        assertNotNull(result);
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_withUnknownEnum_andReadUnknownEnumValuesAsNull_shouldReturnNull() throws IOException {
        String key = "UNKNOWN_ENUM";
        DeserializationContext ctxt = mock(DeserializationContext.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(ctxt.getConfig()).thenReturn(config);
        when(config.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)).thenReturn(true);
        
        StdKeyDeserializer enumKD = new StdKeyDeserializer.EnumKD(null, null);
        
        Object result = enumKD._parse(key, ctxt);
        
        assertNull(result);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testDeserializeKey_withUnknownType_shouldThrowException() throws IOException {
        String key = "UNKNOWN";
        DeserializationContext ctxt = mock(DeserializationContext.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(ctxt.getConfig()).thenReturn(config);
        when(config.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)).thenReturn(false);
        
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, Object.class);
        
        keyDeserializer._parse(key, ctxt);
    }
}
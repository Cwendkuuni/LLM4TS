package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer.Deserializers deserializers;
    
    @Before
    public void setUp() {
        deserializers = new StdKeyDeserializer.Deserializers();
    }

    @Test
    public void testDeserializeKey_String() throws IOException {
        StdKeyDeserializer.StringKD stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        DeserializationContext ctxt = new DefaultDeserializationContext(RawValue.forValue("key"), null, null);

        Object result = stringKD.deserializeKey("key", ctxt);

        assertEquals("key", result);
    }
    
    @Test
    public void testDeserializeKey_String_NullKey() throws IOException {
        StdKeyDeserializer.StringKD stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        DeserializationContext ctxt = new DefaultDeserializationContext(RawValue.forValue(null), null, null);

        Object result = stringKD.deserializeKey(null, ctxt);

        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_Integer() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext ctxt = new DefaultDeserializationContext(RawValue.forValue("5"), null, null);
        
        Object result = keyDeserializer.deserializeKey("5", ctxt);
        
        assertEquals(5, result);
    }

    @Test
    public void testDeserializeKey_Integer_InvalidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Integer.class);
        DeserializationContext ctxt = new DefaultDeserializationContext(RawValue.forValue("abc"), null, null);

        Object result = keyDeserializer.deserializeKey("abc", ctxt);

        assertNull(result);
    }
}
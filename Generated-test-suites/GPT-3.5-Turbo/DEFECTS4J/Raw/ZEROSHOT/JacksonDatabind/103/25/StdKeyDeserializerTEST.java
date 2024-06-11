package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class StdKeyDeserializerTest {
    
    private StdKeyDeserializer stringCtorKeyDeserializer;
    private StdKeyDeserializer stringFactoryKeyDeserializer;
    
    @Before
    public void setUp() throws NoSuchMethodException {
        Constructor<?> ctor = StdKeyDeserializer.class.getDeclaredConstructor(Constructor.class);
        ctor.setAccessible(true);
        stringCtorKeyDeserializer = (StdKeyDeserializer) ctor.newInstance(String.class.getConstructor(String.class));
        
        Method fm = StdKeyDeserializer.class.getDeclaredMethod("StringFactoryKeyDeserializer", Method.class);
        stringFactoryKeyDeserializer = (StdKeyDeserializer) fm.invoke(null, String.class.getMethod("valueOf", String.class));
    }
    
    @Test
    public void testDeserializeKey_withStringCtor() throws IOException {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = stringCtorKeyDeserializer.deserializeKey(key, ctxt);
        
        assertEquals(key, result);
    }
    
    @Test
    public void testDeserializeKey_withStringFactory() throws IOException {
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = stringFactoryKeyDeserializer.deserializeKey(key, ctxt);
        
        assertEquals(key, result);
    }
}
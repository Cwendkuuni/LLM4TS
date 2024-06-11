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
    
    private StdKeyDeserializer stringKD;
    private StdKeyDeserializer enumKD;
    private StdKeyDeserializer stringCtorKD;
    private StdKeyDeserializer stringFactoryKD;
    
    @Before
    public void setUp() {
        stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        
        EnumResolver enumResolver = EnumResolver.constructUnsafeUsingToString(Enum.class, null);
        AnnotatedMethod annotatedMethod = null;
        enumKD = new StdKeyDeserializer.EnumKD(enumResolver, annotatedMethod);
        
        Constructor<?> ctor = String.class.getConstructor(String.class);
        stringCtorKD = new StdKeyDeserializer.StringCtorKeyDeserializer(ctor);
        
        Method factoryMethod = String.class.getMethod("valueOf", String.class);
        stringFactoryKD = new StdKeyDeserializer.StringFactoryKeyDeserializer(factoryMethod);
    }
    
    @Test
    public void testStringKD() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "test";
        Object result = stringKD.deserializeKey(key, ctxt);
        assertEquals(key, result);
    }
    
    @Test
    public void testEnumKD() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "VALUE";
        Object result = enumKD.deserializeKey(key, ctxt);
        assertNull(result); // no enum constant named 'VALUE' found
        
        ctxt.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        result = enumKD.deserializeKey(key, ctxt);
        assertNull(result); // no enum constant named 'VALUE' found and feature enabled
    }
    
    @Test
    public void testStringCtorKD() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "test";
        Object result = stringCtorKD._parse(key, ctxt);
        assertEquals(key, result);
    }
    
    @Test
    public void testStringFactoryKD() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null);
        String key = "test";
        Object result = stringFactoryKD._parse(key, ctxt);
        assertEquals(key, result);
    }
}
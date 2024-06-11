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

import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testForType() {
        Class<?> raw = String.class;
        StdKeyDeserializer result = StdKeyDeserializer.forType(raw);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return key;
            }
        };
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = keyDeserializer.deserializeKey(key, ctxt);
        assertNotNull(result);
        assertEquals(key, result);
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class);
        Class<?> result = keyDeserializer.getKeyClass();
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, String.class) {
            @Override
            protected Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return key;
            }
        };
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        Object result = keyDeserializer._parse(key, ctxt);
        assertNotNull(result);
        assertEquals(key, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseWithUnknownKeyType() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(20, String.class);
        String key = "test";
        DeserializationContext ctxt = new DeserializationContext(null, null);
        keyDeserializer._parse(key, ctxt);
    }

}
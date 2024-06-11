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

    private StdKeyDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        String key = "test";
        DeserializationContext context = new DeserializationContext(null, null, null);
        Object result = deserializer.deserializeKey(key, context);
        assertEquals(key, result);
    }
}
package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdKeyDeserializerTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeserializeKey_WhenKeyIsNull_ThenReturnNull() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        assertNull(keyDeserializer.deserializeKey(null, deserializationContext));
    }

    @Test
    public void testDeserializeKey_WhenKeyIsValidString_ThenReturnString() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        assertEquals("key", keyDeserializer.deserializeKey("key", deserializationContext));
    }

    @Test
    public void testDeserializeKey_WhenKeyIsInvalidString_ThenHandleWeirdKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_STRING, String.class);
        Object result = keyDeserializer.deserializeKey('a', deserializationContext);
        assertEquals(deserializationContext.handleWeirdKey(String.class, 'a', "not a valid representation"), result);
    }
}
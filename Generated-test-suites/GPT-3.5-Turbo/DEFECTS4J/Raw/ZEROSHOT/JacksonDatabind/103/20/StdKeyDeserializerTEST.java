package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setup() {
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
    }

    @Test
    public void testDeserializeKey_Success() throws IOException {
        String key = "10";
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer.deserializeKey(key, deserializationContext);
        assertEquals(Integer.valueOf(10), result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_InvalidKey() throws IOException {
        String key = "notAnInt";
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        stdKeyDeserializer.deserializeKey(key, deserializationContext);
    }
}
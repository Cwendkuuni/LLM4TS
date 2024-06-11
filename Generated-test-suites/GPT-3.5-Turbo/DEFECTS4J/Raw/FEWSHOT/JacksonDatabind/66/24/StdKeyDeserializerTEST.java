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
    public void testDeserializeKey_withNullKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        assertNull(keyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testDeserializeKey_withInvalidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(-1, String.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws IOException {
                throw new IOException("Invalid key");
            }
        };

        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        String key = "invalid_key";

        try {
            keyDeserializer.deserializeKey(key, deserializationContext);
            fail("IOException expected to be thrown for invalid key");
        } catch (IOException e) {
            assertEquals("Invalid key", e.getMessage());
        }
    }
}
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.FromStringDeserializer;

public class StdKeyDeserializerTest {
    
    private final StdKeyDeserializer deserializer = new StdKeyDeserializer() {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt)
            throws IOException
        {
            return null;
        }

        @Override
        public Object _parse(String key, DeserializationContext ctxt) throws Exception
        {
            return null;
        }
    };
    
    @Test
    public void testDeserializeKey() {
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testGetKeyClass() {
        assertEquals(null, deserializer.getKeyClass());
    }
    
    @Test
    public void testParse() throws Exception {
        assertEquals(null, deserializer._parse("", null));
    }
    
    @Test
    public void testParseInt() throws IllegalArgumentException {
        assertEquals(123, deserializer._parseInt("123"));
    }

    @Test
    public void testParseLong() throws IllegalArgumentException {
        assertEquals(1234567890, deserializer._parseLong("1234567890"));
    }

    @Test
    public void testParseDouble() throws IllegalArgumentException {
        assertEquals(123.45, deserializer._parseDouble("123.45"), 0.001);
    }

}
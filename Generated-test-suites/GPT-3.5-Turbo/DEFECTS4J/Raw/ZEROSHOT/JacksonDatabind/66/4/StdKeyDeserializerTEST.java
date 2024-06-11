package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.DelegatingKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.EnumKD;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringCtorKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringFactoryKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stdKeyDeserializer;

    @Before
    public void setup() {
        stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_URI, URI.class);
    }

    @Test
    public void testForType() {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Locale.class);

        assertEquals(StdKeyDeserializer.TYPE_LOCALE, deserializer._kind);
        assertEquals(Locale.class, deserializer._keyClass);
        assertNotNull(deserializer._deser);
    }

    @Test
    public void testDeserializeKey() throws IOException {
        DeserializationContext ctxt = null;
        String key = "http://example.com";

        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        assertEquals(URI.create(key), result);
    }

    @Test
    public void testDeserializeKeyInvalid() throws IOException {
        DeserializationContext ctxt = null;
        String key = "";

        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeKeyInvalidEnum() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        ctxt.getConfig().disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        String key = "INVALID";

        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);

        assertTrue(result instanceof DeserializationContext.WeirdKeyException);
    }
}
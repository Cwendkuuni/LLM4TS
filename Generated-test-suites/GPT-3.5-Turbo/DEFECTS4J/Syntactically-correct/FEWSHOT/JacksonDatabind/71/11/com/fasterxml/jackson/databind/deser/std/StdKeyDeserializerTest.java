package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        DeserializationContext deserializationContext = new DeserializationContext(null, null);
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        try {
            assertNull(stdKeyDeserializer.deserializeKey(null, deserializationContext));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StdKeyDeserializer stringCtorKeyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));
        try {
            assertEquals("test", stringCtorKeyDeserializer.deserializeKey("test", deserializationContext));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StdKeyDeserializer stringFactoryKeyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));
        try {
            assertEquals("test", stringFactoryKeyDeserializer.deserializeKey("test", deserializationContext));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.class, stdKeyDeserializer.getKeyClass());
    }

    @Test
    public void testPrivateMethods() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        try {
            assertNull(stdKeyDeserializer._parse(null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0, stdKeyDeserializer._parseInt("0"));
        assertEquals(0L, stdKeyDeserializer._parseLong("0"));
        assertEquals(0.0, stdKeyDeserializer._parseDouble("0"), 0);
    }

    @Test
    public void testEnumKD() {
        EnumResolver enumResolver = new EnumResolver(TestEnum.class, null, null, true);
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(enumResolver, null);

        DeserializationContext deserializationContext = new DeserializationContext(null, null);
        try {
            assertEquals(TestEnum.TEST, enumKD.deserializeKey("TEST", deserializationContext));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    enum TestEnum {
        TEST
    }
    
}
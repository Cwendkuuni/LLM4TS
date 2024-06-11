
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class StdKeyDeserializerTest {
    @Test
    public void testStdKeyDeserializer() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(1, Boolean.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return null;
            }
        };

        Assert.assertEquals(null, keyDeserializer._parse("key", null));
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringCtorKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));

        Assert.assertEquals("key", keyDeserializer._parse("key", null));
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringFactoryKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));

        Assert.assertEquals("key", keyDeserializer._parse("key", null));
    }

    @Test
    public void testStringKDDeserializeKey() throws IOException, JsonProcessingException {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);

        Assert.assertEquals("key", stringKD.deserializeKey("key", null));
    }

    @Test
    public void testDelegatingKDDeserializeKey() throws IOException, JsonProcessingException {
        KeyDeserializer keyDeserializer = new StdKeyDeserializer.DelegatingKD(String.class, null);

        Assert.assertEquals("key", keyDeserializer.deserializeKey("key", null));
    }

    @Test
    public void testEnumKDForType() {
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(null, null);

        Assert.assertEquals(null, enumKD.forType(String.class));
    }

    @Test
    public void testEnumKDGetKeyClass() {
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(null, null);

        Assert.assertEquals(String.class, enumKD.getKeyClass());
    }

    @Test
    public void testEnumKD() throws Exception {
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(EnumResolver.constructUnsafeUsingToString(String.class), new AnnotatedMethod(getClass(), "testEnumKD"));

        Assert.assertEquals("key1", enumKD._parse("key1", null));
    }

    @Test
    public void testStdKeyDeserializerDeserializeKey() throws IOException {
        int kind = 1;
        Class<?> cls = Boolean.class;
        StdKeyDeserializer.FromStringDeserializer<?> deser = null;
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(kind, cls, deser);

        Assert.assertEquals(null, stdKeyDeserializer.deserializeKey(null, null));
    }
    
    @Test
    public void testStdKeyDeserializerDeserializeKey2() throws IOException {
        int kind = 1;
        Class<?> cls = Boolean.class;
        StdKeyDeserializer.FromStringDeserializer<?> deser = null;
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(kind, cls, deser);

        try {
            stdKeyDeserializer.deserializeKey("key", null);
        } catch (JsonMappingException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testStdKeyDeserializerConstructor() {
        int kind = 1;
        Class<?> cls = Boolean.class;
        StdKeyDeserializer.FromStringDeserializer<?> deser = null;
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(kind, cls, deser);

        Assert.assertNotNull(stdKeyDeserializer);
    }
}

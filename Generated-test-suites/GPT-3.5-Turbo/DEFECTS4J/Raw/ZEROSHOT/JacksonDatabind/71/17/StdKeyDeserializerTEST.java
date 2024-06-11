package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.EnumResolver;

import org.junit.Assert;
import org.junit.Test;

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

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class, null);
        DeserializationContext deserializationContext = null;

        Object result = stdKeyDeserializer.deserializeKey("123", deserializationContext);
        Assert.assertEquals(123, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyWithInvalidInt() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class, null);
        DeserializationContext deserializationContext = null;

        stdKeyDeserializer.deserializeKey("abc", deserializationContext);
    }

    @Test
    public void testDeserializeKeyWithString() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class, null);
        DeserializationContext deserializationContext = null;

        Object result = stdKeyDeserializer.deserializeKey("java.lang.String", deserializationContext);
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyWithInvalidString() throws IOException {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, Class.class, null);
        DeserializationContext deserializationContext = null;

        stdKeyDeserializer.deserializeKey("abc", deserializationContext);
    }

    @Test
    public void testDeserializeKeyWithEnum() throws IOException {
        EnumResolver enumResolver = new EnumResolver(TestEnum.class);
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        DeserializationContext deserializationContext = null;

        Object result = stdKeyDeserializer.deserializeKey("VALUE1", deserializationContext);
        Assert.assertEquals(TestEnum.VALUE1, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKeyWithInvalidEnum() throws IOException {
        EnumResolver enumResolver = new EnumResolver(TestEnum.class);
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer.EnumKD(enumResolver, null);
        DeserializationContext deserializationContext = null;

        stdKeyDeserializer.deserializeKey("VALUE3", deserializationContext);
    }

    private enum TestEnum {
        VALUE1, VALUE2
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class, null);
        Class<?> result = stdKeyDeserializer.getKeyClass();
        Assert.assertEquals(Integer.class, result);
    }
}
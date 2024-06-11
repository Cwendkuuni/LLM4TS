
package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String_Success() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, String.class);
        DeserializationContext context = null;
        String key = "test";

        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_Long_Success() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, Long.class);
        DeserializationContext context = null;
        String key = "123456789";

        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(123456789L, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_Long_InvalidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, Long.class);
        DeserializationContext context = null;
        String key = "test";

        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testDeserializeKey_Float_Success() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, Float.class);
        DeserializationContext context = null;
        String key = "3.14";

        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(3.14f, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_Float_InvalidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, Float.class);
        DeserializationContext context = null;
        String key = "test";

        keyDeserializer.deserializeKey(key, context);
    }

    @Test
    public void testDeserializeKey_Enum_Success() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, MyEnum.class);
        DeserializationContext context = null;
        String key = "VALUE_ONE";

        Object result = keyDeserializer.deserializeKey(key, context);

        Assert.assertEquals(MyEnum.VALUE_ONE, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_Enum_InvalidKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(0, MyEnum.class);
        DeserializationContext context = null;
        String key = "test";

        keyDeserializer.deserializeKey(key, context);
    }

    private enum MyEnum {
        VALUE_ONE,
        VALUE_TWO
    }

}


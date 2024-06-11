
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        DeserializationContext ctxt = null;

        // Test with null key
        Object result = keyDeserializer.deserializeKey(null, ctxt);
        Assert.assertNull(result);

        // Test with valid key
        result = keyDeserializer.deserializeKey("123", ctxt);
        Assert.assertEquals(123, result);

        // Test with invalid key
        try {
            result = keyDeserializer.deserializeKey("abcd", ctxt);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetStringCtorKeyDeserializer() throws IOException {
        StdKeyDeserializer.StringCtorKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));

        DeserializationContext ctxt = null;

        // Test with null key
        Object result = keyDeserializer._parse(null, ctxt);
        Assert.assertNull(result);

        // Test with valid key
        result = keyDeserializer._parse("test", ctxt);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testGetStringFactoryKeyDeserializer() throws IOException {
        StdKeyDeserializer.StringFactoryKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));

        DeserializationContext ctxt = null;

        // Test with null key
        Object result = keyDeserializer._parse(null, ctxt);
        Assert.assertNull(result);

        // Test with valid key
        result = keyDeserializer._parse("test", ctxt);
        Assert.assertEquals("test", result);
    }

}

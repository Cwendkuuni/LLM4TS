package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer kd = new StringKD(String.class);

        DeserializationContext ctxt = null;
        try {
            kd.deserializeKey(null, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("null cannot be deserialized as key of type java.lang.String", e.getMessage());
        }

        try {
            kd.deserializeKey("", ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("\"\" cannot be deserialized as key of type java.lang.String", e.getMessage());
        }
    }

    private static class StringKD extends KeyDeserializer {
        private static final long serialVersionUID = 1L;

        protected StringKD(Class<?> nominalType) {
            super(nominalType);
        }

        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            return key;
        }

    }

}
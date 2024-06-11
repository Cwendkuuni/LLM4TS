package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {
    private FromStringDeserializer.Std deserializer;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer.Std(Object.class, 1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String value = "test";
        assertNotNull(deserializer._deserialize(value, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_invalidValue() throws Exception {
        String value = "";
        deserializer._deserialize(value, null);
    }

    @Test
    public void testDeserializeFromEmptyString() throws Exception {
        assertNull(deserializer._deserializeFromEmptyString());
    }
}
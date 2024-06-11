package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsonValueSerializerTest {
    private JsonValueSerializer serializer;

    @Before
    public void setUp() {
        serializer = new JsonValueSerializer(null, null);
    }

    @Test
    public void testConstructorAndGetters() {
        assertNull(serializer._property);

        serializer = new JsonValueSerializer(null, null, null, true);
        assertTrue(serializer._forceTypeInformation);

        serializer = new JsonValueSerializer(null, null, null, false);
        assertFalse(serializer._forceTypeInformation);
    }

    @Test
    public void testWithResolved() {
        assertEquals(serializer, serializer.withResolved(null, null, true));
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        assertFalse(serializer.isNaturalTypeWithStdHandling(null, null));
    }

    @Test
    public void testToString() {
        assertNotNull(serializer.toString());
    }
}
package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }
}
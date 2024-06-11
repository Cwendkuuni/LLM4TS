package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointerConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        String expected = "";
        String actual = jsonPointer.toString();
        assertEquals(expected, actual);
    }
}
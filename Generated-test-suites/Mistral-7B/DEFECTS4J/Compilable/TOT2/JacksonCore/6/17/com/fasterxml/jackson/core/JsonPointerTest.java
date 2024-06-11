package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonPointerTest {

    @Test
    public void testCompileEmpty() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, pointer);
    }

    @Test
    public void testCompileValidJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("/a/0/b");
        assertEquals("/a/0/b", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointerStartsWithCharacter() {
        JsonPointer.compile("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointerEmptySegment() {
        JsonPointer.compile("/");
    }

}
package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertEquals("/", emptyJsonPointer.toString());
        assertEquals(emptyJsonPointer, emptyJsonPointer.tail());
        assertTrue(emptyJsonPointer.matches());
        assertFalse(emptyJsonPointer.mayMatchProperty());
        assertFalse(emptyJsonPointer.mayMatchElement());
        assertNull(emptyJsonPointer.getMatchingProperty());
        assertNull(emptyJsonPointer.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String input = "/test";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/test", jsonPointer.toString());
        assertEquals(null, jsonPointer.tail());
        assertFalse(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithDoubleSegments() {
        String input = "/test/sub";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/test/sub", jsonPointer.toString());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertFalse(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithIndex() {
        String input = "/test/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/test/0", jsonPointer.toString());
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
        assertFalse(jsonPointer.matches());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInput() {
        JsonPointer.compile("./test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInput2() {
        JsonPointer.compile("/test./");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInput3() {
        JsonPointer.compile("/test/-1");
    }

    @Test
    public void testJsonPointerMatches() {
        String input = "/test/sub/0";
        JsonPointer jsonPointer =JsonPointer.compile(input);
        Object obj = new Object() {{
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("sub", new ArrayList<Object>() {{
                add(new Object());
            }});
            list.add(map);
        }};
        assertEquals(obj.getClass().getDeclaredField("list"), jsonPointer.matchProperty("/test"));
        assertEquals(obj.getClass().getDeclaredField("list").get(obj).getClass().getDeclaredField("list").get(0).getClass(), jsonPointer.matchElement("/test/0"));
    }

    @Test
    public void testJsonPointerTail() {
        String input = "/test/sub/0";
        JsonPointer jsonPointer =JsonPointer.compile(input);
        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertEquals("/test/sub", tailJsonPointer.toString());
        assertEquals(JsonPointer.EMPTY, tailJsonPointer.tail());
        assertFalse(tailJsonPointer.matches());
        assertTrue(tailJsonPointer.mayMatchProperty());
        assertFalse(tailJsonPointer.mayMatchElement());
        assertEquals("sub", tailJsonPointer.getMatchingProperty());
        assertNull(tailJsonPointer.getMatchingIndex());
    }
}
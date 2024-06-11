import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        // Test when the JsonPointer matches the current node
        JsonPointer jsonPointer = new JsonPointer(); // Empty JsonPointer
        assertTrue(jsonPointer.matches());
        
        // Test when the JsonPointer does not match the current node
        jsonPointer = JsonPointer.compile("/foo");
        assertFalse(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        // Test when the JsonPointer matches a property
        JsonPointer jsonPointer = JsonPointer.compile("/foo");
        assertEquals("foo", jsonPointer.getMatchingProperty());

        // Test when the JsonPointer matches an index
        jsonPointer = JsonPointer.compile("/0");
        assertNull(jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        // Test when the JsonPointer matches an index
        JsonPointer jsonPointer = JsonPointer.compile("/0");
        assertEquals(0, jsonPointer.getMatchingIndex());

        // Test when the JsonPointer matches a property
        jsonPointer = JsonPointer.compile("/foo");
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        // Test when the JsonPointer may match a property
        JsonPointer jsonPointer = JsonPointer.compile("/foo");
        assertTrue(jsonPointer.mayMatchProperty());

        // Test when the JsonPointer may not match a property
        jsonPointer = JsonPointer.compile("/0");
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        // Test when the JsonPointer may match an index
        JsonPointer jsonPointer = JsonPointer.compile("/0");
        assertTrue(jsonPointer.mayMatchElement());

        // Test when the JsonPointer may not match an index
        jsonPointer = JsonPointer.compile("/foo");
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        // Test when the JsonPointer matches the given property
        JsonPointer jsonPointer = JsonPointer.compile("/foo");
        JsonPointer result = jsonPointer.matchProperty("foo");
        assertNotNull(result);
        assertTrue(result.matches());

        // Test when the JsonPointer does not match the given property
        result = jsonPointer.matchProperty("bar");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        // Test when the JsonPointer matches the given index
        JsonPointer jsonPointer = JsonPointer.compile("/0");
        JsonPointer result = jsonPointer.matchElement(0);
        assertNotNull(result);
        assertTrue(result.matches());

        // Test when the JsonPointer does not match the given index
        result = jsonPointer.matchElement(1);
        assertNull(result);
    }

    @Test
    public void testTail() {
        // Test when the JsonPointer has a tail
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertNotNull(tailJsonPointer);
        assertFalse(tailJsonPointer.matches());
        assertEquals("bar", tailJsonPointer.getMatchingProperty());

        // Test when the JsonPointer does not have a tail
        jsonPointer = JsonPointer.compile("/foo");
        tailJsonPointer = jsonPointer.tail();
        assertNull(tailJsonPointer);
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/foo");
        JsonPointer jsonPointer2 = JsonPointer.compile("/foo");
        JsonPointer jsonPointer3 = JsonPointer.compile("/bar");

        // Test when the JsonPointers are equal
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertTrue(jsonPointer2.equals(jsonPointer1));

        // Test when the JsonPointers are not equal
        assertFalse(jsonPointer1.equals(jsonPointer3));
        assertFalse(jsonPointer3.equals(jsonPointer1));

        // Test when comparing with a different object type
        assertFalse(jsonPointer1.equals("foo"));
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/foo");
        JsonPointer jsonPointer2 = JsonPointer.compile("/foo");
        JsonPointer jsonPointer3 = JsonPointer.compile("/bar");

        // Test when the JsonPointers have the same hash code
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());

        // Test when the JsonPointers have different hash codes
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
    }

    @Test
    public void testParseIndex() {
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(10, JsonPointer._parseIndex("10"));
        assertEquals(-1, JsonPointer._parseIndex("-1"));
        assertEquals(-1, JsonPointer._parseIndex("abc"));
    }

    @Test
    public void testParseTail() {
        // Test when the input has multiple segments
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertNotNull(jsonPointer);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertNotNull(jsonPointer.tail());
        assertFalse(jsonPointer.tail().matches());
        assertEquals("bar", jsonPointer.tail().getMatchingProperty());
        assertNull(jsonPointer.tail().tail());

        // Test when the input has no segments
        jsonPointer = JsonPointer.compile("/");
        assertNotNull(jsonPointer);
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testFindChild() {
        // Test when the JsonPointer points to a property
        JsonPointer parentJsonPointer = JsonPointer.compile("/foo");
        JsonPointer childJsonPointer = JsonPointer.compile("/foo/bar");
        assertEquals(childJsonPointer, parentJsonPointer.tail());

        // Test when the JsonPointer points to an index
        parentJsonPointer = JsonPointer.compile("/0");
        childJsonPointer = JsonPointer.compile("/0/foo");
        assertEquals(childJsonPointer, parentJsonPointer.tail());
    }

    @Test
    public void testCreateAbsolutePath() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo");
        JsonPointer childJsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer absoluteChildJsonPointer = jsonPointer.createAbsolutePath(childJsonPointer);
        assertEquals(childJsonPointer, absoluteChildJsonPointer);

        jsonPointer = JsonPointer.compile("/0");
        childJsonPointer = JsonPointer.compile("/0/foo");
        absoluteChildJsonPointer = jsonPointer.createAbsolutePath(childJsonPointer);
        assertEquals(childJsonPointer, absoluteChildJsonPointer);
    }

    @Test
    public void testCreateRelativePath() {
        JsonPointer parentJsonPointer = JsonPointer.compile("/foo");
        JsonPointer childJsonPointer = JsonPointer.compile("/foo/bar");
        JsonPointer relativeChildJsonPointer = parentJsonPointer.createRelativePath(childJsonPointer);
        assertEquals(childJsonPointer, relativeChildJsonPointer);

        parentJsonPointer = JsonPointer.compile("/0");
        childJsonPointer = JsonPointer.compile("/0/foo");
        relativeChildJsonPointer = parentJsonPointer.createRelativePath(childJsonPointer);
        assertEquals(childJsonPointer, relativeChildJsonPointer);
    }
}
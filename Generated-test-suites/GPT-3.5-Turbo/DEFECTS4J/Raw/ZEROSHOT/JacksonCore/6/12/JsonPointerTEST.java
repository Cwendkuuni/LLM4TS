import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer();
        
        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), new JsonPointer()._asString.hashCode());
        assertTrue(jsonPointer.equals(jsonPointer));
    }
    
    @Test
    public void testJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);

        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
        assertEquals("fullString", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), new JsonPointer("fullString", "segment", null)._asString.hashCode());
        assertTrue(jsonPointer.equals(new JsonPointer("fullString", "segment", null)));
    }
    
    @Test
    public void testCompileEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("");

        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), JsonPointer.EMPTY._asString.hashCode());
        assertTrue(jsonPointer.equals(JsonPointer.EMPTY));
    }
    
    @Test
    public void testCompileJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/segment");

        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
        assertEquals("/segment", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), JsonPointer.compile("/segment")._asString.hashCode());
        assertTrue(jsonPointer.equals(JsonPointer.compile("/segment")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCompileJsonPointer() {
        JsonPointer.compile("segment");
    }
    
    @Test
    public void testValueOfJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/segment");

        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
        assertEquals("/segment", jsonPointer.toString());
        assertEquals(jsonPointer.hashCode(), JsonPointer.compile("/segment")._asString.hashCode());
        assertTrue(jsonPointer.equals(JsonPointer.compile("/segment")));
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/segment");

        JsonPointer propertyMatch = jsonPointer.matchProperty("segment");

        assertFalse(propertyMatch.matches());
        assertEquals("segment", propertyMatch.getMatchingProperty());
        assertEquals(-1, propertyMatch.getMatchingIndex());
        assertTrue(propertyMatch.mayMatchProperty());
        assertFalse(propertyMatch.mayMatchElement());
        assertNull(propertyMatch.matchProperty("name"));
        assertNull(propertyMatch.matchElement(0));
        assertNull(propertyMatch.tail());
        assertEquals("segment", propertyMatch.toString());
        assertEquals(propertyMatch.hashCode(), JsonPointer.compile("/segment")._asString.hashCode());
        assertTrue(propertyMatch.equals(JsonPointer.compile("/segment")));
    }
    
    @Test
    public void testMatchPropertyWithNextSegment() {
        JsonPointer jsonPointer = JsonPointer.compile("/segment/nextsegment");

        JsonPointer propertyMatch = jsonPointer.matchProperty("segment");

        assertFalse(propertyMatch.matches());
        assertEquals("segment", propertyMatch.getMatchingProperty());
        assertEquals(-1, propertyMatch.getMatchingIndex());
        assertTrue(propertyMatch.mayMatchProperty());
        assertFalse(propertyMatch.mayMatchElement());
        assertNull(propertyMatch.matchProperty("name"));
        assertNull(propertyMatch.matchElement(0));
        assertNotNull(propertyMatch.tail());
        assertEquals("/nextsegment", propertyMatch.tail().toString());
        assertEquals("segment", propertyMatch.toString());
        assertEquals(propertyMatch.hashCode(), JsonPointer.compile("/segment/nextsegment")._asString.hashCode());
        assertTrue(propertyMatch.equals(JsonPointer.compile("/segment/nextsegment")));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/0");

        JsonPointer elementMatch = jsonPointer.matchElement(0);

        assertFalse(elementMatch.matches());
        assertNull(elementMatch.getMatchingProperty());
        assertEquals(0, elementMatch.getMatchingIndex());
        assertFalse(elementMatch.mayMatchProperty());
        assertTrue(elementMatch.mayMatchElement());
        assertNull(elementMatch.matchProperty("name"));
        assertNull(elementMatch.matchElement(1));
        assertNull(elementMatch.tail());
        assertEquals("0", elementMatch.toString());
        assertEquals(elementMatch.hashCode(), JsonPointer.compile("/0")._asString.hashCode());
        assertTrue(elementMatch.equals(JsonPointer.compile("/0")));
    }
    
    @Test
    public void testMatchElementWithNextSegment() {
        JsonPointer jsonPointer = JsonPointer.compile("/0/1");

        JsonPointer elementMatch = jsonPointer.matchElement(0);

        assertFalse(elementMatch.matches());
        assertNull(elementMatch.getMatchingProperty());
        assertEquals(0, elementMatch.getMatchingIndex());
        assertFalse(elementMatch.mayMatchProperty());
        assertTrue(elementMatch.mayMatchElement());
        assertNull(elementMatch.matchProperty("name"));
        assertNull(elementMatch.matchElement(1));
        assertNotNull(elementMatch.tail());
        assertEquals("/1", elementMatch.tail().toString());
        assertEquals("0", elementMatch.toString());
        assertEquals(elementMatch.hashCode(), JsonPointer.compile("/0/1")._asString.hashCode());
        assertTrue(elementMatch.equals(JsonPointer.compile("/0/1")));
    }
    
    
}
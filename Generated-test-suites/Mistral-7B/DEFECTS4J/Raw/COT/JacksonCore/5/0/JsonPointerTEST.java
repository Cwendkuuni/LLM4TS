import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testStringConstructor() {
        JsonPointer jsonPointer = new JsonPointer("/test", "test", null);
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/test", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer jsonPointer = new JsonPointer("test");
    }

    @Test
    public void testCompileMethod() throws IllegalArgumentException {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/test", jsonPointer.toString());
    }

    @Test
    public void testValueOfMethod() throws IllegalArgumentException {
        JsonPointer jsonPointer = JsonPointer.valueOf("/test");
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/test", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputForCompileMethod() throws IllegalArgumentException {
        JsonPointer.compile("test");
    }

    @Test
    public void testFromSegmentMethod() {
        //TODO: Implement this test case once the fromSegment method is implemented
    }

    @Test
    public void testMatchesMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property/element");

        assertTrue(jsonPointer1.matches());
        assertFalse(jsonPointer2.matches());
        assertFalse(jsonPointer3.matches());
    }

    @Test
    public void testGetMatchingPropertyMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property/element");

        assertEquals("", jsonPointer1.getMatchingProperty());
        assertEquals("property", jsonPointer2.getMatchingProperty());
        assertEquals("element", jsonPointer3.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndexMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property/0");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property/1");

        assertEquals(-1, jsonPointer1.getMatchingIndex());
        assertEquals(0, jsonPointer2.getMatchingIndex());
        assertEquals(1, jsonPointer3.getMatchingIndex());
    }

    @Test
    public void testMayMatchPropertyMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test");

        assertTrue(jsonPointer2.mayMatchProperty());
        assertTrue(jsonPointer3.mayMatchProperty());
        assertFalse(jsonPointer1.mayMatchProperty());
    }

    @Test
    public void testMayMatchElementMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/0");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test");

        assertTrue(jsonPointer1.mayMatchElement());
        assertFalse(jsonPointer2.mayMatchElement());
        assertFalse(jsonPointer3.mayMatchElement());
    }

    @Test
    public void testMatchPropertyMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = new JsonPointer("/test/anotherProperty");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");

        assertEquals(jsonPointer3, jsonPointer1.matchProperty("property"));
        assertNull(jsonPointer1.matchProperty("anotherProperty"));
        assertNull(jsonPointer1.matchProperty(null));
    }

    @Test
    public void testMatchElementMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/0");
        JsonPointer jsonPointer2 = new JsonPointer("/test/1");
        JsonPointer jsonPointer3 = new JsonPointer("/test");

        assertEquals(jsonPointer1, jsonPointer1.matchElement(0));
        assertEquals(jsonPointer2, jsonPointer1.matchElement(1));
        assertNull(jsonPointer1.matchElement(-1));
        assertNull(jsonPointer1.matchElement(null));
    }

    @Test
    public void testTailMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test/property");
        JsonPointer jsonPointer2 = jsonPointer1.tail();

        assertEquals(jsonPointer2, jsonPointer1.tail());
        assertFalse(jsonPointer1.matches());
        assertTrue(jsonPointer2.matches());
    }

    @Test
    public void testToStringMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test/property");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property/element");

        assertEquals("/test", jsonPointer1.toString());
        assertEquals("/test/property", jsonPointer2.toString());
        assertEquals("/test/property/element", jsonPointer3.toString());
    }

    @Test
    public void testHashCodeMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");

        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        JsonPointer jsonPointer3 = new JsonPointer("/test/property");

        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
        assertFalse(jsonPointer1.equals(null));
        assertFalse(jsonPointer1.equals(""));
    }

    @Test
    public void testParseIndexMethod() {
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(1, JsonPointer._parseIndex("1"));
        assertEquals(-1, JsonPointer._parseIndex(""));
        assertEquals(-1, JsonPointer._parseIndex("test"));
        assertEquals(-1, JsonPointer._parseIndex("10"));
    }
}
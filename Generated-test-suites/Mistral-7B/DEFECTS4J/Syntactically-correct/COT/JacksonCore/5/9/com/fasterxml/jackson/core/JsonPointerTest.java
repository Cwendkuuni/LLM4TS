import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor(){
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testNonEmptyConstructor() throws IllegalArgumentException {
        String fullString = "/a/b/c";
        String segment = "b";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() throws IllegalArgumentException {
        String input = "abc";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatchProperty_Matches() {
        String fullString = "/a/b/c";
        String matchingProperty = "b";
        JsonPointer jsonPointer = new JsonPointer(fullString, matchingProperty, null);
        JsonPointer expectedResult = new JsonPointer("/c");
        JsonPointer result = jsonPointer.matchProperty(matchingProperty);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatchProperty_DoesNotMatch() {
        String fullString = "/a/b/c";
        String matchingProperty = "d";
        JsonPointer jsonPointer = new JsonPointer(fullString, "b", null);
        assertNull(jsonPointer.matchProperty(matchingProperty));
    }

    @Test
    public void testMatchElement_Matches() {
        String fullString = "[1,2,3]/1";
        int matchingIndex = 1;
        JsonPointer jsonPointer = new JsonPointer(fullString, "", null);
        JsonPointer expectedResult = new JsonPointer("[1]");
        JsonPointer result = jsonPointer.matchElement(matchingIndex);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testMatchElement_DoesNotMatch() {
        String fullString = "[1,2,3]/1";
        int matchingIndex = 0;
        JsonPointer jsonPointer = new JsonPointer(fullString, "", null);
        assertNull(jsonPointer.matchElement(matchingIndex));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndex() {
        String fullString = "[1,2,3]";
        int matchingIndex = -1;
        JsonPointer jsonPointer = new JsonPointer(fullString, "", null);
        jsonPointer.matchElement(matchingIndex);
    }

    @Test
    public void testTail() {
        String fullString = "/a/b/c";
        JsonPointer jsonPointer = new JsonPointer(fullString, "b", null);
        JsonPointer expectedResult = new JsonPointer("/c");
        JsonPointer result = jsonPointer.tail();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToString() {
        String fullString = "/a/b/c";
        String segment = "b";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
        assertEquals("/a/b", jsonPointer.toString());
    }

    @Test
    public void testEquals() {
        String fullString1 = "/a/b/c";
        String fullString2 = "/a/b/c";
        JsonPointer jsonPointer1 = new JsonPointer(fullString1, "", null);
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, "", null);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testHashCode() {
        String fullString1 = "/a/b/c";
        String fullString2 = "/a/b/c";
        JsonPointer jsonPointer1 = new JsonPointer(fullString1, "", null);
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, "", null);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }
}
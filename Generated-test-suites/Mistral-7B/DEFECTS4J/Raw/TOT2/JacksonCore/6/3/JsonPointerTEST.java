
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    // Test cases for compile method

    // Test case for the compile method with valid input
    @Test
    public void testJsonPointerCompilation() {
        String input = "/foo/bar";
        JsonPointer expected = new JsonPointer("/foo/bar");
        JsonPointer actual = JsonPointer.compile(input);
        assertEquals(expected, actual);
    }

    // Test case for the compile method with an empty input
    @Test
    public void testJsonPointerEmptyCompilation() {
        String input = "";
        JsonPointer expected = JsonPointer.EMPTY;
        JsonPointer actual = JsonPointer.compile(input);
        assertEquals(expected, actual);
    }

    // Test case for the compile method with an invalid input
    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerInvalidCompilation() {
        String input = "baz";
        JsonPointer.compile(input);
    }

    // Test cases for matchProperty method

    // Test case for the matchProperty method with a matching property
    @Test
    public void testJsonPointerMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        String expectedPropertyName = "bar";
        String actualPropertyName = pointer.matchProperty("bar").getMatchingProperty();
        assertEquals(expectedPropertyName, actualPropertyName);
    }

    // Test case for the matchProperty method with a non-matching property
    @Test
    public void testJsonPointerNonMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        String actualPropertyName = pointer.matchProperty("qux").getMatchingProperty();
        assertNull(actualPropertyName);
    }

    // Test cases for matchElement method

    // Test case for the matchElement method with a matching index
    @Test
    public void testJsonPointerMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo/0/bar");
        int expectedIndex = 0;
        int actualIndex = pointer.matchElement(expectedIndex).getMatchingIndex();
        assertEquals(expectedIndex, actualIndex);
    }

    // Test case for the matchElement method with a non-matching index
    @Test
    public void testJsonPointerNonMatchingElement() {
        JsonPointer pointer = new JsonPointer("/foo/0/bar");
        int actualIndex = pointer.matchElement(1).getMatchingIndex();
        assertEquals(-1, actualIndex);
    }

    // Test cases for toString method

    // Test case for the toString method with a simple pointer
    @Test
    public void testJsonPointerToStringSimple() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        String expected = "/foo/bar";
        String actual = pointer.toString();
        assertEquals(expected, actual);
    }

    // Test case for the toString method with a complex pointer
    @Test
    public void testJsonPointerToStringComplex() {
        JsonPointer pointer = new JsonPointer("/foo/0/bar/baz/qux[1]/quux");
        String expected = "/foo/0/bar/baz/qux[1]/quux";
        String actual = pointer.toString();
        assertEquals(expected, actual);
    }
}

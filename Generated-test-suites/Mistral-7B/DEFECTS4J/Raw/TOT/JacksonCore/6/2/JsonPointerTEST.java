import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testBasicPointer() {
        String pointerInput = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(pointerInput);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(pointerInput, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerInputStartsWithSlash() {
        String pointerInput = "/foo";
        JsonPointer.compile(pointerInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerInputDoesNotStartWithSlash() {
        String pointerInput = "foo";
        JsonPointer.compile(pointerInput);
    }

    @Test
    public void testComposePointer() {
        String pointerInput1 = "/foo";
        JsonPointer pointer1 = JsonPointer.compile(pointerInput1);

        String pointerInput2 = "/bar";
        JsonPointer pointer2 = JsonPointer.compile(pointerInput2);

        JsonPointer pointer3 = JsonPointer.fromSegment(pointer1, pointer2);

        assertTrue(pointer3.matches());
        assertEquals("foo/bar", pointer3.toString());
        assertNull(pointer3.tail());
    }

    @Test
    public void testMatchProperty() {
        String pointerInput = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(pointerInput);

        String matchingProperty = "foo";
        JsonPointer matchedProperty = pointer.matchProperty(matchingProperty);

        assertTrue(matchedProperty.matches());
        assertEquals(matchingProperty, matchedProperty.getMatchingProperty());
        assertNull(matchedProperty.tail());
        assertEquals(pointerInput, matchedProperty.toString());

        String mismatchedProperty = "baz";
        JsonPointer mismatchedPropertyPointer = pointer.matchProperty(mismatchedProperty);
        assertNull(mismatchedPropertyPointer);
    }

    @Test
    public void testMatchElement() {
        String pointerInput = "/foo/1";
        JsonPointer pointer = JsonPointer.compile(pointerInput);

        int matchingIndex = 1;
        JsonPointer matchedElement = pointer.matchElement(matchingIndex);

        assertTrue(matchedElement.matches());
        assertEquals("1", matchedElement.toString());
        assertNull(matchedElement.tail());

        int mismatchedIndex = 0;
        JsonPointer mismatchedElement = pointer.matchElement(mismatchedIndex);
        assertNull(mismatchedElement);

        int negativeIndex = -1;
        JsonPointer negativeElement = pointer.matchElement(negativeIndex);
        assertNull(negativeElement);
    }
}
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String invalidInput = "abcd";
        JsonPointer pointer = JsonPointer.compile(invalidInput);
        fail("Expected IllegalArgumentException but got: " + pointer);
    }

    @Test
    public void testSimplePointer() {
        String pointerString = "/abc/def";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertTrue(pointer.matches());
        assertEquals("abc", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals(pointerString, pointer.toString());
    }

    @Test
    public void testPointerWithTail() {
        String pointerString = "/abc/def/123";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("def", pointer.getMatchingProperty());
        assertEquals(123, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertTrue(tail.matches());
        assertEquals("/123", tail.toString());
    }

    @Test
    public void testPointerWithPropertyMatch() {
        String pointerString = "/list[0]/name";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("name", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testPointerWithElementMatch() {
        String pointerString = "/list[1]/elements[2]";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("elements", pointer.getMatchingProperty());
        assertEquals(2, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("/list[1]", tail.toString());
        JsonPointer tailTail = tail.tail();
        assertEquals("", tailTail.getMatchingProperty());
        assertEquals(-1, tailTail.getMatchingIndex());
        assertTrue(tailTail.matches());
    }

    @Test
    public void testPointerWithPropertyAndElementMatch() {
        String pointerString = "/people[0]/name";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("name", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String pointerString = "/list/0/item[1]/name";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertFalse(pointer.matches());
        assertEquals("item", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("/list/0/item", tail.toString());
        JsonPointer tailTail = tail.tail();
        assertEquals("", tailTail.getMatchingProperty());
        assertEquals(-1, tailTail.getMatchingIndex());
        assertTrue(tailTail.matches());
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() throws JsonProcessingException {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
    }

    @Test
    public void testSimplePointer() throws JsonProcessingException {
        String pointerString = "/testProperty";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertEquals("testProperty", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() throws JsonProcessingException {
        String pointerString = "testProperty";
        JsonPointer.compile(pointerString);
    }

    @Test
    public void testComplexPointer() throws JsonProcessingException, IOException {
        String pointerString = "/testArray[0]/testProperty";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertTrue(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertEquals("testProperty", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() throws JsonProcessingException {
        String pointerString = "/testObject/testProperty";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        JsonPointer matchingPointer = new JsonPointer("/testObject", null, null);
        JsonPointer nextPointer = new JsonPointer("", "testProperty", null);
        assertEquals(nextPointer, pointer.matchProperty("testProperty"));
        assertNull(pointer.matchProperty("testProperty2"));
        assertNull(pointer.matchProperty("testArray"));
        assertNull(pointer.matchProperty(""));
    }

    @Test
    public void testMatchElement() throws JsonProcessingException {
        String pointerString = "/testArray[0]";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        JsonPointer matchingPointer = new JsonPointer("/testArray", null, null);
        JsonPointer nextPointer = new JsonPointer("", "", new JsonPointer("", "", null));
        assertEquals(nextPointer, pointer.matchElement(0));
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.matchElement(1));
        assertNull(pointer.matchElement(""));
    }

    @Test
    public void testTail() throws JsonProcessingException {
        String pointerString = "/testArray/0";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        JsonPointer nextPointer = new JsonPointer("", "", new JsonPointer("", "", null));
        assertEquals(nextPointer, pointer.tail());
    }

    @Test
    public void testToString() throws JsonProcessingException {
        String pointerString = "/testArray[0]/testProperty";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertEquals(pointerString, pointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() throws JsonProcessingException {
        String pointerString1 = "/testArray[0]/testProperty";
        String pointerString2 = "/testArray[0]/testProperty";
        JsonPointer pointer1 = JsonPointer.compile(pointerString1);
        JsonPointer pointer2 = JsonPointer.compile(pointerString2);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertTrue(pointer1.equals(pointer2));
        JsonPointer pointer3 = new JsonPointer("/testArray[1]/testProperty", null, null);
        assertFalse(pointer1.equals(pointer3));
        JsonPointer pointer4 = new JsonPointer("/testArray[0]/testProperty2", null, null);
        assertFalse(pointer1.equals(pointer4));
    }
}
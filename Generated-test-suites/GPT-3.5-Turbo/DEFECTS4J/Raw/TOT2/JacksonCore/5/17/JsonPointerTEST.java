import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    private JsonPointer emptyPointer;
    private JsonPointer nonEmptyPointer;

    @Before
    public void setUp() {
        emptyPointer = JsonPointer.EMPTY;
        nonEmptyPointer = new JsonPointer("/parent/child");
    }

    @Test
    public void testMatches() {
        assertTrue(emptyPointer.matches());
        assertFalse(nonEmptyPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        assertEquals("", emptyPointer.getMatchingProperty());
        assertEquals("child", nonEmptyPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertEquals(-1, nonEmptyPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        assertTrue(emptyPointer.mayMatchProperty());
        assertTrue(nonEmptyPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        assertFalse(emptyPointer.mayMatchElement());
        assertFalse(nonEmptyPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        assertNull(emptyPointer.matchProperty("test"));
        assertEquals(nonEmptyPointer.tail(), nonEmptyPointer.matchProperty("child"));
    }

    @Test
    public void testMatchElement() {
        assertNull(emptyPointer.matchElement(1));
        assertNull(nonEmptyPointer.matchElement(0));
    }

    @Test
    public void testTail() {
        assertEquals(JsonPointer.EMPTY, emptyPointer.tail());
        assertNotNull(nonEmptyPointer.tail());
    }

    @Test
    public void testToString() {
        assertEquals("", emptyPointer.toString());
        assertEquals("/parent/child", nonEmptyPointer.toString());
    }

}
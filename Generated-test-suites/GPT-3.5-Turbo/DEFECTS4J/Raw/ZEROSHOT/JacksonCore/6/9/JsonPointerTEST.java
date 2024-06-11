import com.fasterxml.jackson.core.JsonPointer;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {
    private static JsonPointer emptyPointer;
    private static JsonPointer nonEmptyPointer;

    @BeforeClass
    public static void setUp() {
        emptyPointer = new JsonPointer();
        nonEmptyPointer = new JsonPointer("test", "segment", null);
    }

    @Test
    public void testEmptyPointer() {
        assertTrue(emptyPointer.matches());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.getMatchingProperty());
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.matchProperty("test"));
        assertNull(emptyPointer.matchElement(0));
    }

    @Test
    public void testNonEmptyPointer() {
        assertFalse(nonEmptyPointer.matches());
        assertTrue(nonEmptyPointer.mayMatchProperty());
        assertFalse(nonEmptyPointer.mayMatchElement());
        assertEquals("segment", nonEmptyPointer.getMatchingProperty());
        assertEquals(-1, nonEmptyPointer.getMatchingIndex());
        assertNull(nonEmptyPointer.matchProperty("non-matching"));
        assertNull(nonEmptyPointer.matchElement(0));
    }

    @Test
    public void testEquals() {
        JsonPointer emptyPointer2 = new JsonPointer();
        JsonPointer nonEmptyPointer2 = new JsonPointer("test", "segment", null);

        assertEquals(emptyPointer, emptyPointer2);
        assertNotEquals(emptyPointer, nonEmptyPointer);
        assertEquals(nonEmptyPointer, nonEmptyPointer2);
    }

    @Test
    public void testToString() {
        assertEquals("", emptyPointer.toString());
        assertEquals("test", nonEmptyPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer emptyPointer2 = new JsonPointer();
        JsonPointer nonEmptyPointer2 = new JsonPointer("test", "segment", null);

        assertEquals(emptyPointer.hashCode(), emptyPointer2.hashCode());
        assertNotEquals(emptyPointer.hashCode(), nonEmptyPointer.hashCode());
        assertEquals(nonEmptyPointer.hashCode(), nonEmptyPointer2.hashCode());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches_WhenEmptyInstance_ExpectTrue() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
    }

    @Test
    public void testMatches_WhenNonEmptyInstance_ExpectFalse() {
        JsonPointer pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty_WhenMatchingPropertySet_ExpectMatchingProperty() {
        JsonPointer pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertEquals("segment", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex_WhenMatchingIndexSet_ExpectMatchingIndex() {
        JsonPointer pointer = new JsonPointer("test", "segment", JsonPointer.EMPTY);
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty_WhenMatchingProperty_ExpectNextSegment() {
        JsonPointer nextSegment = new JsonPointer("test", "nextSegment", JsonPointer.EMPTY);
        JsonPointer pointer = new JsonPointer("test", "segment", nextSegment);
        assertEquals(nextSegment, pointer.matchProperty("nextSegment"));
    }

    @Test
    public void testMatchElement_WhenMatchingIndex_ExpectNextSegment() {
        JsonPointer nextSegment = new JsonPointer("test", "nextSegment", JsonPointer.EMPTY);
        JsonPointer pointer = new JsonPointer("test", "segment", nextSegment);
        assertEquals(nextSegment, pointer.matchElement(-1));
    }

    @Test
    public void testTail_WhenNonEmptyInstance_ExpectNextSegment() {
        JsonPointer nextSegment = new JsonPointer("test", "nextSegment", JsonPointer.EMPTY);
        JsonPointer pointer = new JsonPointer("test", "segment", nextSegment);
        assertEquals(nextSegment, pointer.tail());
    }

    @Test
    public void testCompile_WhenEmptyString_ExpectEmptyInstance() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_WhenInvalidInput_ExpectIllegalArgumentException() {
        JsonPointer.compile("test");
    }

    @Test
    public void testValueOf_WhenCalled_ExpectSameAsCompile() {
        assertEquals(JsonPointer.compile("test"), JsonPointer.valueOf("test"));
    }

    @Test
    public void testEquals_WhenEqualPointers_ExpectTrue() {
        JsonPointer pointer1 = JsonPointer.compile("/test");
        JsonPointer pointer2 = JsonPointer.compile("/test");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testEquals_WhenDifferentPointers_ExpectFalse() {
        JsonPointer pointer1 = JsonPointer.compile("/test");
        JsonPointer pointer2 = JsonPointer.compile("/different");
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void testToString_WhenCalled_ExpectStringRepresentation() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("/test", pointer.toString());
    }

    @Test
    public void testHashCode_WhenCalled_ExpectHashCode() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("/test".hashCode(), pointer.hashCode());
    }
}
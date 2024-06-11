import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void matches_shouldReturnTrueForEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
    }
    
    @Test
    public void getMatchingProperty_shouldReturnEmptyStringForEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.getMatchingProperty());
    }
    
    @Test
    public void getMatchingIndex_shouldReturnNegativeOneForEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void mayMatchProperty_shouldReturnTrueIfMatchingPropertyNameIsNotEmpty() {
        JsonPointer pointer = new JsonPointer("/property", "property", null);
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void mayMatchElement_shouldReturnTrueIfMatchingElementIndexIsNotNegative() {
        JsonPointer pointer = new JsonPointer("/0", "0", null);
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void matchProperty_shouldReturnNextSegmentIfMatchingPropertyNameEqualsGivenProperty() {
        JsonPointer next = new JsonPointer("", "", null);
        JsonPointer pointer = new JsonPointer("", "property", next);
        assertEquals(next, pointer.matchProperty("property"));
    }
    
    @Test
    public void matchProperty_shouldReturnNullIfNextSegmentIsNull() {
        JsonPointer pointer = new JsonPointer("", "property", null);
        assertNull(pointer.matchProperty("property"));
    }
    
    @Test
    public void matchProperty_shouldReturnNullIfMatchingPropertyNameDoesNotEqualGivenProperty() {
        JsonPointer next = new JsonPointer("", "", null);
        JsonPointer pointer = new JsonPointer("", "property", next);
        assertNull(pointer.matchProperty("otherProperty"));
    }
    
    @Test
    public void matchElement_shouldReturnNextSegmentIfMatchingElementIndexEqualsGivenIndex() {
        JsonPointer next = new JsonPointer("", "", null);
        JsonPointer pointer = new JsonPointer("", "0", next);
        assertEquals(next, pointer.matchElement(0));
    }
    
    @Test
    public void matchElement_shouldReturnNullIfNextSegmentIsNull() {
        JsonPointer pointer = new JsonPointer("", "0", null);
        assertNull(pointer.matchElement(0));
    }
    
    @Test
    public void matchElement_shouldReturnNullIfMatchingElementIndexDoesNotEqualGivenIndex() {
        JsonPointer next = new JsonPointer("", "", null);
        JsonPointer pointer = new JsonPointer("", "0", next);
        assertNull(pointer.matchElement(1));
    }
    
    @Test
    public void tail_shouldReturnNextSegment() {
        JsonPointer next = new JsonPointer("", "", null);
        JsonPointer pointer = new JsonPointer("", "property", next);
        assertEquals(next, pointer.tail());
    }
    
    @Test
    public void toString_shouldReturnAsString() {
        JsonPointer pointer = new JsonPointer("/property", "property", null);
        assertEquals("/property", pointer.toString());
    }
    
    @Test
    public void hashCode_shouldBeEqualForPointersWithEqualAsStrings() {
        JsonPointer pointer1 = new JsonPointer("/property", "property", null);
        JsonPointer pointer2 = new JsonPointer("/property", "property", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void equals_shouldReturnTrueForPointersWithEqualAsStrings() {
        JsonPointer pointer1 = new JsonPointer("/property", "property", null);
        JsonPointer pointer2 = new JsonPointer("/property", "property", null);
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void equals_shouldReturnFalseForIncompatibleClasses() {
        JsonPointer pointer = new JsonPointer("/property", "property", null);
        assertFalse(pointer.equals("property"));
    }
}


package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    // equal method test cases
    @Test
    public void testEqual_WhenObjectsAreEqual_ReturnsTrue() {
        assertTrue(Objects.equal("hello", "hello"));
    }

    @Test
    public void testEqual_WhenObjectsAreNotEqual_ReturnsFalse() {
        assertFalse(Objects.equal("hello", "world"));
    }

    @Test
    public void testEqual_WhenOneObjectIsNull_ReturnsFalse() {
        assertFalse(Objects.equal("hello", null));
    }

    @Test
    public void testEqual_WhenBothObjectsAreNull_ReturnsTrue() {
        assertTrue(Objects.equal(null, null));
    }

    // hashCode method test cases
    @Test
    public void testHashCode_WhenObjectsAreEqual_HashCodeIsSame() {
        int hashCode1 = Objects.hashCode("hello");
        int hashCode2 = Objects.hashCode("hello");
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_WhenObjectsAreNotEqual_HashCodeIsDifferent() {
        int hashCode1 = Objects.hashCode("hello");
        int hashCode2 = Objects.hashCode("world");
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_WhenObjectIsNull_HashCodeIsZero() {
        int hashCode = Objects.hashCode(null);
        assertEquals(0, hashCode);
    }

    @Test
    public void testHashCode_WhenMultipleObjects_HashCodeComputedCorrectly() {
        int hashCode1 = Objects.hashCode("hello", "world");
        int hashCode2 = Objects.hashCode("hello", "world");
        assertEquals(hashCode1, hashCode2);
    }

    // toStringHelper method test cases
    @Test
    public void testToStringHelper_ClassNameWithNullClass_ReturnsClassName() {
        Objects.ToStringHelper helper = Objects.toStringHelper((Object) null);
        assertEquals("null", helper.toString());
    }

    @Test
    public void testToStringHelper_ClassNameWithNonNullClass_ReturnsClassName() {
        Objects.ToStringHelper helper = Objects.toStringHelper(String.class);
        assertEquals("String", helper.toString());
    }

    @Test
    public void testToStringHelper_CustomClassName_ReturnsCustomClassName() {
        Objects.ToStringHelper helper = Objects.toStringHelper("CustomClass");
        assertEquals("CustomClass", helper.toString());
    }

    // firstNonNull method test cases
    @Test
    public void testFirstNonNull_WhenBothObjectsAreNull_ReturnsNull() {
        assertNull(Objects.firstNonNull(null, null));
    }

    @Test
    public void testFirstNonNull_WhenFirstObjectIsNull_ReturnsSecondObject() {
        assertEquals("hello", Objects.firstNonNull(null, "hello"));
    }

    @Test
    public void testFirstNonNull_WhenSecondObjectIsNull_ReturnsFirstObject() {
        assertEquals("hello", Objects.firstNonNull("hello", null));
    }

    @Test
    public void testFirstNonNull_WhenBothObjectsAreNonNull_ReturnsFirstObject() {
        assertEquals("hello", Objects.firstNonNull("hello", "world"));
    }

}
import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_nullObjects_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_oneNullObject_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_bothNonNullObjects_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object(), new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_bothNonNullEqualObjects_ReturnsTrue() {
        boolean result = Equality.areEqual("test", "test");
        assertTrue(result);
    }

    @Test
    public void testAreEqual_bothNonNullDifferentObjects_ReturnsFalse() {
        boolean result = Equality.areEqual("test1", "test2");
        assertFalse(result);
    }

    @Test
    public void testAreEqual_bothObjectsArrays_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3});
        assertFalse(result);
    }

    @Test
    public void testAreEqual_bothObjectsArraysEqualElements_ReturnsTrue() {
        boolean result = Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3});
        assertTrue(result);
    }

    @Test
    public void testAreEqual_bothObjectsArraysDifferentElements_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6});
        assertFalse(result);
    }

    @Test
    public void testAreEqual_oneObjectArrayOneNonArray_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object[]{1, 2, 3}, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_oneObjectArrayOneNullObject_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object[]{1, 2, 3}, null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_nullObjectOneNonArray_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_nullObjectOneObjectArray_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object[]{1, 2, 3});
        assertFalse(result);
    }

    @Test
    public void testAreArrayLengthsEqual_equalArrayLengths_ReturnsTrue() {
        boolean result = Equality.areArrayLengthsEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6});
        assertTrue(result);
    }

    @Test
    public void testAreArrayLengthsEqual_differentArrayLengths_ReturnsFalse() {
        boolean result = Equality.areArrayLengthsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2});
        assertFalse(result);
    }

    @Test
    public void testAreArrayElementsEqual_equalArrayElements_ReturnsTrue() {
        boolean result = Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3});
        assertTrue(result);
    }

    @Test
    public void testAreArrayElementsEqual_differentArrayElements_ReturnsFalse() {
        boolean result = Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{4, 5, 6});
        assertFalse(result);
    }

    @Test
    public void testIsArray_arrayObject_ReturnsTrue() {
        boolean result = Equality.isArray(new Object[]{});
        assertTrue(result);
    }

    @Test
    public void testIsArray_nonArrayObject_ReturnsFalse() {
        boolean result = Equality.isArray(new Object());
        assertFalse(result);
    }
}
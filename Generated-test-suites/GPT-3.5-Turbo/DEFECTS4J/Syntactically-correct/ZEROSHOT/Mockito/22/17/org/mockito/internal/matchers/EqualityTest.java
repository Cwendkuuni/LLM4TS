import org.junit.Test;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullBoth_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_NullFirst_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_NullSecond_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object(), null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothNotNullEqual_ReturnsTrue() {
        boolean result = Equality.areEqual("test", "test");
        assertTrue(result);
    }

    @Test
    public void testAreEqual_BothNotNullNotEqual_ReturnsFalse() {
        boolean result = Equality.areEqual("test1", "test2");
        assertFalse(result);
    }

    @Test
    public void testAreEqual_FirstArrayNull_ReturnsFalse() {
        boolean result = Equality.areEqual(null, new Object[5]);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_SecondArrayNull_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object[5], null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_BothArraysNull_ReturnsTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_ArraysEqual_ReturnsTrue() {
        boolean result = Equality.areEqual(new Object[] {1, 2, 3}, new Object[] {1, 2, 3});
        assertTrue(result);
    }

    @Test
    public void testAreEqual_ArraysNotEqual_ReturnsFalse() {
        boolean result = Equality.areEqual(new Object[] {1, 2, 3}, new Object[] {4, 5, 6});
        assertFalse(result);
    }

}
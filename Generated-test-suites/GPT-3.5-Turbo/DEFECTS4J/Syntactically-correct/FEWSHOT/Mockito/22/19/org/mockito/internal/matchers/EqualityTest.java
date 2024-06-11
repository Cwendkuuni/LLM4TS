import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, "foo"));
        assertFalse(Equality.areEqual("foo", null));
        assertTrue(Equality.areEqual("foo", "foo"));
        assertFalse(Equality.areEqual("foo", "bar"));
        assertFalse(Equality.areEqual(1, "1"));
        assertFalse(Equality.areEqual("1", 1));
        assertTrue(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[]{1}, new int[]{1}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1}, new int[]{2}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2}));
        assertFalse(Equality.areArrayElementsEqual(new Object[]{1, 2, 3}, new Object[]{1, 2, 4}));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray(1));
        assertFalse(Equality.isArray("foo"));
        assertFalse(Equality.isArray(null));
    }
}
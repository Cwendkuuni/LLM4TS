import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertTrue(Equality.areEqual(new Object(), new Object()));

        assertTrue(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, new int[]{1}));
        assertFalse(Equality.areEqual(new int[]{1, 2, 3}, null));
        assertFalse(Equality.areEqual(null, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, null));
        assertFalse(Equality.areArraysEqual(null, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{1}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, null));
        assertFalse(Equality.areArrayLengthsEqual(null, new int[]{1, 2, 3}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, null));
        assertFalse(Equality.areArrayElementsEqual(null, new int[]{1, 2, 3}));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray(new Object()));
    }
}
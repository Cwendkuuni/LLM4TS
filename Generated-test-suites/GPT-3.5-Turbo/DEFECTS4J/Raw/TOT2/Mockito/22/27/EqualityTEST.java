import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(1, 1));
        assertFalse(Equality.areEqual(1, 2));
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, 1));
    }

    @Test
    public void testAreArraysEqual() {
        assertTrue(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArraysEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        assertTrue(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
        assertFalse(Equality.areArrayLengthsEqual(new int[]{1, 2, 3}, new int[]{4, 5}));
    }

    @Test
    public void testAreArrayElementsEqual() {
        assertTrue(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
        assertFalse(Equality.areArrayElementsEqual(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[]{1, 2, 3}));
        assertFalse(Equality.isArray(1));
    }

}
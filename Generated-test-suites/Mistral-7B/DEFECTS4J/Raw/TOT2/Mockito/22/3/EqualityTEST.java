import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

    @Test
    public void testAreEqualSameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqualNullObjects() {
        Object obj1 = null;
        Object obj2 = null;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqualDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    // Add test cases for areArraysEqual, areArrayLengthsEqual, areArrayElementsEqual and isArray methods
}
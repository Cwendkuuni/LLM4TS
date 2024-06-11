
import org.junit.Test;

import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_BothNull_ReturnsTrue() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_OneNullOneNotNull_ReturnsFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_NotNullObjects_ReturnsTrue() {
        Object object1 = new Object();
        Object object2 = object1;
        assertTrue(Equality.areEqual(object1, object2));
    }

    @Test
    public void testAreEqual_NotEqualObjects_ReturnsFalse() {
        Object object1 = new Object();
        Object object2 = new Object();
        assertFalse(Equality.areEqual(object1, object2));
    }

    @Test
    public void testAreEqual_ArraysEqual_ReturnsTrue() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArraysLengthNotEqual_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArraysElementNotEqual_ReturnsFalse() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 4};
        assertFalse(Equality.areEqual(array1, array2));
    }
}

import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_NullAndNull_ReturnTrue() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_NullAndObject_ReturnFalse() {
        boolean result = Equality.areEqual(null, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ObjectAndNull_ReturnFalse() {
        boolean result = Equality.areEqual(new Object(), null);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArrayAndArray_ReturnTrue() {
        Object[] array1 = new Object[] { 1, 2, 3 };
        Object[] array2 = new Object[] { 1, 2, 3 };
        boolean result = Equality.areEqual(array1, array2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_ArrayAndDifferentArray_ReturnFalse() {
        Object[] array1 = new Object[] { 1, 2, 3 };
        Object[] array2 = new Object[] { 4, 5, 6 };
        boolean result = Equality.areEqual(array1, array2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArrayAndObject_ReturnFalse() {
        Object[] array = new Object[] { 1, 2, 3 };
        boolean result = Equality.areEqual(array, new Object());
        assertFalse(result);
    }

    @Test
    public void testAreEqual_ObjectAndArray_ReturnFalse() {
        Object[] array = new Object[] { 1, 2, 3 };
        boolean result = Equality.areEqual(new Object(), array);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_SameObject_ReturnTrue() {
        Object object = new Object();
        boolean result = Equality.areEqual(object, object);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_DifferentObjects_ReturnFalse() {
        boolean result = Equality.areEqual(new Object(), new Object());
        assertFalse(result);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullObjects() {
        Object obj1 = null;
        Object obj2 = null;

        boolean result = Equality.areEqual(obj1, obj2);

        assertTrue(result);
    }

    @Test
    public void testAreEqual_ObjectAndNull() {
        Object obj1 = new Object();
        Object obj2 = null;

        boolean result = Equality.areEqual(obj1, obj2);

        assertFalse(result);
    }

    @Test
    public void testAreEqual_NullAndObject() {
        Object obj1 = null;
        Object obj2 = new Object();

        boolean result = Equality.areEqual(obj1, obj2);

        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        boolean result = Equality.areEqual(arr1, arr2);

        assertTrue(result);
    }

    @Test
    public void testAreEqual_ArraysNotEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};

        boolean result = Equality.areEqual(arr1, arr2);

        assertFalse(result);
    }

    @Test
    public void testAreEqual_ArrayAndObject() {
        int[] arr = {1, 2, 3};
        Object obj = new Object();

        boolean result = Equality.areEqual(arr, obj);

        assertFalse(result);
    }

    @Test
    public void testAreEqual_ObjectsEqual() {
        String str1 = "hello";
        String str2 = "hello";

        boolean result = Equality.areEqual(str1, str2);

        assertTrue(result);
    }

    @Test
    public void testAreEqual_ObjectsNotEqual() {
        String str1 = "hello";
        String str2 = "world";

        boolean result = Equality.areEqual(str1, str2);

        assertFalse(result);
    }

    @Test
    public void testAreEqual_ObjectAndArray() {
        String str = "hello";
        int[] arr = {1, 2, 3};

        boolean result = Equality.areEqual(str, arr);

        assertFalse(result);
    }

    @Test
    public void testAreEqual_ObjectAndObject() {
        Integer int1 = new Integer(5);
        Integer int2 = new Integer(5);

        boolean result = Equality.areEqual(int1, int2);

        assertTrue(result);
    }
}
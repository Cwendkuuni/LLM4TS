import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_NullInput1() {
        assertFalse(Equality.areEqual(null, new Object()));
    }

    @Test
    public void testAreEqual_NullInput2() {
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_EqualObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_UnequalObjects() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public void testAreEqual_EqualArrayObjects() {
        Integer[] array1 = {1, 2, 3};
        Integer[] array2 = {1, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_UnequalArrayObjects() {
        Integer[] array1 = {1, 2, 3};
        Integer[] array2 = {1, 2, 4};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArrayWithNullElement() {
        Integer[] array1 = {null, 2, 3};
        Integer[] array2 = {null, 2, 3};
        assertTrue(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArrayWithDifferentLengths() {
        Integer[] array1 = {1, 2, 3};
        Integer[] array2 = {1, 2};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArrayWithDifferentElementTypes() {
        Integer[] array1 = {1, 2, 3};
        Double[] array2 = {1.0, 2.0, 3.0};
        assertFalse(Equality.areEqual(array1, array2));
    }

    @Test
    public void testAreEqual_ArraysWithDifferentElementOrder() {
        Integer[] array1 = {1, 2, 3};
        Integer[] array2 = {3, 2, 1};
        assertFalse(Equality.areEqual(array1, array2));
    }
}
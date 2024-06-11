import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(Equality.areEqual(obj1, obj2)); //default case

        Object obj3 = new Object();
        assertFalse(Equality.areEqual(obj1, obj3)); //different objects

        Object obj4 = obj1;
        assertTrue(Equality.areEqual(obj1, obj4)); //same object
    }

    @Test
    public void testAreEqualWithNulls() {
        assertFalse(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqualWithArrays() {
        Object[] arr1 = new Object[5];
        Object[] arr2 = new Object[5];
        Object[] arr3 = new Object[3];

        for (int i = 0; i < arr1.length; i++) arr1[i] = i;
        for (int i = 0; i < arr2.length; i++) arr2[i] = i;

        assertTrue(Equality.areEqual(arr1, arr2));

        Object[] arr4 = new Object[5];
        Object[] arr5 = new Object[3];
        assertFalse(Equality.areEqual(arr1, arr5));
    }

    @Test
    public void testAreEqualWithDifferentTypes() {
        Object obj1 = new Object();
        String str1 = new String();

        assertFalse(Equality.areEqual(obj1, str1));
    }

    //... Add other test cases for edge cases and exceptions

    @Test
    public void testAreArraysEqual() {
        Object[] arr1 = new Object[]{1, 2, 3};
        Object[] arr2 = new Object[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(arr1, arr2));

        Object[] arr3 = new Object[]{1, 2, 3};
        Object[] arr4 = new Object[]{1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr4));
    }

    //... Add other test cases for areArraysEqual() method

    @Test
    public void testIsArray() {
        assertFalse(Equality.isArray(new Object()));
        assertTrue(Equality.isArray(new Object[0]));
    }

    //... Add other test cases for isArray() method

    @Test(expected = IllegalArgumentException.class)
    public void testAreEqualWithIllegalArgumentException() {
        Equality.areEqual(null, null); //should throw IllegalArgumentException
    }

    //... Add other test cases for exceptions
}
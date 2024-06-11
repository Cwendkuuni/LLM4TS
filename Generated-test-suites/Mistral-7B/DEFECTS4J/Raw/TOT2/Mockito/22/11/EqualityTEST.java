import org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public static void testAreEqualSameObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public static void testAreEqualNullObjects() {
        Object obj1 = null;
        Object obj2 = null;
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    @Test
    public static void testAreEqualDifferentObjects() {
        Object obj1 = new Object();
        Object obj2 = new String("test");
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public static void testAreArraysEqualSameArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1.clone();
        assertTrue(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public static void testAreArraysEqualDifferentArraysSameLength() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public static void testAreArraysEqualDifferentArraysDifferentLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public static void testAreArraysEqualDifferentTypes() {
        Object[] arr1 = new Object[] {1, new String("test")};
        Object[] arr2 = new Object[] {1, new Integer(1)};
        assertFalse(Equality.areArraysEqual(arr1, arr2));
    }

    @Test
    public static void testIsArrayTrue() {
        int[] arr = new int[5];
        assertTrue(Equality.isArray(arr));
    }

    @Test
    public static void testIsArrayFalse() {
        String str = "test";
        assertFalse(Equality.isArray(str));
    }

    @Test(expected = AssertionError.class)
    public static void testAreEqualCustomObjectClass() {
        CustomObject obj1 = new CustomObject();
        CustomObject obj2 = new CustomObject();
        obj1.setProperty("test1");
        obj2.setProperty("test2");
        assertFalse(Equality.areEqual(obj1, obj2));
    }

    @Test
    public static void testAreEqualCustomObjectClassEqual() {
        CustomObject obj1 = new CustomObject();
        obj1.setProperty("test1");
        CustomObject obj2 = new CustomObject();
        obj2.setProperty("test1");
        assertTrue(Equality.areEqual(obj1, obj2));
    }

    public static class CustomObject {
        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }
}
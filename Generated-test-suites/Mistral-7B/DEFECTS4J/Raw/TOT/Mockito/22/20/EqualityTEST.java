import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {
    @Test
    public void testAreEqual() {
        // Test with null objects
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));

        // Test with primitive types
        int a = 5;
        int b = 5;
        assertTrue(Equality.areEqual(a, b));

        // Test with arrays
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));

        // Test with arrays of different lengths
        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2};
        assertFalse(Equality.areEqual(arr3, arr4));

        // Test with arrays of different types
        int[] intArr = {1, 2, 3};
        String[] strArr = {"1", "2", "3"};
        assertFalse(Equality.areEqual(intArr, strArr));

        // Test with non-equal objects
        String str1 = new String("test");
        String str2 = new String("anotherTest");
        assertFalse(Equality.areEqual(str1, str2));
    }
}
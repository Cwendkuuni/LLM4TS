import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Equality;

public class EqualityTest {

    @Test
    public void testAreEqual_forNullInputs() {
        boolean result = Equality.areEqual(null, null);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_forOneNullInput() {
        boolean result = Equality.areEqual(null, "test");
        assertFalse(result);
    }

    @Test
    public void testAreEqual_forDifferentArrayLengths() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_forDifferentArrayElements() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {3, 2, 1};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_forSameArray() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        boolean result = Equality.areEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_forEqualNonArrayObjects() {
        String str1 = "test";
        String str2 = new String("test");
        boolean result = Equality.areEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_forDifferentNonArrayObjects() {
        String str1 = "test";
        String str2 = "different";
        boolean result = Equality.areEqual(str1, str2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_forEqualNonArrayObjectsWithNull() {
        String str1 = null;
        String str2 = null;
        boolean result = Equality.areEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_forDifferentNonArrayObjectsWithNull() {
        String str1 = null;
        String str2 = "test";
        boolean result = Equality.areEqual(str1, str2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_forEqualNonArrayObjectsWithEqualArrays() {
        String[] arr1 = {"a", "b", "c"};
        String[] arr2 = {"a", "b", "c"};
        boolean result = Equality.areEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_forEqualNonArrayObjectsWithDifferentArrays() {
        String[] arr1 = {"a", "b", "c"};
        String[] arr2 = {"c", "b", "a"};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }

    @Test
    public void testAreEqual_forEqualNonArrayObjectsWithNullArrays() {
        String[] arr1 = null;
        String[] arr2 = null;
        boolean result = Equality.areEqual(arr1, arr2);
        assertTrue(result);
    }

    @Test
    public void testAreEqual_forDifferentNonArrayObjectsWithNullArrays() {
        String[] arr1 = null;
        String[] arr2 = {"a", "b", "c"};
        boolean result = Equality.areEqual(arr1, arr2);
        assertFalse(result);
    }
}
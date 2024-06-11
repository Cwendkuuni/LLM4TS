import org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        String str1 = "test";
        String str2 = "test";
        Object obj1 = new Object() {};
        Object obj2 = new Object() {};
        int[] intArr1 = {1, 2, 3};
        int[] intArr2 = {1, 2, 3};
        Object[] arr1 = {"test", 1, new Object() {}};
        Object[] arr2 = {"test", 1, new Object() {}};

        // Test with two equal objects
        assertTrue(Equality.areEqual(str1, str2));
        assertTrue(Equality.areEqual(obj1, obj2));
        assertTrue(Equality.areEqual(intArr1, intArr2));
        assertTrue(Equality.areEqual(arr1, arr2));

        // Test with two non-equal objects
        assertFalse(Equality.areEqual(str1, "not test"));
        assertFalse(Equality.areEqual(obj1, new Object() {}));
        assertFalse(Equality.areEqual(intArr1, intArr2.toString().split(", ")));
        assertFalse(Equality.areEqual(arr1, new Object[]{"test1", 1, new Object() {}}));
    }
}
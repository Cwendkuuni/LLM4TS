import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_BothNull() {
        assertTrue(Equality.areEqual(null, null));
    }

    @Test
    public void testAreEqual_BothNotNull() {
        assertTrue(Equality.areEqual("test", "test"));
    }

    @Test
    public void testAreEqual_NullAndNotNull() {
        assertFalse(Equality.areEqual(null, "test"));
    }

    @Test
    public void testAreEqual_ArraysEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_ArraysNotEqual() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_ExceptionHandling() {
        try {
            Equality.areEqual(new Object(), new Object());
            fail("Expected an exception");
        } catch (Exception e) {
            // Pass
        }
    }
}
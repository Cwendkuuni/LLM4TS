import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_NullInputs_ReturnTrue() {
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreEqual_NullInput_ReturnFalse() {
        assertFalse(Equality.areEqual(null, new Object()));
    }
    
    @Test
    public void testAreEqual_NonNullEqualInputs_ReturnTrue() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }
    
    @Test
    public void testAreEqual_NonNullNotEqualInputs_ReturnFalse() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }
    
    @Test
    public void testAreEqual_ArraysOfDifferentLengths_ReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_ArraysWithDifferentElements_ReturnFalse() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_ArraysWithEqualElements_ReturnTrue() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

}
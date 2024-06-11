
import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {
    
    @Test
    public void testAreEqual_sameObjects() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }
    
    @Test
    public void testAreEqual_bothNull() {
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreEqual_firstNullSecondNotNull() {
        assertFalse(Equality.areEqual(null, new Object()));
    }
    
    @Test
    public void testAreEqual_firstNotNullSecondNull() {
        assertFalse(Equality.areEqual(new Object(), null));
    }
    
    @Test
    public void testAreEqual_notArray() {
        assertTrue(Equality.areEqual("hello", "hello"));
    }
    
    @Test
    public void testAreEqual_notArrayDifferentValues() {
        assertFalse(Equality.areEqual("hello", "world"));
    }
    
    @Test
    public void testAreEqual_arrayLengthsEqual() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_arrayLengthsNotEqual() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_arrayElementsEqual() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_arrayElementsNotEqual() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
}

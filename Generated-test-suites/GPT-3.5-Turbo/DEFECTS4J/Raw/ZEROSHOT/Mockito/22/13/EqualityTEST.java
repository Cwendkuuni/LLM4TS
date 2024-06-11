import org.junit.Test;
import static org.junit.Assert.*;

public class EqualityTest {

    @Test
    public void testAreEqual_sameObject() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }
    
    @Test
    public void testAreEqual_nullObjects() {
        assertTrue(Equality.areEqual(null, null));
    }
    
    @Test
    public void testAreEqual_nullAndNotNull() {
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }
    
    @Test
    public void testAreEqual_equalArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertTrue(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_unequalArrays() {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_equalObjects() {
        String str1 = "hello";
        String str2 = new String("hello");
        assertTrue(Equality.areEqual(str1, str2));
    }
    
    @Test
    public void testAreEqual_unequalObjects() {
        String str1 = "hello";
        String str2 = "world";
        assertFalse(Equality.areEqual(str1, str2));
    }
    
    @Test
    public void testAreEqual_equalNullArrays() {
        int[] arr1 = null;
        int[] arr2 = null;
        assertTrue(Equality.areEqual(arr1, arr2));
    }
    
    @Test
    public void testAreEqual_unequalNullArrays() {
        int[] arr1 = null;
        int[] arr2 = {1, 2, 3};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

}
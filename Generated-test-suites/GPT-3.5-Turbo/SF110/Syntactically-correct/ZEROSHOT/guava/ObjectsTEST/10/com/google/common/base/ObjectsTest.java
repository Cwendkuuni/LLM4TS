
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));
        
        String str1 = "hello";
        String str2 = "hello";
        assertTrue(Objects.equal(str1, str2));
        
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        assertFalse(Objects.equal(arr1, arr2));
        assertTrue(Objects.equal(arr1, arr1));
    }
    
    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(obj1.hashCode(), Objects.hashCode(obj1));
        assertEquals(obj2.hashCode(), Objects.hashCode(obj2));
        
        int[] arr = {1, 2, 3};
        assertEquals(Arrays.hashCode(arr), Objects.hashCode(arr));
        
        assertNull(Objects.hashCode((Object[]) null));
        assertEquals(0, Objects.hashCode());
    }
    
    @Test
    public void testToStringHelper() {
        Object obj = new Object();
        
        Objects.ToStringHelper helper = Objects.toStringHelper(obj);
        assertNotNull(helper);
        
        assertEquals(obj.getClass().getSimpleName(), helper.toString());
        
        helper = Objects.toStringHelper(obj.getClass());
        assertNotNull(helper);
        
        assertEquals(obj.getClass().getSimpleName(), helper.toString());
        
        String className = "Object";
        helper = Objects.toStringHelper(className);
        assertNotNull(helper);
        
        assertEquals(className, helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        
        assertEquals(obj1, Objects.firstNonNull(obj1, obj2));
        assertEquals(obj2, Objects.firstNonNull(null, obj2));
        assertNull(Objects.firstNonNull(null, null));
    }
}

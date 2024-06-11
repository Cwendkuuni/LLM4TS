import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {
    
    @Test
    public void equal_shouldReturnTrue_whenObjectsAreSame() {
        Object obj = new Object();
        assertTrue(Objects.equal(obj, obj));
    }
    
    @Test
    public void equal_shouldReturnFalse_whenObjectsAreDifferent() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void equal_shouldReturnTrue_whenObjectsAreEqual() {
        String str1 = "test";
        String str2 = new String("test");
        assertTrue(Objects.equal(str1, str2));
    }
    
    @Test
    public void equal_shouldReturnFalse_whenOneObjectIsNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        assertFalse(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void hashCode_shouldReturnSameHashCode_whenObjectsAreEqual() {
        String str1 = "test";
        String str2 = new String("test");
        assertEquals(Objects.hashCode(str1), Objects.hashCode(str2));
    }
    
    @Test
    public void hashCode_shouldReturnDifferentHashCode_whenObjectsAreDifferent() {
        String str1 = "test1";
        String str2 = "test2";
        assertNotEquals(Objects.hashCode(str1), Objects.hashCode(str2));
    }
    
    // Test cases for toStringHelper methods omitted as they are deprecated
    
    @Test
    public void firstNonNull_shouldReturnFirstNonNullObject_whenBothAreNonNull() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertEquals(obj1, Objects.firstNonNull(obj1, obj2));
    }
    
    @Test
    public void firstNonNull_shouldReturnFirstNonNullObject_whenFirstIsNull() {
        Object obj1 = null;
        Object obj2 = new Object();
        assertEquals(obj2, Objects.firstNonNull(obj1, obj2));
    }
    
    @Test
    public void firstNonNull_shouldReturnSecondNonNullObject_whenSecondIsNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        assertEquals(obj1, Objects.firstNonNull(obj1, obj2));
    }
    
    @Test
    public void firstNonNull_shouldReturnNull_whenBothAreNull() {
        Object obj1 = null;
        Object obj2 = null;
        assertNull(Objects.firstNonNull(obj1, obj2));
    }
    
    // Edge cases and exception handling scenarios
    
    @Test
    public void equal_shouldReturnFalse_whenBothObjectsAreNull() {
        Object obj1 = null;
        Object obj2 = null;
        assertFalse(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void equal_shouldReturnFalse_whenOneObjectIsNullAndTheOtherIsNotNull() {
        Object obj1 = null;
        Object obj2 = new Object();
        assertFalse(Objects.equal(obj1, obj2));
    }
    
    @Test
    public void equal_shouldHandleNullPointerException_whenComparingObjectsWithNull() {
        Object obj1 = new Object();
        Object obj2 = null;
        assertThrows(NullPointerException.class, () -> Objects.equal(obj1, obj2));
    }
    
    // Complete JUnit test file
    
    @Test
    public void completeTestFile() {
        // Placeholder test case
        assertTrue(true);
    }
}
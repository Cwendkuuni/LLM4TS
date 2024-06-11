import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Objects;

public class ObjectsTest {
    
    @Test
    public void equal_NullObjects_ReturnsTrue() {
        boolean result = Objects.equal(null, null);
        assertTrue(result);
    }
    
    @Test
    public void equal_SameObjects_ReturnsTrue() {
        Object obj = new Object();
        boolean result = Objects.equal(obj, obj);
        assertTrue(result);
    }
    
    @Test
    public void equal_FirstObjectIsNull_ReturnsFalse() {
        Object obj = new Object();
        boolean result = Objects.equal(null, obj);
        assertFalse(result);
    }
    
    @Test
    public void equal_SecondObjectIsNull_ReturnsFalse() {
        Object obj = new Object();
        boolean result = Objects.equal(obj, null);
        assertFalse(result);
    }
    
    @Test
    public void equal_DifferentObjects_ReturnsFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        boolean result = Objects.equal(obj1, obj2);
        assertFalse(result);
    }
    
    @Test
    public void hashCode_NullObjects_ReturnsZero() {
        int result = Objects.hashCode(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void hashCode_SingleObject_ReturnsHashValue() {
        Object obj = new Object();
        int expectedResult = obj.hashCode();
        int result = Objects.hashCode(obj);
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void toStringHelper_OmitNullValues_ReturnsExpectedString() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass").omitNullValues();
        helper.add("name", "John");
        helper.add("age", null);
        String expected = "TestClass{name=John}";
        assertEquals(expected, helper.toString());
    }
    
    @Test
    public void toStringHelper_AddValues_ReturnsExpectedString() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("name", "John");
        helper.add("age", 25);
        String expected = "TestClass{name=John, age=25}";
        assertEquals(expected, helper.toString());
    }
    
    @Test
    public void firstNonNull_BothObjectsNull_ReturnsNull() {
        Object result = Objects.firstNonNull(null, null);
        assertNull(result);
    }
    
    @Test
    public void firstNonNull_FirstObjectNull_ReturnsSecondObject() {
        Object second = new Object();
        Object result = Objects.firstNonNull(null, second);
        assertEquals(second, result);
    }
    
    @Test
    public void firstNonNull_SecondObjectNull_ReturnsFirstObject() {
        Object first = new Object();
        Object result = Objects.firstNonNull(first, null);
        assertEquals(first, result);
    }
    
    @Test
    public void firstNonNull_NeitherObjectNull_ReturnsFirstObject() {
        Object first = new Object();
        Object second = new Object();
        Object result = Objects.firstNonNull(first, second);
        assertEquals(first, result);
    }

}
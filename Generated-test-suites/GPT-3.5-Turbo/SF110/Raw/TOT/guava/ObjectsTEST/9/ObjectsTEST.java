
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Objects;

public class ObjectsTest {
    
    @Test
    public void testEqual_SameObject() {
        Object a = new Object();
        Object b = a;
        
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_NullObjects() {
        Object a = null;
        Object b = null;
        
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_OneNullObject() {
        Object a = null;
        Object b = new Object();
        
        assertFalse(Objects.equal(a, b));
    }
    
    @Test
    public void testEqual_DifferentObjects() {
        Object a = new Object();
        Object b = new Object();
        
        assertFalse(Objects.equal(a, b));
    }
    
    @Test
    public void testHashCode_NoObjects() {
        int expected = 0;
        
        int result = Objects.hashCode();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testHashCode_OneNullObject() {
        Object a = null;
        int expected = 0;
        
        int result = Objects.hashCode(a);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testHashCode_MultipleObjects() {
        Object a = new Object();
        Object b = new Object();
        int expected = Objects.hash(a, b);
        
        int result = Objects.hashCode(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testToStringHelper_OmitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test").omitNullValues();
        helper.add("name", "John");
        helper.add("age", null);
        helper.add("city", "New York");

        String expected = "Test{name=John, city=New York}";
        
        assertEquals(expected, helper.toString());
    }
    
    @Test
    public void testToStringHelper_NoOmitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("name", "John");
        helper.add("age", null);
        helper.add("city", "New York");
        
        String expected = "Test{name=John, age=null, city=New York}";
        
        assertEquals(expected, helper.toString());
    }
    
    @Test
    public void testFirstNonNull_BothNull() {
        Object a = null;
        Object b = null;
        Object expected = null;
        
        Object result = Objects.firstNonNull(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFirstNonNull_FirstNonNull() {
        Object a = "Hello";
        Object b = null;
        Object expected = "Hello";
        
        Object result = Objects.firstNonNull(a, b);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFirstNonNull_SecondNonNull() {
        Object a = null;
        Object b = "World";
        Object expected = "World";
        
        Object result = Objects.firstNonNull(a, b);
        
        assertEquals(expected, result);
    }
}


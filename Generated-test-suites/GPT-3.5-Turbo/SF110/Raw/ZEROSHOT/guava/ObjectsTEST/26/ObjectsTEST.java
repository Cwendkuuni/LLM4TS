import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        Object a = new Object();
        Object b = new Object();
        assertFalse(Objects.equal(a, b));
        
        a = "Hello";
        b = "Hello";
        assertTrue(Objects.equal(a, b));
    }
    
    @Test
    public void testHashCode() {
        Object[] objects = {"Hello", 123, true};
        int hashCode = Objects.hashCode(objects);
        assertEquals(Objects.hashCode("Hello", 123, true), hashCode);
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.addValue("Hello");
        helper.addValue(123);
        helper.addValue(true);
        
        String expected = "TestClass{Hello, 123, true}";
        assertEquals(expected, helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        String first = null;
        String second = "Hello";
        String result = Objects.firstNonNull(first, second);
        assertEquals("Hello", result);
        
        first = "Hello";
        second = null;
        result = Objects.firstNonNull(first, second);
        assertEquals("Hello", result);
        
        first = null;
        second = null;
        result = Objects.firstNonNull(first, second);
        assertNull(result);
    }
}
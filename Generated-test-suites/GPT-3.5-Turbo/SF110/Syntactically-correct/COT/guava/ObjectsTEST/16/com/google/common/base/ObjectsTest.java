
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "Hello"));
        assertFalse(Objects.equal("Hello", null));
        assertTrue(Objects.equal("Hello", "Hello"));
        assertFalse(Objects.equal("Hello", "World"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123, Objects.hashCode(123));
        assertEquals(321, Objects.hashCode(123, "Hello"));
        assertEquals(123, Objects.hashCode("Hello", 123));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.addValue(null);
        helper.addValue(true);
        helper.addValue('c');
        helper.addValue(1.23);
        
        String expected = "TestClass{null, true, c, 1.23}";
        assertEquals(expected, helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        Integer result = Objects.firstNonNull(null, 123);
        assertEquals(Integer.valueOf(123), result);
        
        String str = Objects.firstNonNull("Hello", null);
        assertEquals("Hello", str);
        
        Object obj = new Object();
        Object sameObj = Objects.firstNonNull(obj, null);
        assertSame(obj, sameObj);
    }
    
    @Test
    public void testToStringHelperWithOmitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.omitNullValues();
        helper.addValue(null);
        helper.addValue(true);
        helper.addValue('c');
        helper.addValue(1.23);
        
        String expected = "TestClass{true, c, 1.23}";
        assertEquals(expected, helper.toString());
    }
}

import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal("test", "Test"));
    }
    
    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        int hashCode = Objects.hashCode(obj1, obj2);
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
    }
    
    @Test
    public void testFirstNonNull() {
        Object obj = new Object();
        Object result = Objects.firstNonNull(obj, null);
        assertNotNull(result);
    }
    
    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass");
        helper.add("name", "value");
        assertEquals("TestClass{name=value}", helper.toString());
    }
    
    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("TestClass");
        helper.addValue("value");
        assertEquals("TestClass{value}", helper.toString());
    }
    
}
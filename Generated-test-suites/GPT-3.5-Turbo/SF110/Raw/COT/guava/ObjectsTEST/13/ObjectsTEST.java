import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("hello", "hello"));
        assertFalse(Objects.equal(null, "hello"));
        assertFalse(Objects.equal("hello", null));
        assertFalse(Objects.equal("hello", "world"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123, Objects.hashCode(123));
        assertEquals(456, Objects.hashCode(456));
        assertEquals(987, Objects.hashCode(987));
        assertEquals(1231, Objects.hashCode(123, 456, 987));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertNotNull(helper);
        assertEquals("Test{}", helper.toString());
        
        helper.add("name", "John");
        assertEquals("Test{name=John}", helper.toString());
        
        helper.addValue(25);
        assertEquals("Test{name=John, 25}", helper.toString());
        
        helper.add("isStudent", true);
        assertEquals("Test{name=John, 25, isStudent=true}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("hello", Objects.firstNonNull("hello", null));
        assertEquals("world", Objects.firstNonNull(null, "world"));
        assertNull(Objects.firstNonNull(null, null));
    }
}
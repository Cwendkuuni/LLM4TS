import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("hello", "hello"));
        assertFalse(Objects.equal("hello", "world"));
        assertFalse(Objects.equal(null, "hello"));
        assertFalse(Objects.equal("hello", null));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123456, Objects.hashCode(123456));
        assertEquals(-987654, Objects.hashCode(-987654));
        assertEquals(246813579, Objects.hashCode(2, 4, 6, 8, 1, 3, 5, 7, 9));
    }
    
    @Test
    public void testToStringHelper() {
        String expected = "ObjectsTest{}";
        String actual = Objects.toStringHelper(this).toString();
        assertEquals(expected, actual);
        
        expected = "ObjectsTest{test=true}";
        actual = Objects.toStringHelper(this).add("test", true).toString();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testFirstNonNull() {
        String expected = "hello";
        String actual = Objects.firstNonNull(null, "hello");
        assertEquals(expected, actual);
        
        expected = "world";
        actual = Objects.firstNonNull("world", "hello");
        assertEquals(expected, actual);
        
        expected = null;
        actual = Objects.firstNonNull(null, null);
        assertEquals(expected, actual);
    }
    
}
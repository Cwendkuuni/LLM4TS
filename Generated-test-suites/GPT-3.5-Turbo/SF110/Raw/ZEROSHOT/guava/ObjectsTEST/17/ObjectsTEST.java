import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
        assertFalse(Objects.equal("test", null));
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal("test", "Test"));
        assertTrue(Objects.equal(5, 5));
        assertTrue(Objects.equal(5.0, 5.0));
        assertFalse(Objects.equal(5, 10));
        assertFalse(Objects.equal(5.0, 10.0));
        assertFalse(Objects.equal(null, 5));
        assertFalse(Objects.equal(null, 5.0));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(0, Objects.hashCode(null));
        assertEquals(31, Objects.hashCode("test"));
        assertEquals(31 * 31, Objects.hashCode("test", "test"));
        assertEquals(31 * 31 * 31, Objects.hashCode("test", "test", "test"));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertNotNull(helper);
        
        helper = Objects.toStringHelper(this.getClass());
        assertNotNull(helper);
        
        helper = Objects.toStringHelper(this);
        assertNotNull(helper);
        
        helper.omitNullValues();
        assertNotNull(helper.toString());
        
        helper.add("name", "John");
        assertNotNull(helper.toString());
        
        helper.addValue("Doe");
        assertNotNull(helper.toString());
        
        helper.add("age", 25);
        assertNotNull(helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("test", Objects.firstNonNull("test", null));
        assertEquals("test", Objects.firstNonNull(null, "test"));
        assertEquals("test", Objects.firstNonNull("test", "Test"));
        assertEquals(5, Objects.firstNonNull(null, 5));
        assertEquals(5.0, Objects.firstNonNull(null, 5.0));
    }
}
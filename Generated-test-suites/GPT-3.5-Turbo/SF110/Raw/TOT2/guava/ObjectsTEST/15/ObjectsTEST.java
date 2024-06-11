import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal("a", "b"));
        assertTrue(Objects.equal("a", "a"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("a", "b"), Arrays.hashCode(new Object[] {"a", "b"}));
    }
    
    @Test
    public void testToStringHelper() {
        assertNotNull(Objects.toStringHelper("TestClass"));
        assertNotNull(Objects.toStringHelper(TestClass.class));
        assertNotNull(Objects.toStringHelper(new Object()));
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("a", Objects.firstNonNull("a", null));
        assertEquals("b", Objects.firstNonNull(null, "b"));
        assertNull(Objects.firstNonNull(null, null));
    }
    
    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = Objects.toStringHelper("test");
        helper.add("key1", "value1");
        helper.add("key2", 123);
        assertEquals("test{key1=value1, key2=123}", helper.toString());
    }
    
    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("test");
        helper.addValue("value1");
        helper.addValue(123);
        assertEquals("test{value1, 123}", helper.toString());
    }
    
    // Add more test cases to cover edge cases and exception handling scenarios
    
}
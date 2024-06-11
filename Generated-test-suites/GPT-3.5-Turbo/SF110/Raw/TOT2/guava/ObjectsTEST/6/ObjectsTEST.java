import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
        assertTrue(Objects.equal("test", "test"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(null, 1, "test"), Arrays.hashCode(new Object[]{null, 1, "test"}));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.addValue(1);
        helper.addValue("TestString");
        assertEquals("TestClass{1, TestString}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("test", Objects.firstNonNull(null, "test"));
    }
    
    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("value", "test");
        assertEquals("TestClass{value=test}", helper.toString());
    }
    
    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("value", null).omitNullValues();
        assertEquals("TestClass{}", helper.toString());
    }
    
    // Add more test cases for other methods as necessary
    
}
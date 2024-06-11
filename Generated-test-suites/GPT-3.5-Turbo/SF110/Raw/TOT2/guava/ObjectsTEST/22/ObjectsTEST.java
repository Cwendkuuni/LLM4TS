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
        Object[] objects = { "test1", "test2" };
        assertEquals(Objects.hashCode(objects), Arrays.hashCode(objects));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertNotNull(helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        Object result = Objects.firstNonNull(null, "default");
        assertEquals(result, "default");
    }
    
    @Test
    public void testValueHolder() {
        Objects.ToStringHelper.ValueHolder holder = new Objects.ToStringHelper.ValueHolder();
        assertNull(holder.name);
        assertNull(holder.value);
        assertNull(holder.next);
    }
    
    // Add more test cases for edge cases, exception handling, and other scenarios as necessary

}
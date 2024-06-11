import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal("test1", "test2"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(123, Objects.hashCode(123));
    }
    
    @Test
    public void testToStringHelper() {
        assertNotNull(Objects.toStringHelper("Test"));
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("Test", Objects.firstNonNull(null, "Test"));
    }
    
    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.add("name", "value");
        assertEquals("Test{name=value}", toStringHelper.toString());
    }
    
    @Test
    public void testValueHolder() {
        Objects.ToStringHelper.ValueHolder valueHolder = new Objects.ToStringHelper.ValueHolder();
        assertNull(valueHolder.name);
        assertNull(valueHolder.value);
        assertNull(valueHolder.next);
    }
}
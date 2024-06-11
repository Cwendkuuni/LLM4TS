import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.common.base.Objects.ToStringHelper;
import static org.junit.Assert.assertNull;

public class ObjectsTest {
    
    @Test
    public void testEqual() {
        assertEquals(false, Objects.equal(null, null));
        assertEquals(true, Objects.equal(null, "hello"));
        assertEquals(true, Objects.equal("hello", "hello"));
        assertEquals(false, Objects.equal("hello", "world"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(97, Objects.hashCode("a"));
        assertEquals(629, Objects.hashCode("a", "b", "c"));
    }
    
    @Test
    public void testToStringHelper() {
        ToStringHelper helper = Objects.toStringHelper("Test");
        assertEquals("Test{}", helper.toString());
        
        helper = Objects.toStringHelper(String.class);
        assertEquals("String{}", helper.toString());
        
        helper = Objects.toStringHelper(Object.class);
        assertEquals("Object{}", helper.toString());
        
        helper.add("key", "value");
        assertEquals("Object{key=value}", helper.toString());
        
        helper.addValue("hello");
        assertEquals("Object{key=value, hello}", helper.toString());
        
        helper.addValue(1);
        assertEquals("Object{key=value, hello, 1}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("hello", Objects.firstNonNull("hello", null));
        assertEquals("world", Objects.firstNonNull(null, "world"));
        assertEquals("hello", Objects.firstNonNull("hello", "world"));
    }
}
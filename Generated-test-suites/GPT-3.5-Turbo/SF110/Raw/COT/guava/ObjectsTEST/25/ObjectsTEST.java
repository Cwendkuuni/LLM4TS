
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "abc"));
        assertFalse(Objects.equal("abc", null));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(123, Objects.hashCode(123));
        assertEquals(123 ^ 456 ^ 789, Objects.hashCode(123, 456, 789));
    }
    
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = new Objects.ToStringHelper("ClassName");
        helper.add("name", "John");
        helper.add("age", 30);
        assertEquals("ClassName{name=John, age=30}", helper.toString());
    }
    
    @Test
    public void testFirstNonNull() {
        assertEquals("abc", Objects.firstNonNull(null, "abc"));
        assertEquals("def", Objects.firstNonNull("def", "abc"));
    }
    
}

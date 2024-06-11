import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(1, 2, 3), Arrays.hashCode(new Object[]{1, 2, 3}));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass")
                .add("name", "John")
                .add("age", 30);
        assertEquals("TestClass{name=John, age=30}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        Object result = Objects.firstNonNull(null, "default");
        assertEquals("default", result);
    }

    // Add test cases for other methods and edge cases here

}
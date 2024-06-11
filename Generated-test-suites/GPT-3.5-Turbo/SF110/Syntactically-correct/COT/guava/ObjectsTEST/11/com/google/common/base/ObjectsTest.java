
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "abc"));
        assertFalse(Objects.equal("abc", null));
        assertTrue(Objects.equal("abc", "abc"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(100, Objects.hashCode(100));
        assertEquals(12345, Objects.hashCode("abc", 123, true));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("name", "John");
        helper.add("age", 25);
        helper.omitNullValues();

        assertEquals("TestClass{name=John, age=25}", helper.toString());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testFirstNonNull() {
        Integer result = Objects.firstNonNull(null, 5);
        assertEquals(Integer.valueOf(5), result);

        String str = Objects.firstNonNull("Hello", "World");
        assertEquals("Hello", str);
    }
}


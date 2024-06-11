
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "Hello"));
        assertFalse(Objects.equal("Hello", null));
        assertTrue(Objects.equal("Hello", "Hello"));
        assertFalse(Objects.equal("Hello", "World"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(null), 0);
        assertEquals(Objects.hashCode("Hello"), "Hello".hashCode());
        assertEquals(Objects.hashCode("Hello", "World"), Arrays.hashCode(new Object[] { "Hello", "World" }));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("name", "John");
        helper.add("age", 25);
        assertEquals(helper.toString(), "Test{name=John, age=25}");
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals(Objects.firstNonNull(null, "Hello"), "Hello");
        assertEquals(Objects.firstNonNull("Hello", null), "Hello");
        assertEquals(Objects.firstNonNull("Hello", "World"), "Hello");
    }
}

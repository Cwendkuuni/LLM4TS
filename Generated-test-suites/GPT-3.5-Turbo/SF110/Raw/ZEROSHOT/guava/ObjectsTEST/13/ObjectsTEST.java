import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
        assertFalse(Objects.equal("test", null));
        assertTrue(Objects.equal("test", "test"));
        assertFalse(Objects.equal("test", "TEST"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(701127713, Objects.hashCode("test", "123", null));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("name", "John Doe");
        helper.add("age", 30);
        assertEquals("Test{name=John Doe, age=30}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertEquals("test", Objects.firstNonNull("test", null));
        assertEquals("test", Objects.firstNonNull(null, "test"));
        assertNull(Objects.firstNonNull(null, null));
    }
}
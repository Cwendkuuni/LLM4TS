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
        Object[] objects = {1, "test", false};
        assertEquals(Objects.hashCode(objects), Arrays.hashCode(objects));
    }

    @Test
    public void testToStringHelper() {
        Object obj = new Object();
        assertNotNull(Objects.toStringHelper(obj));
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("test", Objects.firstNonNull("test", null));
        assertEquals("test", Objects.firstNonNull(null, "test"));
    }
}
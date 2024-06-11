
import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
        assertFalse(Objects.equal("abc", null));
        assertFalse(Objects.equal(null, "abc"));
        assertFalse(Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(1));
        assertEquals(123, Objects.hashCode(1, 2, 3));
        assertEquals(-123, Objects.hashCode(-1, -2, -3));
        assertEquals(456, Objects.hashCode("abc", 123, true));
    }

    @Test
    public void testToStringHelper() {
        assertNotNull(Objects.toStringHelper(new Object()));
        assertNotNull(Objects.toStringHelper(Object.class));
        assertNotNull(Objects.toStringHelper("abc"));
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("abc", Objects.firstNonNull("abc", null));
        assertEquals("def", Objects.firstNonNull(null, "def"));
        assertEquals("abc", Objects.firstNonNull("abc", "def"));
    }
}

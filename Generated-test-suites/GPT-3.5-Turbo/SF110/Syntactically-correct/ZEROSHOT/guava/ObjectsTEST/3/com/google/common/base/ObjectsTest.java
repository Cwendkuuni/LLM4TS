import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
        assertFalse(Objects.equal(null, "abc"));
        assertFalse(Objects.equal("abc", null));
        assertFalse(Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(12345, Objects.hashCode(12345));
        assertEquals(987654321, Objects.hashCode(987654321));
        assertEquals(12345, Objects.hashCode(12345, null));
        assertEquals(12345, Objects.hashCode(null, 12345));
        assertEquals(12345987654321L, Objects.hashCode(12345, 987654321));
    }

    @Test
    @Deprecated
    public void testToStringHelperObject() {
        Objects.ToStringHelper helper = Objects.toStringHelper(new Object());
        assertNotNull(helper);
    }

    @Test
    @Deprecated
    public void testToStringHelperClass() {
        Objects.ToStringHelper helper = Objects.toStringHelper(ObjectsTest.class);
        assertNotNull(helper);
    }

    @Test
    @Deprecated
    public void testToStringHelperString() {
        Objects.ToStringHelper helper = Objects.toStringHelper("className");
        assertNotNull(helper);
    }

    @Test
    @Deprecated
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("abc", Objects.firstNonNull("abc", null));
        assertEquals("def", Objects.firstNonNull(null, "def"));
        assertEquals("abc", Objects.firstNonNull("abc", "def"));
        assertEquals(123, Objects.firstNonNull(123, null));
        assertEquals(456, Objects.firstNonNull(null, 456));
        assertEquals(123, Objects.firstNonNull(123, 456));
    }
}
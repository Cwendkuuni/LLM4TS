import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual_typical() {
        assertTrue(Objects.equal("abc", "abc"));
    }

    @Test
    public void testEqual_null() {
        assertFalse(Objects.equal(null, "abc"));
    }

    @Test
    public void testEqual_notEqual() {
        assertFalse(Objects.equal("abc", "def"));
    }

    @Test
    public void testHashCode_empty() {
        assertEquals(0, Objects.hashCode());
    }

    @Test
    public void testHashCode_typical() {
        assertEquals(31 * 31 + 32 * 32, Objects.hashCode(31, 32));
    }

    @Test
    public void testToStringHelper_typical() {
        Object obj = new Object();
        assertEquals(obj.getClass().getSimpleName() + "{}", Objects.toStringHelper(obj).toString());
    }

    @Test
    public void testToStringHelper_null() {
        assertEquals("null{}", Objects.toStringHelper(null).toString());
    }

    @Test
    public void testToStringHelper_customName() {
        assertEquals("CustomName{}", Objects.toStringHelper("CustomName").toString());
    }

    @Test
    public void testFirstNonNull_typical() {
        String result = Objects.firstNonNull("abc", "def");
        assertEquals("abc", result);
    }

    @Test
    public void testFirstNonNull_null() {
        Integer result = Objects.firstNonNull(null, 123);
        assertEquals(Integer.valueOf(123), result);
    }

    @Test
    public void testFirstNonNull_bothNull() {
        Object result = Objects.firstNonNull(null, null);
        assertNull(result);
    }

}
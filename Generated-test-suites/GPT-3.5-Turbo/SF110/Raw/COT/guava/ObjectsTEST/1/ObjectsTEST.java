
import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object a = new Object();
        Object b = new Object();
        assertTrue(Objects.equal(a, a)); // identical objects, should be equal
        assertFalse(Objects.equal(a, b)); // different objects, should not be equal
        assertTrue(Objects.equal(null, null)); // both are null, should be equal
        assertFalse(Objects.equal(a, null)); // one is null, should not be equal
        assertFalse(Objects.equal(null, b)); // one is null, should not be equal
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode()); // no objects, should return 0
        assertEquals(123, Objects.hashCode(123)); // single object, should return its hash code
        assertEquals(321, Objects.hashCode(123, "abc")); // multiple objects, should return combined hash code
        assertEquals(456, Objects.hashCode(null)); // null object, should return 0
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.addValue("abc");
        helper.addValue(123);

        assertEquals("Test{abc=abc, 123=123}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        String first = "abc";
        String second = "def";
        assertEquals("abc", Objects.firstNonNull(first, second)); // first is not null, should return first
        assertNull(Objects.firstNonNull(null, second)); // first is null, should return second
    }

}

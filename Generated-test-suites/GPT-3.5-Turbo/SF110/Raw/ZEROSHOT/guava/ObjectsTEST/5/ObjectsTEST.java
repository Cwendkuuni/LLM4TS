import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals(123, Objects.hashCode(123));
        assertEquals(123456, Objects.hashCode(123, 456));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this);
        assertEquals("ObjectsTest{", helper.toString());

        helper.add("name", "John");
        assertEquals("ObjectsTest{name=John}", helper.toString());

        helper.add("age", 25);
        assertEquals("ObjectsTest{name=John, age=25}", helper.toString());

        helper.addValue(true);
        assertEquals("ObjectsTest{name=John, age=25, true}", helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("abc", Objects.firstNonNull("abc", null));
        assertEquals("def", Objects.firstNonNull(null, "def"));
        assertEquals("xyz", Objects.firstNonNull("xyz", "abc"));
    }

}
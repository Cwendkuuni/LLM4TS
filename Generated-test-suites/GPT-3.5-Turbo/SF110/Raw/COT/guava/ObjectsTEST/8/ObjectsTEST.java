
import org.junit.Assert;
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
        assertEquals(0, Objects.hashCode(null));
        assertEquals(123, Objects.hashCode(123));
        assertEquals(1234, Objects.hashCode(123, 4));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("ClassName");
        assertNotNull(helper);
        assertNotNull(helper.toString());

        helper = Objects.toStringHelper(Object.class);
        assertNotNull(helper);
        assertNotNull(helper.toString());

        helper = Objects.toStringHelper(new Object());
        assertNotNull(helper);
        assertNotNull(helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("abc", Objects.firstNonNull(null, "abc"));
        assertEquals("def", Objects.firstNonNull("def", null));
        assertEquals("ghi", Objects.firstNonNull("ghi", "jkl"));
    }
}

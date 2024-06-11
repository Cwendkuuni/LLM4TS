
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("hello", "hello"));
        assertFalse(Objects.equal("hello", "world"));
    }

    @Test
    public void testHashCode() {
        assertEquals(0, Objects.hashCode());
        assertEquals(1, Objects.hashCode(null));
        assertEquals(123, Objects.hashCode(123));
        assertEquals(456, Objects.hashCode(456, "hello", true));
    }

    @Test
    public void testToStringHelper() {
        assertEquals("ObjectsTest{}", Objects.toStringHelper(ObjectsTest.class).toString());
        assertEquals("Test{}", Objects.toStringHelper("Test").toString());
        assertEquals("ToStringHelper{className=Objects}", Objects.toStringHelper(new Objects.ToStringHelper("Objects")).toString());
    }

    @Test
    public void testFirstNonNull() {
        assertNull(Objects.firstNonNull(null, null));
        assertEquals("hello", Objects.firstNonNull(null, "hello"));
        assertEquals(123, Objects.firstNonNull(123, "world"));
    }
}

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "abc"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("abc", "def"), Objects.hashCode("abc", "def"));
        assertNotEquals(Objects.hashCode("abc", "def"), Objects.hashCode("def", "abc"));
        assertEquals(Objects.hashCode("abc"), Objects.hashCode("abc"));
        assertEquals(Objects.hashCode(), Objects.hashCode());
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
    }

    // Add more test cases for other methods if needed

}
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
        assertEquals(Objects.hashCode("a", "b", "c"), Objects.hashCode("a", "b", "c"));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertNotNull(helper.toString());
    }

    @Test
    public void testFirstNonNull() {
        String result = Objects.firstNonNull(null, "default");
        assertEquals("default", result);
    }

    // Add more test cases for other public methods as needed

}
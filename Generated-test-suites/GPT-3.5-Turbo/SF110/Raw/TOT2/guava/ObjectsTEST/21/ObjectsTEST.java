import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(), 1); // Edge case with no objects provided
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        assertNotNull(helper);
    }

    @Test
    public void testFirstNonNull() {
        String result = Objects.firstNonNull(null, "test");
        assertEquals("test", result);
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("Name", "Value");
        assertEquals("Test{Name=Value}", helper.toString());
    }

    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.addValue("Value");
        assertEquals("Test{Value}", helper.toString());
    }
}
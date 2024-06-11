import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal("a", "a"));
        assertFalse(Objects.equal("a", "b"));
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("a", "b"), Objects.hashCode("a", "b"));
        assertNotEquals(Objects.hashCode("a", "b"), Objects.hashCode("b", "a"));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper(Object.class);
        assertNotNull(helper);
    }

    @Test
    public void testFirstNonNull() {
        Integer result = Objects.firstNonNull(null, 5);
        assertNotNull(result);
        assertEquals(Integer.valueOf(5), result);
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("key", "value");
        String result = helper.toString();
        assertEquals("Test{key=value}", result);
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.omitNullValues();
        helper.add("key", null);
        String result = helper.toString();
        assertEquals("Test{}", result);
    }

    // Add more test cases for other methods as needed

}
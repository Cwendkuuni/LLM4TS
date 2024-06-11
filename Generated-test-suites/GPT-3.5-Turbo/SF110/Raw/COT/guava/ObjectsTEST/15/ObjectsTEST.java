import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Basic test case
        assertTrue(Objects.equal("abc", "abc"));
    }

    @Test
    public void testHashCode() {
        // Basic test case
        int hashCode = Objects.hashCode("abc", 123);
        assertEquals(-1938725304, hashCode);
    }

    @Test
    public void testToStringHelper() {
        // Basic test case
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add("name", "John");
        helper.addValue(25);
        String result = helper.toString();
        assertEquals("Test{name=John, 25}", result);
    }

    @Test
    public void testFirstNonNull() {
        // Basic test case
        String result = Objects.firstNonNull(null, "abc");
        assertEquals("abc", result);
    }

    // Edge case - Testing equal method with null input
    @Test
    public void testEqualWithNullInput() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal("abc", null));
        assertFalse(Objects.equal(null, "abc"));
    }

    // Exception handling - Testing add method with null name and value
    @Test(expected = NullPointerException.class)
    public void testAddWithNullNameAndValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.add(null, null);
    }

    // Exception handling - Testing addHolder method with null value
    @Test(expected = NullPointerException.class)
    public void testAddHolderWithNullValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");
        helper.addHolder(null);
    }

    // Edge case - Testing addHolder method with null name and value
    @Test
    public void testAddHolderWithNullNameAndValue() {
        Objects.ToStringHelper helper = Objects.toStringHelper("Test");

        Objects.ToStringHelper.ValueHolder valueHolder = helper.addHolder(null, null);

        assertNull(valueHolder.name);
        assertNull(valueHolder.value);
    }

}
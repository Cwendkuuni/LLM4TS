
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Basic test case
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "someValue"));
        assertFalse(Objects.equal("someValue", null));
        assertTrue(Objects.equal("sameValue", "sameValue"));

        // Edge cases
        assertTrue(Objects.equal("a", "a"));
        assertFalse(Objects.equal("a", "A"));
    }

    @Test
    public void testHashCode() {
        // Basic test case
        assertEquals(Objects.hashCode(), 0);
        assertEquals(Objects.hashCode(null), 0);
        assertEquals(Objects.hashCode("someValue"), "someValue".hashCode());

        // Edge cases
        assertEquals(Objects.hashCode("a", "b", "c"), Arrays.hashCode(new Object[]{"a", "b", "c"}));
    }

    @Test
    public void testToStringHelper() {
        // Basic test case
        assertEquals(Objects.toStringHelper("ClassName").toString(), "ClassName{}");

        // Edge cases
        assertEquals(Objects.toStringHelper("a").add("b", "c").toString(), "a{b=c}");
        assertEquals(Objects.toStringHelper("a").omitNullValues().toString(), "a{}");
    }

    @Test
    public void testFirstNonNull() {
        // Basic test case
        assertEquals(Objects.firstNonNull(null, "defaultValue"), "defaultValue");
        assertEquals(Objects.firstNonNull("value", "defaultValue"), "value");

        // Edge cases
        assertEquals(Objects.firstNonNull("a", "A"), "a");
        assertEquals(Objects.firstNonNull("a", "A", "b", "B"), "a");
    }
}

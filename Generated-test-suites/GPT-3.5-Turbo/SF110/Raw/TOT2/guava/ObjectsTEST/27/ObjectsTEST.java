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
        Object[] objects = {1, "test", true};
        assertEquals(Objects.hashCode(objects), Arrays.hashCode(objects));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("test");
        assertNotNull(toStringHelper);
    }

    @Test
    public void testFirstNonNull() {
        Integer first = 5;
        Integer second = null;
        assertEquals(Objects.firstNonNull(first, second), first);
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("test").omitNullValues();
        assertNotNull(toStringHelper);
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("test").add("name", "value");
        assertNotNull(toStringHelper);
    }

    // Add more test cases for other methods as needed

}
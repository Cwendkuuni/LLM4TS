import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Objects;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("abc", "abc"));
        assertFalse(Objects.equal("abc", "def"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode("a", "b", "c"), Objects.hashCode("a", "b", "c"));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Test");
        assertNotNull(toStringHelper.toString());
    }

    @Test
    public void testFirstNonNull() {
        Object first = new Object();
        Object second = null;
        assertEquals(first, Objects.firstNonNull(first, second));
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.add("name", "value");
        assertNotNull(toStringHelper.toString());
    }

    @Test
    public void testToStringHelperAddValue() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.addValue("value");
        assertNotNull(toStringHelper.toString());
    }

    @Test
    public void testToStringHelperOmitNullValues() {
        Objects.ToStringHelper toStringHelper = Objects.toStringHelper("Test");
        toStringHelper.add("name", "value");
        toStringHelper.add("nullValue", null).omitNullValues();
        assertEquals("Test{name=value}", toStringHelper.toString());
    }

}
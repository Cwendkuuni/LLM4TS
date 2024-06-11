import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Objects;

public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "test"));
        assertFalse(Objects.equal("test", null));
        assertTrue(Objects.equal("test", "test"));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hashCode(), 1);
        assertEquals(Objects.hashCode("test"), "test".hashCode());
        assertEquals(Objects.hashCode("test1", "test2"), Arrays.hashCode(new Object[]{"test1", "test2"}));
    }

    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        assertNotNull(helper);
        assertTrue(helper.toString().contains("TestClass"));
    }

    @Test
    public void testFirstNonNull() {
        assertEquals(Objects.firstNonNull(null, "default"), "default");
        assertEquals(Objects.firstNonNull("value", "default"), "value");
    }

    @Test
    public void testToStringHelperAdd() {
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.add("testName", "testValue");
        assertTrue(helper.toString().contains("testName=testValue"));
    }

    // Add test cases for other methods, edge cases, and potential exceptions

}
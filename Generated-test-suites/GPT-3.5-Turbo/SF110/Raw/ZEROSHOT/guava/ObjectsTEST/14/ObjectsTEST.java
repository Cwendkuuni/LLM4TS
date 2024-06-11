import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test case 1
        assertTrue(Objects.equal(null, null));

        // Test case 2
        assertFalse(Objects.equal(null, "test"));

        // Test case 3
        assertTrue(Objects.equal("test", "test"));

        // Test case 4
        assertFalse(Objects.equal("test", null));
    }

    @Test
    public void testHashCode() {
        // Test case 1
        assertEquals(0, Objects.hashCode(null));

        // Test case 2
        assertEquals(1, Objects.hashCode(true));

        // Test case 3
        assertEquals(123, Objects.hashCode(123));

        // Test case 4
        assertEquals(-345, Objects.hashCode(-345));

        // Test case 5
        assertEquals(12345, Objects.hashCode(1, 2, 3, 4, 5));
    }

    @Test
    public void testToStringHelper() {
        // Test case 1
        Objects.ToStringHelper helper = Objects.toStringHelper("TestClass");
        helper.addValue("test");
        assertEquals("TestClass{test}", helper.toString());

        // Test case 2
        Objects.ToStringHelper helper2 = Objects.toStringHelper(String.class);
        helper2.addValue(null);
        helper2.addValue(true);
        assertEquals("String{null, true}", helper2.toString());

        // Test case 3
        Objects.ToStringHelper helper3 = Objects.toStringHelper(Objects.toStringHelper.class);
        helper3.addValue("test").addValue(123);
        assertEquals("ToStringHelper{test, 123}", helper3.toString());
    }

    @Test
    public void testFirstNonNull() {
        // Test case 1
        assertNull(Objects.firstNonNull(null, null));

        // Test case 2
        assertNull(Objects.firstNonNull(null, "test"));

        // Test case 3
        assertEquals("test", Objects.firstNonNull("test", null));
    }
}
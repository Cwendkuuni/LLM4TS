import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {

    // test equal() method
    @Test
    public void testEqual() {
        // test when both objects are null
        boolean result1 = Objects.equal(null, null);
        assertTrue(result1);

        // test when one object is null and the other is not
        boolean result2 = Objects.equal("abc", null);
        assertFalse(result2);

        // test when both objects are not null and are equal
        boolean result3 = Objects.equal("abc", "abc");
        assertTrue(result3);

        // test when both objects are not null and are not equal
        boolean result4 = Objects.equal("abc", "def");
        assertFalse(result4);
    }

    // test hashCode() method
    @Test
    public void testHashCode() {
        int result1 = Objects.hashCode(null);
        assertEquals(0, result1);

        int result2 = Objects.hashCode("abc", "def");
        int expected2 = Arrays.hashCode(new Object[] {"abc", "def"});
        assertEquals(expected2, result2);

        int result3 = Objects.hashCode("abc", "def", "ghi");
        int expected3 = Arrays.hashCode(new Object[] {"abc", "def", "ghi"});
        assertEquals(expected3, result3);
    }

    // test toStringHelper() methods
    @Test
    public void testToStringHelper() {
        Objects.ToStringHelper helper1 = Objects.toStringHelper("Test");
        assertNotNull(helper1);

        Objects.ToStringHelper helper2 = Objects.toStringHelper(ObjectsTest.class);
        assertNotNull(helper2);

        Objects.ToStringHelper helper3 = Objects.toStringHelper(ObjectsTest.class.getSimpleName());
        assertNotNull(helper3);
    }

    // test firstNonNull() method
    @Test
    public void testFirstNonNull() {
        String result1 = Objects.firstNonNull(null, "abc");
        assertEquals("abc", result1);

        String result2 = Objects.firstNonNull("def", "ghi");
        assertEquals("def", result2);
    }

}
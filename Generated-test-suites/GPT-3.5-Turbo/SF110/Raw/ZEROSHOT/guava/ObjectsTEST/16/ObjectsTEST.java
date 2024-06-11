import static org.junit.Assert.*;
import org.junit.Test;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test when both objects are null
        assertTrue(Objects.equal(null, null));

        // Test when the first object is null and the second object is not null
        assertFalse(Objects.equal(null, "test"));

        // Test when the first object is not null and the second object is null
        assertFalse(Objects.equal("test", null));

        // Test when both objects are equal
        assertTrue(Objects.equal("test", "test"));

        // Test when the objects are not equal
        assertFalse(Objects.equal("test1", "test2"));
    }

    @Test
    public void testHashCode() {
        // Test when no objects are passed
        assertEquals(0, Objects.hashCode());

        // Test when one object is passed
        Object obj = new Object();
        assertEquals(obj.hashCode(), Objects.hashCode(obj));

        // Test when multiple objects are passed
        Object obj1 = new Object();
        Object obj2 = new Object();

        int expectedHashCode = Arrays.hashCode(new Object[] { obj1, obj2 });
        assertEquals(expectedHashCode, Objects.hashCode(obj1, obj2));
    }

    @Test
    public void testToStringHelper() {
        // Test toStringHelper with object
        Objects.ToStringHelper helper1 = Objects.toStringHelper(new Object());
        assertNotNull(helper1);

        // Test toStringHelper with class
        Objects.ToStringHelper helper2 = Objects.toStringHelper(String.class);
        assertNotNull(helper2);

        // Test toStringHelper with class name
        Objects.ToStringHelper helper3 = Objects.toStringHelper("TestClass");
        assertNotNull(helper3);
    }

    @Test
    public void testFirstNonNull() {
        // Test when both objects are null
        assertNull(Objects.firstNonNull(null, null));

        // Test when the first object is null and the second object is not null
        assertNotNull(Objects.firstNonNull(null, "test"));

        // Test when the first object is not null and the second object is null
        assertNotNull(Objects.firstNonNull("test", null));

        // Test when both objects are not null
        Object obj = new Object();
        assertSame(obj, Objects.firstNonNull(obj, "test"));
    }
}
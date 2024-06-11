import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    @Test
    public void testEqual() {
        // Test equal objects
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal("hello", "hello"));
        assertTrue(Objects.equal(123, 123));
        assertTrue(Objects.equal(1.23, 1.23));

        // Test unequal objects
        assertFalse(Objects.equal("hello", "world"));
        assertFalse(Objects.equal(123, 456));
        assertFalse(Objects.equal(1.23, 4.56));

        // Test objects with null values
        assertFalse(Objects.equal(null, "world"));
        assertFalse(Objects.equal("hello", null));
    }

    @Test
    public void testHashCode() {
        // Test objects with values
        assertEquals(Objects.hashCode(null), 0);
        assertEquals(Objects.hashCode("hello"), "hello".hashCode());
        assertEquals(Objects.hashCode(123), Integer.hashCode(123));
        assertEquals(Objects.hashCode(1.23), Double.hashCode(1.23));

        // Test objects with multiple values
        assertEquals(Objects.hashCode("hello", "world"), ("hello" + "world").hashCode());
        assertEquals(Objects.hashCode(123, 456), Integer.hashCode(123) + Integer.hashCode(456));
        assertEquals(Objects.hashCode(1.23, 4.56), Double.hashCode(1.23) + Double.hashCode(4.56));

        // Test objects with null values
        assertEquals(Objects.hashCode(null, "world"), "world".hashCode());
        assertEquals(Objects.hashCode("hello", null), "hello".hashCode());
    }

    @Test
    public void testToStringHelper() {
        // Test toStringHelper with object
        Objects.ToStringHelper helperObject = Objects.toStringHelper("Object");
        assertNotNull(helperObject);
        assertEquals(helperObject.toString(), "Object{}");

        // Test toStringHelper with class
        Objects.ToStringHelper helperClass = Objects.toStringHelper(ObjectsTest.class);
        assertNotNull(helperClass);
        assertEquals(helperClass.toString(), "ObjectsTest{}");

        // Test toStringHelper with className
        Objects.ToStringHelper helperClassName = Objects.toStringHelper("ClassName");
        assertNotNull(helperClassName);
        assertEquals(helperClassName.toString(), "ClassName{}");
    }

    @Test
    public void testFirstNonNull() {
        // Test firstNonNull with non-null values
        assertEquals(Objects.firstNonNull("hello", "world"), "hello");
        assertEquals(Objects.firstNonNull(123, 456), Integer.valueOf(123));
        assertEquals(Objects.firstNonNull(1.23, 4.56), Double.valueOf(1.23));

        // Test firstNonNull with null values
        assertEquals(Objects.firstNonNull(null, "world"), "world");
        assertEquals(Objects.firstNonNull("hello", null), "hello");
    }

}
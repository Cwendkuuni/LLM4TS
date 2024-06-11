import com.google.common.base.Objects;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

    // Test case for equal method
    @Test
    public void testEqual() {
        // Typical use case - equal objects
        Object a = "hello";
        Object b = "hello";
        assertTrue(Objects.equal(a, b));

        // Typical use case - unequal objects
        Object c = "hello";
        Object d = "world";
        assertFalse(Objects.equal(c, d));

        // Edge case - both objects are null
        Object e = null;
        Object f = null;
        assertTrue(Objects.equal(e, f));

        // Edge case - one object is null, other is not null
        Object g = null;
        Object h = "hello";
        assertFalse(Objects.equal(g, h));

        // Error scenario - comparing with non-compatible types
        Object i = "hello";
        int j = 5;
        assertFalse(Objects.equal(i, j));
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        // Typical use case - multiple objects
        Object[] objects = {"apple", "banana", "cherry"};
        int expectedHashCode = Objects.hashCode(objects);
        assertEquals(expectedHashCode, 96259);

        // Edge case - single object
        Object[] singleObject = {"apple"};
        int expectedHashCodeSingle = Objects.hashCode(singleObject);
        assertEquals(expectedHashCodeSingle, 64683);

        // Edge case - empty array
        Object[] emptyArray = new Object[0];
        int expectedHashCodeEmpty = Objects.hashCode(emptyArray);
        assertEquals(expectedHashCodeEmpty, 1);
    }

    // Test case for toStringHelper methods
    @Test
    public void testToStringHelper() {
        // Typical use case - add method with name and value
        String expectedString1 = "Objects.ToStringHelperTest{firstName=John, lastName=Doe}";
        String toString1 = Objects.toStringHelper("Objects.ToStringHelperTest")
                .add("firstName", "John")
                .add("lastName", "Doe")
                .toString();
        assertEquals(expectedString1, toString1);

        // Typical use case - addValue method with value
        String expectedString2 = "Objects.ToStringHelperTest{John Doe}";
        String toString2 = Objects.toStringHelper("Objects.ToStringHelperTest")
                .addValue("John Doe")
                .toString();
        assertEquals(expectedString2, toString2);

        // Edge case - add method with name and null value, omitNullValues is true
        String expectedString3 = "Objects.ToStringHelperTest{}";
        String toString3 = Objects.toStringHelper("Objects.ToStringHelperTest")
                .add("firstName", null)
                .omitNullValues()
                .toString();
        assertEquals(expectedString3, toString3);

        // Error scenario - add method with null name and value
        String expectedString4 = "Objects.ToStringHelperTest{name=null}";
        String toString4 = Objects.toStringHelper("Objects.ToStringHelperTest")
                .add(null, null)
                .toString();
        assertEquals(expectedString4, toString4);
    }

    // Test case for firstNonNull method
    @Test
    public void testFirstNonNull() {
        // Typical use case - first object is non-null
        String expectedFirstNonNull1 = "hello";
        String firstNonNull1 = Objects.firstNonNull("hello", "world");
        assertEquals(expectedFirstNonNull1, firstNonNull1);

        // Typical use case - second object is non-null
        String expectedFirstNonNull2 = "world";
        String firstNonNull2 = Objects.firstNonNull(null, "world");
        assertEquals(expectedFirstNonNull2, firstNonNull2);

        // Edge case - both objects are null
        Integer expectedFirstNonNull3 = null;
        Integer firstNonNull3 = Objects.firstNonNull(null, null);
        assertEquals(expectedFirstNonNull3, firstNonNull3);

        // Error scenario - non-compatible types
        Double expectedFirstNonNull4 = null;
        Double firstNonNull4 = Objects.firstNonNull("hello", 10);
        assertEquals(expectedFirstNonNull4, firstNonNull4);
    }
}
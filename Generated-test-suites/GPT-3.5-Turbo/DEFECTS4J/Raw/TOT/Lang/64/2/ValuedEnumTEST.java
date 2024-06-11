import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        // typical use case
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());

        // edge case - minimum value
        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", Integer.MIN_VALUE);
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(Integer.MIN_VALUE, enum2.getValue());

        // error scenario - negative value
        try {
            ValuedEnum enum3 = new JavaVersionEnum("Java 1.2", -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success - exception thrown
        }
    }

    @Test
    public void testGetEnum() {
        // typical use case
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.getEnum("Java 1.0"));
        assertEquals(JavaVersionEnum.JAVA1_1, JavaVersionEnum.getEnum("Java 1.1"));
        assertEquals(JavaVersionEnum.JAVA1_2, JavaVersionEnum.getEnum("Java 1.2"));
        assertEquals(JavaVersionEnum.JAVA1_3, JavaVersionEnum.getEnum("Java 1.3"));

        // edge case - null value
        assertNull(JavaVersionEnum.getEnum(null));

        // error scenario - non-existent value
        assertNull(JavaVersionEnum.getEnum("Java 1.4"));
    }

    @Test
    public void testGetEnumWithInt() {
        // typical use case
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.getEnum(100));
        assertEquals(JavaVersionEnum.JAVA1_1, JavaVersionEnum.getEnum(110));
        assertEquals(JavaVersionEnum.JAVA1_2, JavaVersionEnum.getEnum(120));
        assertEquals(JavaVersionEnum.JAVA1_3, JavaVersionEnum.getEnum(130));

        // edge case - minimum value
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.getEnum(Integer.MIN_VALUE));

        // error scenario - non-existent value
        assertNull(JavaVersionEnum.getEnum(150));
        assertNull(JavaVersionEnum.getEnum(Integer.MAX_VALUE));
    }

    @Test
    public void testGetEnumMap() {
        // typical use case
        assertEquals(4, JavaVersionEnum.getEnumMap().size());
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.getEnumMap().get("Java 1.0"));
        assertEquals(JavaVersionEnum.JAVA1_1, JavaVersionEnum.getEnumMap().get("Java 1.1"));
        assertEquals(JavaVersionEnum.JAVA1_2, JavaVersionEnum.getEnumMap().get("Java 1.2"));
        assertEquals(JavaVersionEnum.JAVA1_3, JavaVersionEnum.getEnumMap().get("Java 1.3"));

        // edge case - empty map
        // empty map is not possible, as there are hardcoded enum values

        // error scenario - null map
        // null map is not possible, as there are hardcoded enum values
    }

    @Test
    public void testGetEnumList() {
        // typical use case
        assertEquals(4, JavaVersionEnum.getEnumList().size());
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_0));
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_1));
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_2));
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_3));

        // edge case - empty list
        // empty list is not possible, as there are hardcoded enum values

        // error scenario - null list
        // null list is not possible, as there are hardcoded enum values
    }

    @Test
    public void testIterator() {
        // typical use case
        int count = 0;
        for (Iterator<JavaVersionEnum> it = JavaVersionEnum.iterator(); it.hasNext();) {
            assertNotNull(it.next());
            count++;
        }
        assertEquals(4, count);

        // edge case - empty iterator
        // empty iterator is not possible, as there are hardcoded enum values

        // error scenario - null iterator
        // null iterator is not possible, as there are hardcoded enum values
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;

        int value = javaVersionEnum.getValue();

        assertEquals(100, value);
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(100);

        assertNotNull(javaVersionEnum);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersionEnum);
    }

    @Test
    public void testGetEnumByNonExistingValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(200);

        assertNull(javaVersionEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        Enum.getEnum(null, 100);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.JAVA1_1;

        assertTrue(javaVersionEnum1.compareTo(javaVersionEnum2) < 0);
        assertTrue(javaVersionEnum2.compareTo(javaVersionEnum1) > 0);
        assertEquals(0, javaVersionEnum1.compareTo(JavaVersionEnum.JAVA1_0));
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;

        String str = javaVersionEnum.toString();

        assertEquals("JavaVersionEnum[Java 1.0=100]", str);
    }

    @Test
    public void testIterator() {
        Iterator<JavaVersionEnum> iterator = JavaVersionEnum.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }

        assertEquals(4, count);
    }

    // Additional test cases can be added for getEnumMap() and getEnumList()
}
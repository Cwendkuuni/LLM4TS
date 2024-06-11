import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersionEnum.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersionEnum1.compareTo(javaVersionEnum2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[JAVA1_0=100]", javaVersionEnum.toString());
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersionEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByValueWithNullClass() {
        JavaVersionEnum.getEnum(null, 100);
    }

    // More tests for edge cases and error scenarios can be added here

}
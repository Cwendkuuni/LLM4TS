import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(110);
        assertEquals("Java 1.1", javaVersionEnum.getName());
    }

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(120);
        assertEquals(120, javaVersionEnum.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.getEnum(100);
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.getEnum(130);
        assertTrue(javaVersionEnum1.compareTo(javaVersionEnum2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(100);
        assertEquals("JavaVersion[Java 1.0=100]", javaVersionEnum.toString());
    }
}
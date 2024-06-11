import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaEnum.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaEnum = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, javaEnum);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaEnum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaEnum2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaEnum1.compareTo(javaEnum2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaEnum = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", javaEnum.toString());
    }
}
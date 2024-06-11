import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", javaVersion.toString());
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersion);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByValueWithNullClass() {
        JavaVersionEnum.getEnum(null, 100);
    }

}
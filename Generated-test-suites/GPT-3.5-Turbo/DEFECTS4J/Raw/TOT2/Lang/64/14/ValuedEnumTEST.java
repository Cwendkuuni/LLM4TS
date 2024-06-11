import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum result = JavaVersionEnum.getEnum(100);
        assertEquals(javaVersion, result);
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
        assertEquals("JavaVersionEnum[Java 1.0=100]", javaVersion.toString());
    }
}
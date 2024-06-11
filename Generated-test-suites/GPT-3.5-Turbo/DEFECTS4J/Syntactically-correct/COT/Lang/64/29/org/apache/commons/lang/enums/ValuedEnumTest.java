import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        int expectedValue = 100;
        int actualValue = javaVersion.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(100);
        assertNotNull(javaVersion);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersion);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        JavaVersionEnum.getEnum(null, 100);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
        assertTrue(javaVersion2.compareTo(javaVersion1) > 0);
        assertEquals(0, javaVersion1.compareTo(JavaVersionEnum.JAVA1_0));
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        String expectedToString = "JavaVersion[JAVA1_0=100]";
        assertEquals(expectedToString, javaVersion.toString());
    }
}
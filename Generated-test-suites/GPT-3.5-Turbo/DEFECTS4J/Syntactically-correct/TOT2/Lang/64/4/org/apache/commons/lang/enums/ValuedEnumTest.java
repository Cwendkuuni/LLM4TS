import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    private JavaVersionEnum javaVersion;

    @Before
    public void setUp() {
        javaVersion = JavaVersionEnum.JAVA1_0;
    }

    @Test
    public void testGetValue() {
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum result = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, result);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum other = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersion.compareTo(other) < 0);
    }

    @Test
    public void testToString() {
        String expected = "JavaVersion[Java 1.0=100]";
        assertEquals(expected, javaVersion.toString());
    }
}
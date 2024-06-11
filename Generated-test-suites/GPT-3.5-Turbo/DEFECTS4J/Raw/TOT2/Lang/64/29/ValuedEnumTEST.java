import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnumByValue() {
        ValuedEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals("Java 1.0", java1_0.getName());
    }

    @Test
    public void testGetValue() {
        ValuedEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java1_0.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = JavaVersionEnum.JAVA1_0;
        ValuedEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertTrue(java1_0.compareTo(java1_1) < 0);
    }

    @Test
    public void testToString() {
        ValuedEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java12 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java13 = JavaVersionEnum.JAVA1_3;

        assertEquals(100, java10.getValue());
        assertEquals(110, java11.getValue());
        assertEquals(120, java12.getValue());
        assertEquals(130, java13.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java12 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java13 = JavaVersionEnum.JAVA1_3;

        assertTrue(java10.compareTo(java11) < 0);
        assertTrue(java11.compareTo(java12) < 0);
        assertTrue(java12.compareTo(java13) < 0);
        assertEquals(0, java10.compareTo(java10));
        assertEquals(0, java11.compareTo(java11));
        assertEquals(0, java12.compareTo(java12));
        assertEquals(0, java13.compareTo(java13));
    }

    @Test
    public void testToString() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java12 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java13 = JavaVersionEnum.JAVA1_3;

        assertEquals("JavaVersionEnum[Java 1.0=100]", java10.toString());
        assertEquals("JavaVersionEnum[Java 1.1=110]", java11.toString());
        assertEquals("JavaVersionEnum[Java 1.2=120]", java12.toString());
        assertEquals("JavaVersionEnum[Java 1.3=130]", java13.toString());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum JAVA1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, JAVA1_0.getValue());
        
        JavaVersionEnum JAVA1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, JAVA1_1.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, enum1);
        
        JavaVersionEnum enum2 = JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, enum2);
        
        JavaVersionEnum enum3 = JavaVersionEnum.getEnum(120);
        assertNull(enum3);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum JAVA1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum JAVA1_1 = JavaVersionEnum.JAVA1_1;

        assertTrue(JAVA1_0.compareTo(JAVA1_1) < 0);
        assertTrue(JAVA1_1.compareTo(JAVA1_0) > 0);
        assertTrue(JAVA1_0.compareTo(JAVA1_0) == 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum JAVA1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", JAVA1_0.toString());
        
        JavaVersionEnum JAVA1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals("JavaVersion[Java 1.1=110]", JAVA1_1.toString());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, enum1.getValue());

        JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, enum2.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(JavaVersionEnum.JAVA1_0, enum1);

        JavaVersionEnum enum2 = JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, enum2);

        JavaVersionEnum enum3 = JavaVersionEnum.getEnum("Invalid");
        assertNull(enum3);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertTrue(enum1.compareTo(enum2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());
    }
}
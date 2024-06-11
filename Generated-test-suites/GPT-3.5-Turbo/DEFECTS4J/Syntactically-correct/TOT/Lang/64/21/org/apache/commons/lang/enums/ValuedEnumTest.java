import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = (ValuedEnum)ValuedEnum.getEnum(JavaVersionEnum.class, 110);
        assertNotNull(enum1);
        assertEquals("Java 1.1", enum1.getName());
    }

    @Test
    public void testGetValue() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, enum1.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_2;
        assertTrue(enum1.compareTo(enum2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_1;
        String expected = "JavaVersionEnum[Java 1.1=110]";
        assertEquals(expected, enum1.toString());
    }

}
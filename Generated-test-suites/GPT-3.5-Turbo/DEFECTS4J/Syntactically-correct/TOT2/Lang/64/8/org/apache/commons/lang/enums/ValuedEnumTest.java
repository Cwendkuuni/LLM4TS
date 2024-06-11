import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum enumValue = JavaVersionEnum.JAVA1_0;
        assertEquals(100, enumValue.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertTrue(enum1.compareTo(enum2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum enumValue = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersionEnum[Java 1.0=100]", enumValue.toString());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum enumValue = JavaVersionEnum.getEnum(100);
        assertNotNull(enumValue);
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum enumValue = JavaVersionEnum.JAVA1_0;
        assertEquals(100, enumValue.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum enumValue = (JavaVersionEnum)ValuedEnum.getEnum(JavaVersionEnum.class, 100);
        assertEquals("Java 1.0", enumValue.getName());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum enumValue1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum enumValue2 = JavaVersionEnum.JAVA1_1;
        assertTrue(enumValue1.compareTo(enumValue2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum enumValue = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersionEnum[Java 1.0=100]", enumValue.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test
    public void testGetEnumWithInvalidValue() {
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 150));
    }
}
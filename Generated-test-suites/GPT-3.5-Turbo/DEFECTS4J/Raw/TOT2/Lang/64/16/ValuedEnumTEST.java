import static org.junit.Assert.*;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        ValuedEnum valuedEnum = new JavaVersionEnum("Test", 123);
        assertEquals("Test", valuedEnum.getName());
        assertEquals(123, valuedEnum.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum enumValue = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, enumValue);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = JavaVersionEnum.JAVA1_0;
        ValuedEnum enum2 = JavaVersionEnum.JAVA1_1;

        assertTrue(enum1.compareTo(enum2) < 0);
    }

    @Test
    public void testToString() {
        ValuedEnum enumValue = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersionEnum[Java 1.0=100]", enumValue.toString());
    }
}
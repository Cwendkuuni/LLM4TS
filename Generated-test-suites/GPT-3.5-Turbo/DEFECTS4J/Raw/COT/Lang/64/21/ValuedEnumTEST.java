import org.junit.Test;

import java.util.Enum;

import static org.junit.Assert.*;

public class ValuedEnumTest {

    // Basic test cases

    @Test
    public void testGetEnum() {
        Class enumClass = ValuedEnum.class;
        int value = 100;
        Enum e = ValuedEnum.getEnum(enumClass, value);
        assertNotNull(e);
        assertEquals(value, ((ValuedEnum) e).getValue());
    }

    @Test
    public void testGetValue() {
        String name = "Java 1.0";
        int value = 100;
        ValuedEnum enumItem = new JavaVersionEnum(name, value);
        assertEquals(value, enumItem.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum e1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum e2 = new JavaVersionEnum("Java 1.1", 110);
        assertTrue(e1.compareTo(e2) < 0);
        assertTrue(e2.compareTo(e1) > 0);
        assertTrue(e1.compareTo(e1) == 0);
    }

    @Test
    public void testToString() {
        String name = "Java 1.0";
        int value = 100;
        ValuedEnum enumItem = new JavaVersionEnum(name, value);
        assertEquals("JavaVersionEnum[Java 1.0=100]", enumItem.toString());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        Class enumClass = null;
        int value = 100;
        ValuedEnum.getEnum(enumClass, value);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentType() {
        ValuedEnum e = new JavaVersionEnum("Java 1.0", 100);
        Integer i = new Integer(100);
        e.compareTo(i);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToWithNull() {
        ValuedEnum e = new JavaVersionEnum("Java 1.0", 100);
        e.compareTo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValuedEnumWithNegativeValue() {
        String name = "Negative";
        int value = -1;
        ValuedEnum enumItem = new JavaVersionEnum(name, value);
    }

}
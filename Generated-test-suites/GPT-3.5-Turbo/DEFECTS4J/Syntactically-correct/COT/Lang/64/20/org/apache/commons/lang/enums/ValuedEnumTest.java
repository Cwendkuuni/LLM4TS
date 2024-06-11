import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        // Create ValuedEnum instance
        ValuedEnum enumValue = new ValuedEnumImpl("Name", 123);

        // Test the getValue method
        int value = enumValue.getValue();
        assertEquals(123, value);
    }

    @Test
    public void testCompareTo() {
        // Create ValuedEnum instances
        ValuedEnum enumValue1 = new ValuedEnumImpl("Name1", 123);
        ValuedEnum enumValue2 = new ValuedEnumImpl("Name2", 456);
        ValuedEnum enumValue3 = new ValuedEnumImpl("Name3", 123);

        // Test the compareTo method
        int result1 = enumValue1.compareTo(enumValue2);
        int result2 = enumValue1.compareTo(enumValue3);
        assertEquals(-333, result1);
        assertEquals(0, result2);
    }

    @Test
    public void testToString() {
        // Create ValuedEnum instance
        ValuedEnum enumValue = new ValuedEnumImpl("Name", 123);

        // Test the toString method
        String str = enumValue.toString();
        assertEquals("ValuedEnumImpl[Name=123]", str);
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testGetEnum() {
        // Test the getEnum method with valid value
        ValuedEnum enumValue = ValuedEnum.getEnum(123);
        assertNotNull(enumValue);
        assertEquals("Name", enumValue.getName());
        assertEquals(123, enumValue.getValue());

        // Test the getEnum method with invalid value
        ValuedEnum nullEnumValue = ValuedEnum.getEnum(456);
        assertNull(nullEnumValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        // Test the getEnum method with null class
        ValuedEnum enumValue = ValuedEnum.getEnum(null, 123);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToInvalidType() {
        // Create ValuedEnum instance
        ValuedEnum enumValue = new ValuedEnumImpl("Name", 123);

        // Test the compareTo method with invalid type
        enumValue.compareTo("Invalid");
    }

    private static class ValuedEnumImpl extends ValuedEnum {

        public ValuedEnumImpl(String name, int value) {
            super(name, value);
        }
    }
}


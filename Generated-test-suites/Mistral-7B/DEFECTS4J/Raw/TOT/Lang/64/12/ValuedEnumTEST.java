import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnumByValue() {
        ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, 100);
        ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, 110);
        ValuedEnum java1_2 = ValuedEnum.getEnum(ValuedEnum.class, 120);

        // Test with valid values
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals("Java 1.2", java1_2.getName());

        // Test with null class
        assertNull(ValuedEnum.getEnum(null, 100));

        // Test with null value
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, null));

        // Test with invalid value
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, 500));
    }

    @Test
    public void testGetEnumByName() {
        ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.0");
        ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.1");
        ValuedEnum java1_2 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.2");

        // Test with valid names
        assertEquals(100, java1_0.getValue());
        assertEquals(110, java1_1.getValue());
        assertEquals(120, java1_2.getValue());

        // Test with null class
        assertNull(ValuedEnum.getEnum(null, "Java 1.0"));

        // Test with null name
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, null));

        // Test with invalid name
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, "InvalidName"));
    }
}
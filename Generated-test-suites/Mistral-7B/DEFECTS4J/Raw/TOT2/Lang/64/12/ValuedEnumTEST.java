import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {
    @Test
    public void testGetEnumByName() {
        ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertEquals("Java 1.0", javaVersionEnum.getName());
        assertEquals(100, javaVersionEnum.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNameWithInvalidName() {
        try {
            ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", "InvalidName");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid name: InvalidName", e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumByNameWithNullClass() {
        try {
            ValuedEnum javaVersionEnum = ValuedEnum.getEnum(null, "JAVA1_0");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
}
import static org.junit.Assert.*;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        // Typical use case
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        assertEquals(100, valuedEnum.getValue());
    }

    @Test
    public void testGetEnum() {
        // Typical use case
        ValuedEnum enumValue = JavaVersionEnum.getEnum(100);
        assertNotNull(enumValue);
        
        // Edge case - Enum not found
        enumValue = JavaVersionEnum.getEnum(200);
        assertNull(enumValue);
    }

    @Test
    public void testCompareTo() {
        // Typical use case
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
        assertTrue(enum1.compareTo(enum2) < 0);
        
        // Edge case - Comparing with null
        try {
            enum1.compareTo(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected exception
        }
    }

    @Test
    public void testToString() {
        // Typical use case
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("JavaVersion[Java 1.0=100]", valuedEnum.toString());
    }
}
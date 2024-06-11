
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        // Test with an existing enumClass and a valid value
        Class enumClass1 = JavaVersionEnum.class;
        int value1 = JavaVersionEnum.JAVA1_0_VALUE;
        Enum result1 = ValuedEnum.getEnum(enumClass1, value1);
        assertNotNull(result1);
        assertEquals("Java 1.0", result1.getName());

        // Test with an existing enumClass and an invalid value
        Class enumClass2 = JavaVersionEnum.class;
        int value2 = 999;
        Enum result2 = ValuedEnum.getEnum(enumClass2, value2);
        assertNull(result2);

        // Test with a null enumClass and an arbitrary value
        Class enumClass3 = null;
        int value3 = 123;
        try {
            ValuedEnum.getEnum(enumClass3, value3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Exception expected
        }
    }

    @Test
    public void testGetValue() {
        // Test with different instances of ValuedEnum
        ValuedEnum enum1 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, enum1.getValue());

        ValuedEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, enum2.getValue());

        ValuedEnum enum3 = JavaVersionEnum.JAVA1_2;
        assertEquals(120, enum3.getValue());

        ValuedEnum enum4 = JavaVersionEnum.JAVA1_3;
        assertEquals(130, enum4.getValue());
    }

    @Test
    public void testCompareTo() {
        // Test with two ValuedEnum objects having different values
        ValuedEnum enum1 = JavaVersionEnum.JAVA1_0;
        ValuedEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertTrue(enum1.compareTo(enum2) < 0);

        // Test with two ValuedEnum objects having the same value
        ValuedEnum enum3 = JavaVersionEnum.JAVA1_0;
        ValuedEnum enum4 = JavaVersionEnum.JAVA1_0;
        assertEquals(0, enum3.compareTo(enum4));

        // Test with two ValuedEnum objects having different types/class loaders
        ValuedEnum enum5 = JavaVersionEnum.JAVA1_0;
        Object other = new Object();
        try {
            enum5.compareTo(other);
            fail("Expected ClassCastException");
        } catch (ClassCastException ex) {
            // Exception expected
        }
    }

    @Test
    public void testToString() {
        // Test with different instances of ValuedEnum
        ValuedEnum enum1 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());

        ValuedEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertEquals("JavaVersion[Java 1.1=110]", enum2.toString());

        ValuedEnum enum3 = JavaVersionEnum.JAVA1_2;
        assertEquals("JavaVersion[Java 1.2=120]", enum3.toString());

        ValuedEnum enum4 = JavaVersionEnum.JAVA1_3;
        assertEquals("JavaVersion[Java 1.3=130]", enum4.toString());
    }

}

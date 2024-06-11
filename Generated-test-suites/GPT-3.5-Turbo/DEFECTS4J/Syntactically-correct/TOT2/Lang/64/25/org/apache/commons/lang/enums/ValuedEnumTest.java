import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    // Test case for ValuedEnum constructor
    @Test
    public void testValuedEnumConstructor() {
        ValuedEnum valuedEnum = new JavaVersionEnum("TestEnum", 200);
        assertEquals("TestEnum", valuedEnum.getName());
        assertEquals(200, valuedEnum.getValue());
    }

    // Test case for getEnum method
    @Test
    public void testGetEnum() {
        ValuedEnum valuedEnum = JavaVersionEnum.getEnum(100);
        assertNotNull(valuedEnum);
        assertEquals("Java 1.0", valuedEnum.getName());
    }

    // Test case for getValue method
    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_1;
        assertEquals(110, javaVersionEnum.getValue());
    }

    // Test case for compareTo method
    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.JAVA1_3;
        assertTrue(javaVersionEnum1.compareTo(javaVersionEnum2) < 0);
    }

    // Test case for toString method
    @Test
    public void testToString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersionEnum[Java 1.0=100]", javaVersionEnum.toString());
    }
}
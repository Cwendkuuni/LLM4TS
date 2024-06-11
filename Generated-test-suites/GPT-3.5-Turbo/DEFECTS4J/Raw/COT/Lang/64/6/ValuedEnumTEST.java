
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        // Create an instance of ValuedEnum to test
        ValuedEnum valuedEnum = new JavaVersionEnum("Java 1.0", 100);
        
        // Test the getValue() method
        int value = valuedEnum.getValue();
        assertEquals(100, value);
    }

    @Test
    public void testGetEnum() {
        // Test the getEnum() method
        
        // Test with an existing value
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum(100);
        assertNotNull(enum1);
        
        // Test with a non-existing value
        JavaVersionEnum enum2 = JavaVersionEnum.getEnum(200);
        assertNull(enum2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnum_NullClass() {
        // Test the getEnum() method with a null enumClass argument
        ValuedEnum.getEnum(null, 100);
    }
    
    @Test
    public void testCompareTo() {
        // Create two instances of ValuedEnum to test
        ValuedEnum valuedEnum1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum valuedEnum2 = new JavaVersionEnum("Java 1.1", 110);
        
        // Test the compareTo() method
        int result = valuedEnum1.compareTo(valuedEnum2);
        assertTrue(result < 0);
    }
}

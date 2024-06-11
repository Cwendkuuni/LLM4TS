import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {

    // Test for constructor
    @Test
    public void testConstructor() {
        ValuedEnum enumItem = new JavaVersionEnum("Test Enum", 200);
        
        // Verify that the name and value are set correctly
        assertEquals("Test Enum", enumItem.getName());
        assertEquals(200, enumItem.getValue());
    }

    // Test for getEnum
    @Test
    public void testGetEnum() {
        ValuedEnum enumItem = JavaVersionEnum.getEnum(100);
        
        // Verify that the correct enum item is returned
        assertEquals(JavaVersionEnum.JAVA1_0, enumItem);
        
        // Test for non-existent enum item
        enumItem = JavaVersionEnum.getEnum(999);
        
        // Verify that null is returned for non-existent item
        assertNull(enumItem);
    }

    // Test for getValue
    @Test
    public void testGetValue() {
        ValuedEnum enumItem = JavaVersionEnum.getEnum(100);
        
        // Verify that the correct value is returned
        assertEquals(100, enumItem.getValue());
    }

    // Test for compareTo
    @Test
    public void testCompareTo() {
        ValuedEnum enumItem1 = JavaVersionEnum.getEnum(100);
        ValuedEnum enumItem2 = JavaVersionEnum.getEnum(110);
        ValuedEnum enumItem3 = JavaVersionEnum.getEnum(120);
        
        // Verify that enumItem1 is less than enumItem2
        assertTrue(enumItem1.compareTo(enumItem2) < 0);
        
        // Verify that enumItem2 is greater than enumItem1
        assertTrue(enumItem2.compareTo(enumItem1) > 0);
        
        // Verify that enumItem2 is equal to enumItem3
        assertTrue(enumItem2.compareTo(enumItem3) == 0);
    }

    // Test for toString
    @Test
    public void testToString() {
        ValuedEnum enumItem = JavaVersionEnum.getEnum(100);
        
        // Verify that the correct string representation is returned
        assertEquals("JavaVersion[Java 1.0=100]", enumItem.toString());
    }
}
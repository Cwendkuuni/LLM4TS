import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        // Test for valid value
        String expectedName = "Java 1.0";
        int expectedValue = 100;
        ValuedEnum enumObj = JavaVersionEnum.getEnum(expectedValue);
        Assert.assertNotNull(enumObj);
        Assert.assertEquals(expectedName, enumObj.getName());
        Assert.assertEquals(expectedValue, enumObj.getValue());
        
        // Test for invalid value
        int invalidValue = 999;
        enumObj = JavaVersionEnum.getEnum(invalidValue);
        Assert.assertNull(enumObj);
    }

    @Test
    public void testGetValue() {
        // Test for valid enum object
        int expectedValue = 100;
        ValuedEnum enumObj = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(expectedValue, enumObj.getValue());
        
        // Test for invalid enum object
        expectedValue = 0;
        enumObj = new JavaVersionEnum("Invalid Enum", 0);
        Assert.assertEquals(expectedValue, enumObj.getValue());
    }

    @Test
    public void testCompareTo() {
        // Test for equal enum objects
        ValuedEnum enumObj1 = JavaVersionEnum.JAVA1_0;
        ValuedEnum enumObj2 = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(0, enumObj1.compareTo(enumObj2));
        
        // Test for less than enum objects
        enumObj1 = JavaVersionEnum.JAVA1_0;
        enumObj2 = JavaVersionEnum.JAVA1_1;
        Assert.assertEquals(-10, enumObj1.compareTo(enumObj2));
        
        // Test for greater than enum objects
        enumObj1 = JavaVersionEnum.JAVA1_1;
        enumObj2 = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(10, enumObj1.compareTo(enumObj2));
    }

    @Test
    public void testToString() {
        // Test for valid enum object
        String expectedToString = "JavaVersionEnum[Java 1.0=100]";
        ValuedEnum enumObj = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(expectedToString, enumObj.toString());
        
        // Test for invalid enum object
        expectedToString = "JavaVersionEnum[Invalid Enum=0]";
        enumObj = new JavaVersionEnum("Invalid Enum", 0);
        Assert.assertEquals(expectedToString, enumObj.toString());
    }
    
}
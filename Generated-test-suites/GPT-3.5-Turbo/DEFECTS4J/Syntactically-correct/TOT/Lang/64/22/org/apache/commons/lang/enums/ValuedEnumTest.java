
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        assertNull(ValuedEnum.getEnum(null, 100)); // Test with null enum class
        assertNull(ValuedEnum.getEnum(ValuedEnum.class, 200)); // Test with nonexistent value
        assertNotNull(ValuedEnum.getEnum(JavaVersionEnum.class, 100)); // Test with valid value
    }

    @Test
    public void testGetValue() {
        JavaVersionEnum versionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, versionEnum.getValue()); // Test with a valid value
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum version1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum version2 = JavaVersionEnum.JAVA1_1;
        assertTrue(version1.compareTo(version2) < 0); // Test with a lower version
        assertTrue(version1.compareTo(version1) == 0); // Test with the same version
        assertTrue(version2.compareTo(version1) > 0); // Test with a higher version
    }

    @Test
    public void testToString() {
        JavaVersionEnum versionEnum = JavaVersionEnum.JAVA1_0;
        String expectedToString = "JavaVersionEnum[Java 1.0=100]";
        assertEquals(expectedToString, versionEnum.toString()); // Test the toString method
    }
}


import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    // Test for ValuedEnum constructor and getValue method
    @Test
    public void testValuedEnumConstructor() {
        JavaVersionEnum javaVersion = new JavaVersionEnum("Java 1.0", 100);
        assertNotNull(javaVersion);
        assertEquals(100, javaVersion.getValue());
    }

    // Test for getEnum method when the enum exists
    @Test
    public void testGetEnumExisting() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(100);
        assertNotNull(javaVersion);
        assertEquals("Java 1.0", javaVersion.getName());
    }

    // Test for getEnum method when the enum doesn't exist
    @Test
    public void testGetEnumNonExisting() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(200);
        assertNull(javaVersion);
    }

    // Test for getEnum method with null enum class
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullEnumClass() {
        JavaVersionEnum.getEnum(null, 100);
    }

    // Test for getValue method
    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_1;
        assertEquals(110, javaVersion.getValue());
    }

    // Test for compareTo method when the objects are equal
    @Test
    public void testCompareToEqual() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_0;
        assertEquals(0, javaVersion1.compareTo(javaVersion2));
    }

    // Test for compareTo method when the calling object is less than the parameter
    @Test
    public void testCompareToLessThan() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
    }

    // Test for compareTo method when the calling object is greater than the parameter
    @Test
    public void testCompareToGreaterThan() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_0;
        assertTrue(javaVersion1.compareTo(javaVersion2) > 0);
    }

    // Test for toString method
    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", javaVersion.toString());
    }

}
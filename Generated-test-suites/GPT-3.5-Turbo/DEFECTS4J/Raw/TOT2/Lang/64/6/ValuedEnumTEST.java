import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnumByClassAndValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(100);
        assertNotNull(javaVersion);
        assertEquals("Java 1.0", javaVersion.getName());
        assertEquals(100, javaVersion.getValue());
    }
    
    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_1;
        assertEquals(110, javaVersion.getValue());
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
        assertTrue(javaVersion2.compareTo(javaVersion1) > 0);
        assertEquals(0, javaVersion1.compareTo(javaVersion1));
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_2;
        assertEquals("JavaVersion[Java 1.2=120]", javaVersion.toString());
    }
}
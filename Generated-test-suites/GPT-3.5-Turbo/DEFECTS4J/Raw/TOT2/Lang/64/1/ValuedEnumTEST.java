import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {
    
    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersion.getValue());
    }
    
    @Test
    public void testGetEnumByValue() {
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.getEnum(100));
        assertNull(JavaVersionEnum.getEnum(200));
    }
    
    @Test
    public void testEquals() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_0;
        
        assertTrue(javaVersion1.equals(javaVersion2));
        assertFalse(javaVersion1.equals(JavaVersionEnum.JAVA1_1));
        assertFalse(javaVersion1.equals(null));
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        
        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
        assertTrue(javaVersion2.compareTo(javaVersion1) > 0);
        assertEquals(0, javaVersion1.compareTo(JavaVersionEnum.JAVA1_0));
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[JAVA1_0=100]", javaVersion.toString());
    }
}
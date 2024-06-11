import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java10.getValue());
        
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, java11.getValue());
        
        JavaVersionEnum java12 = JavaVersionEnum.JAVA1_2;
        assertEquals(120, java12.getValue());
        
        JavaVersionEnum java13 = JavaVersionEnum.JAVA1_3;
        assertEquals(130, java13.getValue());
    }
    
    @Test
    public void testGetEnum() {
        JavaVersionEnum java10 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(100, java10.getValue());
        
        JavaVersionEnum java11 = JavaVersionEnum.getEnum(110);
        assertEquals("Java 1.1", java11.getName());
        
        JavaVersionEnum java15 = JavaVersionEnum.getEnum("Java 1.5");
        assertNull(java15);
        
        JavaVersionEnum java09 = JavaVersionEnum.getEnum(90);
        assertNull(java09);
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        
        assertTrue(java10.compareTo(java11) < 0);
        assertTrue(java11.compareTo(java10) > 0);
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", java10.toString());
        
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        assertEquals("JavaVersion[Java 1.1=110]", java11.toString());
    }
}
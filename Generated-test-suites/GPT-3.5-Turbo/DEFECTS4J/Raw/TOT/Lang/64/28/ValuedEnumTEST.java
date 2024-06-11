import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {
    
    @Test
    public void testConstructor() {
        JavaVersionEnum java1_0 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
    }
    
    @Test
    public void testGetEnumWithString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
        
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum("Java 1.2");
        assertNull(java1_2);
        
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum("Java 1.3");
        assertEquals("Java 1.3", java1_3.getName());
        assertEquals(130, java1_3.getValue());
    }
    
    @Test
    public void testGetEnumWithInt() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
        
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());
        
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(125);
        assertNull(java1_3);
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        
        assertTrue(java1_0.compareTo(java1_1) < 0);
        assertTrue(java1_1.compareTo(java1_2) < 0);
        assertTrue(java1_2.compareTo(java1_0) > 0);
        assertTrue(java1_0.compareTo(java1_0) == 0);
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
        
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
    }
}
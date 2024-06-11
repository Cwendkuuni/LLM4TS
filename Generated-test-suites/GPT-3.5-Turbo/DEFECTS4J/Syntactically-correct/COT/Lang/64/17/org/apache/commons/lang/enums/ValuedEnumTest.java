
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {
    
    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java1_0.getValue());
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        
        assertTrue(java1_0.compareTo(java1_1) < 0);
        assertTrue(java1_1.compareTo(java1_0) > 0);
        assertTrue(java1_0.compareTo(java1_0) == 0);
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
    }
}

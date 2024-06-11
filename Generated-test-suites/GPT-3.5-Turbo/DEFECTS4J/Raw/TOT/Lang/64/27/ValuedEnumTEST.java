import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ValuedEnumTest {
    
    private JavaVersionEnum javaVersionEnum;
    
    @Before
    public void setUp() {
        javaVersionEnum = new JavaVersionEnum("Java 1.0", 100);
    }
    
    @Test
    public void testGetValue() {
        assertEquals(100, javaVersionEnum.getValue());
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersionEnum1 = new JavaVersionEnum("Java 1.1", 110);
        assertEquals(-10, javaVersionEnum.compareTo(javaVersionEnum1));
    }
    
    @Test
    public void testToString() {
        assertEquals("JavaVersionEnum[Java 1.0=100]", javaVersionEnum.toString());
    }
    
    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.getEnum(100);
        assertEquals(javaVersionEnum, javaVersionEnum1);
        
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.getEnum(200);
        assertNull(javaVersionEnum2);
    }
}
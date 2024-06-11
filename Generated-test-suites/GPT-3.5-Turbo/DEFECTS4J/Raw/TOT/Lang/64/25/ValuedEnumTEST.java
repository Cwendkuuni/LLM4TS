import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    private JavaVersionEnum javaVersionEnum;
    
    @Before
    public void setup() {
        javaVersionEnum = new JavaVersionEnum("Java 1.0", 100);
    }
    
    @Test
    public void testGetName() {
        assertEquals("Java 1.0", javaVersionEnum.getName());
    }
    
    @Test
    public void testGetValue() {
        assertEquals(100, javaVersionEnum.getValue());
    }
    
    @Test
    public void testEquals() {
        JavaVersionEnum javaVersionEnum1 = new JavaVersionEnum("Java 1.0", 100);
        JavaVersionEnum javaVersionEnum2 = new JavaVersionEnum("Java 1.1", 110);
        
        assertTrue(javaVersionEnum.equals(javaVersionEnum1));
        assertFalse(javaVersionEnum.equals(javaVersionEnum2));
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersionEnum1 = new JavaVersionEnum("Java 1.1", 110);
        
        assertTrue(javaVersionEnum.compareTo(javaVersionEnum1) < 0);
    }
    
    @Test
    public void testToString() {
        assertEquals("JavaVersionEnum[Java 1.0=100]", javaVersionEnum.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 100);
    }
    
    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersionEnum1 = new JavaVersionEnum("Java 1.0", 100);
        JavaVersionEnum javaVersionEnum2 = new JavaVersionEnum("Java 1.1", 110);
        
        assertEquals(javaVersionEnum1, ValuedEnum.getEnum(JavaVersionEnum.class, 100));
        assertEquals(null, ValuedEnum.getEnum(JavaVersionEnum.class, 120));
        assertEquals(javaVersionEnum2, ValuedEnum.getEnum(JavaVersionEnum.class, 110));
    }
}
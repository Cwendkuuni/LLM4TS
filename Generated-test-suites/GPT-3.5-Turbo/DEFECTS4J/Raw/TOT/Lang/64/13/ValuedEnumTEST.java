import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    private JavaVersionEnum java1_0;
    private JavaVersionEnum java1_1;
    private JavaVersionEnum java1_2;
    private JavaVersionEnum java1_3;
    
    @Before
    public void setup() {
        java1_0 = JavaVersionEnum.JAVA1_0;
        java1_1 = JavaVersionEnum.JAVA1_1;
        java1_2 = JavaVersionEnum.JAVA1_2;
        java1_3 = JavaVersionEnum.JAVA1_3;
    }

    @Test
    public void testGetValue() {
        assertEquals(100, java1_0.getValue());
        assertEquals(110, java1_1.getValue());
        assertEquals(120, java1_2.getValue());
        assertEquals(130, java1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        assertTrue(java1_0.compareTo(java1_1) < 0);
        assertEquals(0, java1_1.compareTo(java1_1));
        assertTrue(java1_2.compareTo(java1_1) > 0);
    }

    @Test
    public void testToString() {
        assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
        assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
        assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
        assertEquals("JavaVersionEnum[Java 1.3=130]", java1_3.toString());
    }
    
    @Test
    public void testGetEnum() {
        assertEquals(java1_0, JavaVersionEnum.getEnum("Java 1.0"));
        assertEquals(java1_1, JavaVersionEnum.getEnum("Java 1.1"));
        assertEquals(java1_2, JavaVersionEnum.getEnum("Java 1.2"));
        assertEquals(java1_3, JavaVersionEnum.getEnum("Java 1.3"));

        assertEquals(java1_0, JavaVersionEnum.getEnum(100));
        assertEquals(java1_1, JavaVersionEnum.getEnum(110));
        assertEquals(java1_2, JavaVersionEnum.getEnum(120));
        assertEquals(java1_3, JavaVersionEnum.getEnum(130));
        
        assertNull(JavaVersionEnum.getEnum("Invalid Java Version"));
        assertNull(JavaVersionEnum.getEnum(-1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        JavaVersionEnum.getEnum(null, 100);
    }
    
    @Test
    public void testGetEnumMap() {
        assertNotNull(JavaVersionEnum.getEnumMap());
        assertEquals(4, JavaVersionEnum.getEnumMap().size());
    }
    
    @Test
    public void testGetEnumList() {
        assertNotNull(JavaVersionEnum.getEnumList());
        assertEquals(4, JavaVersionEnum.getEnumList().size());
    }
    
    @Test
    public void testIterator() {
        assertNotNull(JavaVersionEnum.iterator());
    }
}
import static org.junit.Assert.*;

import org.junit.Test;

public class ValuedEnumTest {
    
    @Test
    public void testGetName() {
        JavaVersionEnum javaEnum = JavaVersionEnum.JAVA1_0;
        assertEquals("Java 1.0", javaEnum.getName());
    }
    
    @Test
    public void testGetValue() {
        JavaVersionEnum javaEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaEnum.getValue());
    }
    
    @Test
    public void testGetEnum_String() {
        JavaVersionEnum javaEnum = JavaVersionEnum.getEnum("Java 1.0");
        assertNotNull(javaEnum);
        assertEquals("Java 1.0", javaEnum.getName());
    }
    
    @Test
    public void testGetEnum_Int() {
        JavaVersionEnum javaEnum = JavaVersionEnum.getEnum(100);
        assertNotNull(javaEnum);
        assertEquals(100, javaEnum.getValue());
    }
    
    @Test
    public void testGetEnumMap() {
        assertEquals(4, JavaVersionEnum.getEnumMap().size());
    }
    
    @Test
    public void testGetEnumList() {
        assertEquals(4, JavaVersionEnum.getEnumList().size());
    }
    
    @Test
    public void testIterator() {
        assertNotNull(JavaVersionEnum.iterator());
    }
    
    @Test
    public void testCompareTo_SameType() {
        JavaVersionEnum java1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java2 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java3 = JavaVersionEnum.JAVA1_1;
        
        assertEquals(0, java1.compareTo(java2));
        assertTrue(java1.compareTo(java3) < 0);
        assertTrue(java3.compareTo(java1) > 0);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareTo_DifferentType() {
        JavaVersionEnum javaEnum = JavaVersionEnum.JAVA1_0;
        Object obj = new Object();
        
        javaEnum.compareTo(obj);
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum javaEnum = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersionEnum[Java 1.0=100]", javaEnum.toString());
    }
}
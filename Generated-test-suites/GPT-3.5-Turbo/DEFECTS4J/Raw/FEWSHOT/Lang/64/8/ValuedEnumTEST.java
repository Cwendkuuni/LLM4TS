import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetValue() {
        // Test for positive value
        JavaVersionEnum version1 = JavaVersionEnum.getEnum(100);
        assertEquals(100, version1.getValue());
        
        // Test for negative value
        JavaVersionEnum version2 = JavaVersionEnum.getEnum(-100);
        assertEquals(-100, version2.getValue());
    }

    @Test
    public void testCompareTo() {
        // Test for equal values
        JavaVersionEnum version1 = JavaVersionEnum.getEnum(100);
        JavaVersionEnum version2 = JavaVersionEnum.getEnum(100);
        assertEquals(0, version1.compareTo(version2));
        
        // Test for greater value
        JavaVersionEnum version3 = JavaVersionEnum.getEnum(110);
        assertEquals(-1, version1.compareTo(version3));
        
        // Test for lesser value
        JavaVersionEnum version4 = JavaVersionEnum.getEnum(90);
        assertEquals(1, version1.compareTo(version4));
    }

    @Test
    public void testToString() {
        JavaVersionEnum version = JavaVersionEnum.getEnum(100);
        assertEquals("JavaVersion[Java 1.0=100]", version.toString());
    }
}
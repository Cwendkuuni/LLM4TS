import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(100);
        assertNotNull(javaVersion);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersion);
        
        JavaVersionEnum invalidJavaVersion = JavaVersionEnum.getEnum(999);
        assertNull(invalidJavaVersion);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", javaVersion.toString());
    }
}
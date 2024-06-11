import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersionEnum.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.getEnum("Java 1.0");
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.getEnum(100);
        
        assertNotNull(javaVersionEnum1);
        assertNotNull(javaVersionEnum2);
        
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersionEnum1);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersionEnum2);
    }

    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        assertNotNull(enumMap);
        assertTrue(enumMap.containsKey("Java 1.0"));
        assertTrue(enumMap.containsValue(JavaVersionEnum.JAVA1_0));
    }

    @Test
    public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        assertNotNull(enumList);
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_0));
    }

    @Test
    public void testIterator() {
        Iterator iterator = JavaVersionEnum.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersionEnum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersionEnum2 = JavaVersionEnum.JAVA1_1;
        
        assertTrue(javaVersionEnum1.compareTo(javaVersionEnum2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", javaVersionEnum.toString());
    }
}

package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        assertEquals(100, JavaVersionEnum.JAVA1_0.getValue());
        assertEquals(110, JavaVersionEnum.JAVA1_1.getValue());
        assertEquals(120, JavaVersionEnum.JAVA1_2.getValue());
        assertEquals(130, JavaVersionEnum.JAVA1_3.getValue());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_0));
        assertEquals(-1, JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_1));
        assertEquals(1, JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_0));
    }

    @Test
    public void testGetEnum() {
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.getEnum("Java 1.0"));
        assertEquals(JavaVersionEnum.JAVA1_1, JavaVersionEnum.getEnum("Java 1.1"));
        assertEquals(JavaVersionEnum.JAVA1_2, JavaVersionEnum.getEnum("Java 1.2"));
        assertEquals(JavaVersionEnum.JAVA1_3, JavaVersionEnum.getEnum("Java 1.3"));
        
        assertNull(JavaVersionEnum.getEnum("Java 1.4"));
    }
    
    @Test
    public void testGetEnumWithValue() {
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.getEnum(100));
        assertEquals(JavaVersionEnum.JAVA1_1, JavaVersionEnum.getEnum(110));
        assertEquals(JavaVersionEnum.JAVA1_2, JavaVersionEnum.getEnum(120));
        assertEquals(JavaVersionEnum.JAVA1_3, JavaVersionEnum.getEnum(130));
        
        assertNull(JavaVersionEnum.getEnum(140));
    }
    
    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        assertEquals(JavaVersionEnum.JAVA1_0, enumMap.get("Java 1.0"));
        assertEquals(JavaVersionEnum.JAVA1_1, enumMap.get("Java 1.1"));
        assertEquals(JavaVersionEnum.JAVA1_2, enumMap.get("Java 1.2"));
        assertEquals(JavaVersionEnum.JAVA1_3, enumMap.get("Java 1.3"));
    }
    
    @Test
    public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        assertEquals(4, enumList.size());
        assertEquals(JavaVersionEnum.JAVA1_0, enumList.get(0));
        assertEquals(JavaVersionEnum.JAVA1_1, enumList.get(1));
        assertEquals(JavaVersionEnum.JAVA1_2, enumList.get(2));
        assertEquals(JavaVersionEnum.JAVA1_3, enumList.get(3));
    }
    
    @Test
    public void testIterator() {
        int count = 0;
        for (Object obj: JavaVersionEnum.iterator()) {
            count++;
        }
        assertEquals(4, count);
    }
}

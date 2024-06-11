package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, java1_0.getValue());
        
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, java1_1.getValue());
        
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals(120, java1_2.getValue());
        
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals(130, java1_3.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum java1_0 = (JavaVersionEnum) JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, java1_0);
        
        JavaVersionEnum java1_1 = (JavaVersionEnum) JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, java1_1);
        
        JavaVersionEnum java1_2 = (JavaVersionEnum) JavaVersionEnum.getEnum(120);
        assertEquals(JavaVersionEnum.JAVA1_2, java1_2);
        
        JavaVersionEnum java1_3 = (JavaVersionEnum) JavaVersionEnum.getEnum(130);
        assertEquals(JavaVersionEnum.JAVA1_3, java1_3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNullClass() {
        JavaVersionEnum.getEnum(null, 100);
    }

    @Test
    public void testGetEnumByNonexistentValue() {
        JavaVersionEnum java1_0 = (JavaVersionEnum) JavaVersionEnum.getEnum(200);
        assertNull(java1_0);
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
        
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals("JavaVersion[Java 1.1=110]", java1_1.toString());
        
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals("JavaVersion[Java 1.2=120]", java1_2.toString());
        
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals("JavaVersion[Java 1.3=130]", java1_3.toString());
    }
    
    //Additional test cases for other methods can be added here
    
    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        assertEquals(4, enumMap.size());
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
        Iterator iterator = JavaVersionEnum.iterator();
        assertEquals(JavaVersionEnum.JAVA1_0, iterator.next());
        assertEquals(JavaVersionEnum.JAVA1_1, iterator.next());
        assertEquals(JavaVersionEnum.JAVA1_2, iterator.next());
        assertEquals(JavaVersionEnum.JAVA1_3, iterator.next());
    }
}
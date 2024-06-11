package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = JavaVersionEnum.getEnum(100);
        assertEquals("Java 1.0", enum2.getName());
    }
    
    @Test
    public void testGetValue() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
        assertEquals(100, enum1.getValue());
        
        JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertEquals(110, enum2.getValue());
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum enum3 = JavaVersionEnum.JAVA1_2;
        
        assertEquals(-10, enum1.compareTo(enum2));
        assertEquals(10, enum2.compareTo(enum1));
        assertEquals(10, enum2.compareTo(enum3));
        assertEquals(-10, enum3.compareTo(enum2));
        assertEquals(0, enum1.compareTo(enum1));
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());
        
        JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_1;
        assertEquals("JavaVersion[Java 1.1=110]", enum2.toString());
    }
}
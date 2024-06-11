package org.apache.commons.lang.enums;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValuedEnumTest {
    
    @Test
    public void testGetName() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("Java 1.0", java1_0.getName());
        
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals("Java 1.1", java1_1.getName());
        
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals("Java 1.2", java1_2.getName());
        
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals("Java 1.3", java1_3.getName());
    }
    
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
    public void testGetEnum() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, java1_0);
        
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, java1_1);
        
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        assertEquals(JavaVersionEnum.JAVA1_2, java1_2);
        
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
        assertEquals(JavaVersionEnum.JAVA1_3, java1_3);
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
}
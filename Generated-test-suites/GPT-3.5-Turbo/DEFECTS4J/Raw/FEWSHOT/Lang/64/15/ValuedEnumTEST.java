
package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testGetEnum() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(JavaVersionEnum.JAVA1_0, java1_0);
        
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum("Java 1.1");
        assertEquals(JavaVersionEnum.JAVA1_1, java1_1);
        
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum("Java 1.2");
        assertEquals(JavaVersionEnum.JAVA1_2, java1_2);
        
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum("Java 1.3");
        assertEquals(JavaVersionEnum.JAVA1_3, java1_3);
        
        JavaVersionEnum java1_4 = JavaVersionEnum.getEnum("Java 1.4");
        assertNull(java1_4);
    }
    
    @Test
    public void testGetEnumWithValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, java1_0);
        
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        assertEquals(JavaVersionEnum.JAVA1_1, java1_1);
        
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        assertEquals(JavaVersionEnum.JAVA1_2, java1_2);
        
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
        assertEquals(JavaVersionEnum.JAVA1_3, java1_3);
        
        JavaVersionEnum java1_4 = JavaVersionEnum.getEnum(140);
        assertNull(java1_4);
    }
}

package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testConstructor() {
        JavaVersionEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
        
        JavaVersionEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
        
        JavaVersionEnum enum3 = new JavaVersionEnum("Java 1.2", 120);
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());
        
        JavaVersionEnum enum4 = new JavaVersionEnum("Java 1.3", 130);
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
    }
    
    @Test
    public void testGetEnum() {
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
        
        JavaVersionEnum enum2 = JavaVersionEnum.getEnum("Java 1.1");
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
        
        JavaVersionEnum enum3 = JavaVersionEnum.getEnum(120);
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());
        
        JavaVersionEnum enum4 = JavaVersionEnum.getEnum(130);
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
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
        int count = 0;
        for (JavaVersionEnum enumItem : JavaVersionEnum.iterator()) {
            count++;
        }
        assertEquals(4, count);
    }
}
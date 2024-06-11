
package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testConstructor() {
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
    }

    @Test
    public void testGetEnum() {
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 200));
        
        ValuedEnum enum2 = ValuedEnum.getEnum(JavaVersionEnum.class, 100);
        
        assertSame(JavaVersionEnum.JAVA1_0, enum2);
        assertEquals("Java 1.0", enum2.getName());
        assertEquals(100, enum2.getValue());
    }

    @Test
    public void testGetValue() {
        ValuedEnum enum3 = JavaVersionEnum.getEnum(100);
        
        assertEquals(100, enum3.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum4 = JavaVersionEnum.getEnum(100);
        ValuedEnum enum5 = JavaVersionEnum.getEnum(110);
        ValuedEnum enum6 = JavaVersionEnum.getEnum(120);
        
        assertEquals(-10, enum4.compareTo(enum5));
        assertEquals(10, enum5.compareTo(enum4));
        assertEquals(0, enum4.compareTo(enum4));
        assertEquals(-20, enum4.compareTo(enum6));
        assertEquals(20, enum6.compareTo(enum4));
    }

    @Test
    public void testToString() {
        ValuedEnum enum7 = new JavaVersionEnum("Java 1.0", 100);
        
        assertEquals("JavaVersionEnum[Java 1.0=100]", enum7.toString());
    }
}

package org.apache.commons.lang.enums;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum(100);
        assertEquals(100, enum1.getValue());

        JavaVersionEnum enum2 = JavaVersionEnum.getEnum(120);
        assertEquals(120, enum2.getValue());

        JavaVersionEnum enum3 = JavaVersionEnum.getEnum(130);
        assertEquals(130, enum3.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum(100);
        JavaVersionEnum enum2 = JavaVersionEnum.getEnum(120);
        JavaVersionEnum enum3 = JavaVersionEnum.getEnum(130);
        
        // enum1 is less than enum2
        assertTrue(enum1.compareTo(enum2) < 0);
        
        // enum3 is greater than enum1
        assertTrue(enum3.compareTo(enum1) > 0);
        
        // enum2 is equal to enum2
        assertTrue(enum2.compareTo(enum2) == 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum(100);
        assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());

        JavaVersionEnum enum2 = JavaVersionEnum.getEnum(120);
        assertEquals("JavaVersion[Java 1.2=120]", enum2.toString());

        JavaVersionEnum enum3 = JavaVersionEnum.getEnum(130);
        assertEquals("JavaVersion[Java 1.3=130]", enum3.toString());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
        assertNotNull(enum1);
        assertEquals(100, enum1.getValue());
        
        JavaVersionEnum enum2 = JavaVersionEnum.getEnum("Java 1.2");
        assertNotNull(enum2);
        assertEquals(120, enum2.getValue());
        
        JavaVersionEnum enum3 = JavaVersionEnum.getEnum("Java 1.3");
        assertNotNull(enum3);
        assertEquals(130, enum3.getValue());
        
        JavaVersionEnum enum4 = JavaVersionEnum.getEnum("Invalid Java Version");
        assertNull(enum4);
    }
}
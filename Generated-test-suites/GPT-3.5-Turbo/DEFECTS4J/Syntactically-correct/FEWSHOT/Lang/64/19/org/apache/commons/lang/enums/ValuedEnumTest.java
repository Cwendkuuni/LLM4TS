package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValuedEnumTest {
    @Test
    public void testConstructorAndGetValue() {
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
        assertEquals(110, enum2.getValue());

        ValuedEnum enum3 = new JavaVersionEnum("Java 1.2", 120);
        assertEquals(120, enum3.getValue());

        ValuedEnum enum4 = new JavaVersionEnum("Java 1.3", 130);
        assertEquals(130, enum4.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());

        ValuedEnum enum2 = JavaVersionEnum.getEnum("Java 1.1");
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());

        ValuedEnum enum3 = JavaVersionEnum.getEnum("Java 1.2");
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());

        ValuedEnum enum4 = JavaVersionEnum.getEnum("Java 1.3");
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum(100);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());

        ValuedEnum enum2 = JavaVersionEnum.getEnum(110);
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());

        ValuedEnum enum3 = JavaVersionEnum.getEnum(120);
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());

        ValuedEnum enum4 = JavaVersionEnum.getEnum(130);
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
        
        ValuedEnum enum5 = JavaVersionEnum.getEnum(140);
        assertNull(enum5);
    }

    @Test
    public void testGetEnumMap() {
        assertTrue(JavaVersionEnum.getEnumMap().containsKey("Java 1.0"));
        assertTrue(JavaVersionEnum.getEnumMap().containsKey("Java 1.1"));
        assertTrue(JavaVersionEnum.getEnumMap().containsKey("Java 1.2"));
        assertTrue(JavaVersionEnum.getEnumMap().containsKey("Java 1.3"));
    }

    @Test
    public void testGetEnumList() {
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_0));
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_1));
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_2));
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_3));
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Object enumValue : JavaVersionEnum.iterator()) {
            count++;
        }
        assertEquals(4, count);
    }
}
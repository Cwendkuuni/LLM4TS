package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());

        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
        assertNotNull(enum1);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());

        ValuedEnum enum2 = JavaVersionEnum.getEnum(110);
        assertNotNull(enum2);
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());

        ValuedEnum enum3 = JavaVersionEnum.getEnum("Unknown");
        assertNull(enum3);

        ValuedEnum enum4 = JavaVersionEnum.getEnum(200);
        assertNull(enum4);
    }

    @Test
    public void testGetEnumMap() {
        assertNotNull(JavaVersionEnum.getEnumMap());
        assertTrue(JavaVersionEnum.getEnumMap().containsKey("Java 1.0"));
        assertTrue(JavaVersionEnum.getEnumMap().containsKey("Java 1.1"));
    }

    @Test
    public void testGetEnumList() {
        assertNotNull(JavaVersionEnum.getEnumList());
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_0));
        assertTrue(JavaVersionEnum.getEnumList().contains(JavaVersionEnum.JAVA1_1));
    }

    @Test
    public void testIterator() {
        assertTrue(JavaVersionEnum.iterator().hasNext());
        assertEquals(JavaVersionEnum.JAVA1_0, JavaVersionEnum.iterator().next());
    }
}
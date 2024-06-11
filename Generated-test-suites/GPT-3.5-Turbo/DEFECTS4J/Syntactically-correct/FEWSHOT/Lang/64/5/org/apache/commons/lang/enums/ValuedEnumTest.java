package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_1;
        assertEquals(110, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_2;
        assertEquals(120, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.JAVA1_3;
        assertEquals(130, javaVersionEnum.getValue());
    }

    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum("Java 1.0");
        assertNotNull(javaVersionEnum);
        assertEquals(100, javaVersionEnum.getValue());

        javaVersionEnum = JavaVersionEnum.getEnum(100);
        assertNotNull(javaVersionEnum);
        assertEquals("Java 1.0", javaVersionEnum.getName());

        javaVersionEnum = JavaVersionEnum.getEnum("Java 1.4");
        assertNull(javaVersionEnum);

        javaVersionEnum = JavaVersionEnum.getEnum(140);
        assertNull(javaVersionEnum);
    }

    @Test
    public void testGetEnumMap() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum("Java 1.3");
        assertNotNull(javaVersionEnum);
        assertEquals(130, javaVersionEnum.getValue());

        java.util.Map enumMap = JavaVersionEnum.getEnumMap();
        assertNotNull(enumMap);
        assertTrue(enumMap.containsKey("Java 1.0"));
        assertTrue(enumMap.containsKey("Java 1.1"));
        assertTrue(enumMap.containsKey("Java 1.2"));
        assertTrue(enumMap.containsKey("Java 1.3"));
    }

    @Test
    public void testGetEnumList() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum("Java 1.3");
        assertNotNull(javaVersionEnum);
        assertEquals(130, javaVersionEnum.getValue());

        List enumList = JavaVersionEnum.getEnumList();
        assertNotNull(enumList);
        assertEquals(4, enumList.size());
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_0));
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_1));
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_2));
        assertTrue(enumList.contains(JavaVersionEnum.JAVA1_3));
    }

    @Test
    public void testIterator() {
        Iterator iterator = JavaVersionEnum.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(JavaVersionEnum.JAVA1_0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(JavaVersionEnum.JAVA1_1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(JavaVersionEnum.JAVA1_2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(JavaVersionEnum.JAVA1_3, iterator.next());
    }

    @Test
    public void testCompareTo() {
        int result = JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_1);
        assertTrue(result < 0);

        result = JavaVersionEnum.JAVA1_3.compareTo(JavaVersionEnum.JAVA1_2);
        assertTrue(result > 0);

        result = JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_1);
        assertEquals(0, result);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClass() {
        StringEnum stringEnum = StringEnum.STRING1;
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        javaVersionEnum.compareTo(stringEnum);
    }

    @Test
    public void testToString() {
        String value = JavaVersionEnum.JAVA1_0.toString();
        assertNotNull(value);
        assertEquals("JavaVersionEnum[Java 1.0=100]", value);

        value = JavaVersionEnum.JAVA1_3.toString();
        assertNotNull(value);
        assertEquals("JavaVersionEnum[Java 1.3=130]", value);
    }

}
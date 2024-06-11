package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = ValuedEnumTest.getEnum(ValuedEnum.class, "JavaVersion", 100);
        assertNotNull(enum1);
        assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());

        ValuedEnum enum2 = ValuedEnumTest.getEnum(ValuedEnum.class, "JavaVersion", 110);
        assertNotNull(enum2);
        assertEquals("JavaVersion[Java 1.1=110]", enum2.toString());

        ValuedEnum enum3 = ValuedEnumTest.getEnum(ValuedEnum.class, "JavaVersion", 120);
        assertNotNull(enum3);
        assertEquals("JavaVersion[Java 1.2=120]", enum3.toString());

        ValuedEnum enum4 = ValuedEnumTest.getEnum(ValuedEnum.class, "JavaVersion", 130);
        assertNotNull(enum4);
        assertEquals("JavaVersion[Java 1.3=130]", enum4.toString());

        ValuedEnum enum5 = ValuedEnumTest.getEnum(ValuedEnum.class, "JavaVersion", 140);
        assertNull(enum5);
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = ValuedEnumTest.getEnumList(ValuedEnum.class);
        assertEquals(4, enumList.size());
        assertSame(ValuedEnum.JAVA1_0, enumList.get(0));
        assertSame(ValuedEnum.JAVA1_1, enumList.get(1));
        assertSame(ValuedEnum.JAVA1_2, enumList.get(2));
        assertSame(ValuedEnum.JAVA1_3, enumList.get(3));
    }

    @Test
    public void testGetEnumMap() {
        Map<Integer, ValuedEnum> enumMap = ValuedEnumTest.getEnumMap(ValuedEnum.class);
        assertEquals(4, enumMap.size());
        assertEquals(ValuedEnum.JAVA1_0, enumMap.get(100));
        assertEquals(ValuedEnum.JAVA1_1, enumMap.get(110));
        assertEquals(ValuedEnum.JAVA1_2, enumMap.get(120));
        assertEquals(ValuedEnum.JAVA1_3, enumMap.get(130));
    }

    @Test
    public void testIterator() {
        Iterator<ValuedEnum> iterator = ValuedEnumTest.iterator(ValuedEnum.class);
        assertEquals(4, iterator.nextCount());
        assertSame(ValuedEnum.JAVA1_0, iterator.next());
        assertSame(ValuedEnum.JAVA1_1, iterator.next());
        assertSame(ValuedEnum.JAVA1_2, iterator.next());
        assertSame(ValuedEnum.JAVA1_3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    private static ValuedEnum getEnum(Class<? extends ValuedEnum> enumClass, String name, int value) {
        try {
            Method method = enumClass.getMethod("getEnum", new Class[] { String.class, int.class });
            return (ValuedEnum) method.invoke(null, name, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<ValuedEnum> getEnumList(Class<? extends ValuedEnum> enumClass) {
        try {
            Method method = enumClass.getMethod("getEnumList", new Class[0]);
            return (List<ValuedEnum>) method.invoke(null, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Integer, ValuedEnum> getEnumMap(Class<? extends ValuedEnum> enumClass) {
        try {
            Method method = enumClass.getMethod("getEnumMap", new Class[0]);
            return (Map<Integer, ValuedEnum>) method.invoke(null, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Iterator<ValuedEnum> iterator(Class<? extends ValuedEnum> enumClass) {
        try {
            Method method = enumClass.getMethod("iterator", new Class[0]);
            return (Iterator<ValuedEnum>) method.invoke(null, new Object[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.EnumUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = ValuedEnumTest.DummyEnum.JAVA1_0.getEnum("Java 1.0");
        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_0, enum1);

        ValuedEnum enum2 = ValuedEnumTest.DummyEnum.JAVA1_1.getEnum(101);
        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_1, enum2);

        ValuedEnum enum3 = ValuedEnumTest.DummyEnum.getEnum(null);
        assertNull(enum3);

        ValuedEnum enum4 = ValuedEnumTest.DummyEnum.getEnum(120);
        assertNull(enum4);
    }

    @Test
    public void testGetEnumMap() {
        Map<ValuedEnum, Integer> map = ValuedEnumTest.DummyEnum.getEnumMap();
        assertNotNull(map);

        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_0, map.get(ValuedEnumTest.DummyEnum.JAVA1_0));
        assertEquals(100, map.get(ValuedEnumTest.DummyEnum.JAVA1_0).intValue());

        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_1, map.get(ValuedEnumTest.DummyEnum.JAVA1_1));
        assertEquals(101, map.get(ValuedEnumTest.DummyEnum.JAVA1_1).intValue());

        assertNull(map.get(ValuedEnumTest.DummyEnum.JAVA1_2));
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> list = ValuedEnumTest.DummyEnum.getEnumList();
        assertNotNull(list);

        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_0, list.get(0));
        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_1, list.get(1));
        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_2, list.get(2));
    }

    @Test
    public void testIterator() {
        Iterator<ValuedEnum> iterator = ValuedEnumTest.DummyEnum.iterator();
        assertNotNull(iterator);

        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_0, iterator.next());
        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_1, iterator.next());
        assertEquals(ValuedEnumTest.DummyEnum.JAVA1_2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, ValuedEnumTest.DummyEnum.JAVA1_0.compareTo(ValuedEnumTest.DummyEnum.JAVA1_0));
        assertEquals(-1, ValuedEnumTest.DummyEnum.JAVA1_0.compareTo(ValuedEnumTest.DummyEnum.JAVA1_1));
        assertEquals(1, ValuedEnumTest.DummyEnum.JAVA1_1.compareTo(ValuedEnumTest.DummyEnum.JAVA1_0));

        try {
            ValuedEnumTest.DummyEnum notExistingEnum = ValuedEnumTest.DummyEnum.getEnum("notExistingEnum");
            ValuedEnumTest.DummyEnum dummyEnum = ValuedEnumTest.DummyEnum.JAVA1_0;
            int comparisonResult = dummyEnum.compareTo(notExistingEnum);
            assertEquals(-1, comparisonResult);
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        assertEquals("JavaVersion[Java 1.0=100]", ValuedEnumTest.DummyEnum.JAVA1_0.toString());
        assertEquals("JavaVersion[Java 1.1=101]", ValuedEnumTest.DummyEnum.JAVA1_1.toString());
        assertEquals("JavaVersion[Java 1.2=102]", ValuedEnumTest.DummyEnum.JAVA1_2.toString());
    }

    @Test
    public void testGetValue() {
        assertEquals(100, ValuedEnumTest.DummyEnum.JAVA1_0.getValue());
        assertEquals(101, ValuedEnumTest.DummyEnum.JAVA1_1.getValue());
        assertEquals(102, ValuedEnumTest.DummyEnum.JAVA1_2.getValue());
    }

    @Test
    public void testGetName() {
        assertEquals("Java 1.0", ValuedEnumTest.DummyEnum.JAVA1_0.getName());
        assertEquals("Java 1.1", ValuedEnumTest.DummyEnum.JAVA1_1.getName());
        assertEquals("Java 1.2", ValuedEnumTest.DummyEnum.JAVA1_2.getName());
    }

    static class DummyEnum extends ValuedEnum {
        public static final int JAVA1_0_VALUE = 100;
        public static final int JAVA1_1_VALUE = 101;
        public static final int JAVA1_2_VALUE = 102;

        public static final ValuedEnum JAVA1_0 = new DummyEnum("Java 1.0", JAVA1_0_VALUE);
        public static final ValuedEnum JAVA1_1 = new DummyEnum("Java 1.1", JAVA1_1_VALUE);
        public static final ValuedEnum JAVA1_2 = new DummyEnum("Java 1.2", JAVA1_2_VALUE);

        private DummyEnum(String name, int value) {
            super(name, value);
        }

        @Override
        public String getName() {
            return super.getName();
        }

        @Override
        public int getValue() {
            return super.getValue();
        }

        public static ValuedEnum getEnum(String javaVersion) {
            return EnumUtils.getEnum(ValuedEnumTest.DummyEnum.class, javaVersion);
        }

        public static ValuedEnum getEnum(int javaVersion) {
            return EnumUtils.getEnum(ValuedEnumTest.DummyEnum.class, javaVersion);
        }

        public static Map getEnumMap() {
            return EnumUtils.getEnumMap(ValuedEnumTest.DummyEnum.class);
        }

        public static List getEnumList() {
            return EnumUtils.getEnumList(ValuedEnumTest.DummyEnum.class);
        }

        public static Iterator iterator() {
            return EnumUtils.iterator(ValuedEnumTest.DummyEnum.class);
        }

        private static final long serialVersionUID = 1L;
        private static Method iToString;

        static {
            try {
                iToString = DummyEnum.class.getDeclaredField("iToString").getDeclaringClass().getMethod("toString");
                iToString.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException | NoSuchMethodException e) {
                // expected
            }
        }
    }
}
package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.annotation.NotNull;
import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;

/**
 * JUnit 4 test file for ValuedEnum class.
 */
public class ValuedEnumTest {

    /**
     * Test method for {@link ValuedEnum#getValue()} and {@link ValuedEnum#compareTo(Object)}.
     */
    @Test
    public void testGetValueAndCompareTo() {
        EnumTestObject enum1 = new EnumTestObject("Enum1", 10);
        EnumTestObject enum2 = new EnumTestObject("Enum2", 20);
        EnumTestObject enum3 = new EnumTestObject("Enum3", 10);

        assertEquals(10, enum1.getValue());
        assertEquals(10, enum3.getValue());
        assertEquals(0, enum1.compareTo(enum3));
        assertEquals(-10, enum3.compareTo(enum1));
        assertEquals(10, enum1.compareTo(enum2));
    }

    /**
     * Test method for {@link ValuedEnum#getEnum(Class, int)} and {@link ValuedEnum#getEnum(Class, String)}.
     */
    @Test
    public void testGetEnum() {
        EnumTestObject enum1 = new EnumTestObject("Enum1", 10);
        EnumTestObject enum2 = new EnumTestObject("Enum2", 20);
        EnumTestObject enum3 = new EnumTestObject("Enum3", 10);

        EnumTestObject result1 = (EnumTestObject) ValuedEnum.getEnum(EnumTestObject.class, 10);
        EnumTestObject result2 = (EnumTestObject) ValuedEnum.getEnum(EnumTestObject.class, "Enum1");

        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(enum1, result1);
        assertEquals(enum1, result2);

        assertNull(ValuedEnum.getEnum(EnumTestObject.class, 30));
        assertNull(ValuedEnum.getEnum(EnumTestObject.class, "NonExistentEnum"));
    }

    /**
     * Test method for {@link ValuedEnum#getEnumMap()}.
     */
    @Test
    public void testGetEnumMap() {
        EnumTestObject enum1 = new EnumTestObject("Enum1", 10);
        EnumTestObject enum2 = new EnumTestObject("Enum2", 20);
        EnumTestObject enum3 = new EnumTestObject("Enum3", 10);

        List<EnumTestObject> enumList = EnumTestObject.values();
        Map<Integer, EnumTestObject> enumMap = ValuedEnum.getEnumMap(EnumTestObject.class);

        assertEquals(3, enumList.size());
        assertEquals(3, enumMap.size());
        assertEquals(enum1, enumMap.get(10));
        assertEquals(enum2, enumMap.get(20));
        assertEquals(enum3, enumMap.get(10));
    }

    /**
     * Test method for {@link ValuedEnum#getEnumList()}.
     */
    @Test
    public void testGetEnumList() {
        EnumTestObject enum1 = new EnumTestObject("Enum1", 10);
        EnumTestObject enum2 = new EnumTestObject("Enum2", 20);
        EnumTestObject enum3 = new EnumTestObject("Enum3", 10);

        List<EnumTestObject> enumList = ValuedEnum.getEnumList(EnumTestObject.class);

        assertEquals(3, enumList.size());
        assertEquals(enum1, enumList.get(0));
        assertEquals(enum2, enumList.get(1));
        assertEquals(enum3, enumList.get(2));
    }

    /**
     * Test method for {@link ValuedEnum#iterator()}.
     */
    @Test
    public void testIterator() {
        EnumTestObject enum1 = new EnumTestObject("Enum1", 10);
        EnumTestObject enum2 = new EnumTestObject("Enum2", 20);
        EnumTestObject enum3 = new EnumTestObject("Enum3", 10);

        Iterator<EnumTestObject> iterator = ValuedEnum.iterator(EnumTestObject.class);

        assertEquals(3, iterator.nextCount());
        assertEquals(enum1, iterator.next());
        assertEquals(enum2, iterator.next());
        assertEquals(enum3, iterator.next());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test case for an Enum class.
     */
    @SuppressWarnings("unused")
    static class EnumTestObject extends ValuedEnum {
        private static final long serialVersionUID = 1L;

        EnumTestObject(String name, int value) {
            super(name, value);
        }

        @NotNull
        public static List<EnumTestObject> values() {
            return ValuedEnum.asList(EnumTestObject.class);
        }

        @NotNull
        public static Map<Integer, EnumTestObject> getEnumMap() {
            return ValuedEnum.getEnumMap(EnumTestObject.class);
        }

        @NotNull
        public static List<EnumTestObject> getEnumList() {
            return ValuedEnum.getEnumList(EnumTestObject.class);
        }

        @NotNull
        public static Iterator<EnumTestObject> iterator() {
            return ValuedEnum.iterator(EnumTestObject.class);
        }
    }
}
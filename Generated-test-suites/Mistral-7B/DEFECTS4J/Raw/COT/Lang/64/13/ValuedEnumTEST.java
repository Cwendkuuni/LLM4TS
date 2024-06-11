package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.Enum;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        ValuedEnum javaVersion1_0_enum = ValuedEnum.getEnum(enumClass, "JavaVersion1_0");
        assertEquals(javaVersion1_0, javaVersion1_0_enum);

        ValuedEnum javaVersion1_1_enum = ValuedEnum.getEnum(enumClass, "JavaVersion1_1");
        assertEquals(javaVersion1_1, javaVersion1_1_enum);

        ValuedEnum nullEnum = ValuedEnum.getEnum(enumClass, null);
        assertNull(nullEnum);

        ValuedEnum unknownEnum = ValuedEnum.getEnum(enumClass, "UnknownEnum");
        assertNull(unknownEnum);
    }

    @Test
    public void testGetEnumWithValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        ValuedEnum javaVersion1_0_enum = ValuedEnum.getEnum(enumClass, 100);
        assertEquals(javaVersion1_0, javaVersion1_0_enum);

        ValuedEnum javaVersion1_1_enum = ValuedEnum.getEnum(enumClass, 110);
        assertEquals(javaVersion1_1, javaVersion1_1_enum);

        ValuedEnum nullEnum = ValuedEnum.getEnum(enumClass, -1);
        assertNull(nullEnum);

        ValuedEnum unknownEnum = ValuedEnum.getEnum(enumClass, 999);
        assertNull(unknownEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullEnumClass() {
        ValuedEnum.getEnum(null, "JavaVersion1_0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNegativeValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        ValuedEnum javaVersion1_0_enum = ValuedEnum.getEnum(enumClass, -100);
    }

    @Test
    public void testGetEnumMap() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        @SuppressWarnings("unchecked")
        Map<String, ValuedEnum> enumMap = (Map<String, ValuedEnum>) ValuedEnum.getEnumMap(enumClass);
        assertEquals(2, enumMap.size());
        assertEquals(javaVersion1_0, enumMap.get("JavaVersion1_0"));
        assertEquals(javaVersion1_1, enumMap.get("JavaVersion1_1"));
    }

    @Test
    public void testGetEnumList() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        @SuppressWarnings("unchecked")
        List<ValuedEnum> enumList = (List<ValuedEnum>) ValuedEnum.getEnumList(enumClass);
        assertEquals(2, enumList.size());
        assertEquals(javaVersion1_0, enumList.get(0));
        assertEquals(javaVersion1_1, enumList.get(1));
    }

    @Test
    public void testIterator() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100) {
        };
        ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110) {
        };

        @SuppressWarnings("unchecked")
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        assertEquals(2, iterator.size());
        assertEquals(javaVersion1_0, iterator.next());
        assertEquals(javaVersion1_1, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class ValuedEnumTest {
    private static final Class enumClass = ValuedEnum.class;
    private static List<ValuedEnum> enumList;

    @BeforeClass
    public static void setup() {
        enumList = Arrays.asList(new ValuedEnum("Enum1", 1), new ValuedEnum("Enum2", 2), new ValuedEnum("Enum3", 3));
    }

    @Test
    public void testGetEnum() {
        for (ValuedEnum value : enumList) {
            ValuedEnum enumValue = (ValuedEnum) ValuedEnum.getEnum(enumClass, value.getValue());
            assertEquals(value, enumValue);
        }

        assertNull(ValuedEnum.getEnum(enumClass, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullValue() {
        ValuedEnum.getEnum(enumClass, null);
    }

    @Test
    public void testGetEnumWithValueLessThanMinimum() {
        assertEquals(null, ValuedEnum.getEnum(enumClass, Integer.MIN_VALUE));
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum2", 2);
        ValuedEnum enum3 = new ValuedEnum("Enum3", 3);

        assertEquals(0, enum1.compareTo(enum1));
        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(1, enum2.compareTo(enum1));
        assertEquals(0, enum1.compareTo(enum1));
        assertEquals(1, enum2.compareTo(enum2));
        assertEquals(-1, enum3.compareTo(enum2));
        assertEquals(1, enum2.compareTo(enum3));

        ValuedEnum enum4 = new ValuedEnum("Enum4", 10);
        assertEquals(-3, enum1.compareTo(enum4));
        assertEquals(-2, enum2.compareTo(enum4));
        assertEquals(-1, enum3.compareTo(enum4));
        assertEquals(0, enum4.compareTo(enum4));
        assertEquals(3, enum1.compareTo(enum4));
        assertEquals(2, enum2.compareTo(enum4));
        assertEquals(1, enum3.compareTo(enum4));
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum2", 2);
        ValuedEnum enum3 = new ValuedEnum("Enum3", 3);

        assertEquals("ValuedEnum[Enum1=1]", enum1.toString());
        assertEquals("ValuedEnum[Enum2=2]", enum2.toString());
        assertEquals("ValuedEnum[Enum3=3]", enum3.toString());

        ValuedEnum enum4 = new ValuedEnum("Enum4", 10);
        assertEquals("ValuedEnum[Enum4=10]", enum4.toString());
    }

    @Test
    public void testGetValue() {
        for (ValuedEnum value : enumList) {
            assertEquals(value.getValue(), value.getValue());
        }
    }

    @Test
    public void testGetValueWithNullEnum() {
        assertNull(new ValuedEnum(null, 1).getValue());
    }

    @Test
    public void testGetValueWithNullValue() {
        assertNull(new ValuedEnum("", 0).getValue());
    }

    @Test
    public void testGetValueWithNegativeValue() {
        assertNull(new ValuedEnum("Enum", Integer.MIN_VALUE).getValue());
    }

    @Test
    public void testGetEnumMap() {
        assertEquals(Arrays.asList(new ValuedEnum("Enum1", 1), new ValuedEnum("Enum2", 2), new ValuedEnum("Enum3", 3)),
                ValuedEnum.getEnumMap(enumClass));
    }

    @Test
    public void testGetEnumMapWithNullClass() {
        assertNull(ValuedEnum.getEnumMap(null));
    }

    @Test
    public void testGetEnumList() {
        assertEquals(Arrays.asList(new ValuedEnum("Enum1", 1), new ValuedEnum("Enum2", 2), new ValuedEnum("Enum3", 3)),
                ValuedEnum.getEnumList(enumClass));
    }

    @Test
    public void testGetEnumListWithNullClass() {
        assertNull(ValuedEnum.getEnumList(null));
    }

    @Test
    public void testIterator() {
        Iterator iterator = ValuedEnum.iterator(enumClass);
        int count = 0;
        while (iterator.hasNext()) {
            ValuedEnum enumValue = (ValuedEnum) iterator.next();
            assertEquals(enumValue.getValue(), ++count);
        }
    }

    @Test
    public void testIteratorWithNullClass() {
        assertNull(ValuedEnum.iterator(null));
    }

    @Test
    public void testEquals() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum3 = new ValuedEnum("Enum2", 2);
        assertEquals(enum1, enum1);
        assertEquals(enum1, enum1);
        assertEquals(enum1, enum1);
        assertEquals(enum1, enum2);
        assertNotEquals(enum1, enum3);
    }

    @Test
    public void testEqualsWithNull() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        assertNotEquals(enum1, null);
    }

    @Test
    public void testEqualsWithDifferentType() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        Object obj = "DifferentType";
        assertNotEquals(enum1, obj);
    }

    @Test
    public void testHashCode() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum3 = new ValuedEnum("Enum2", 2);
        assertEquals(hashCode(enum1), hashCode(enum1));
        assertEquals(hashCode(enum1), hashCode(enum2));
        assertNotEquals(hashCode(enum1), hashCode(enum3));
    }

    @Test
    public void testHashCodeWithNull() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        assertNull(enum1.hashCode());
    }

    private int hashCode(ValuedEnum obj) {
        try {
            Method method = ValuedEnum.class.getDeclaredMethod("hashCode");
            method.setAccessible(true);
            return (Integer) method.invoke(obj);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
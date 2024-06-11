package org.apache.commons.lang.enums;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class ValuedEnumTest {

    private Class<ValuedEnum> enumClass;
    private String enumName;
    private int value;
    private ValuedEnum expectedEnum;

    public ValuedEnumTest(Class<ValuedEnum> enumClass, String enumName, int value, ValuedEnum expectedEnum) {
        this.enumClass = enumClass;
        this.enumName = enumName;
        this.value = value;
        this.expectedEnum = expectedEnum;
    }

    @Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ValuedEnum.class, "JavaVersion", 100, JavaVersionEnum.JAVA1_0},
                {ValuedEnum.class, "JavaVersion", 110, JavaVersionEnum.JAVA1_1},
                {ValuedEnum.class, "JavaVersion", 120, JavaVersionEnum.JAVA1_2},
                {ValuedEnum.class, "JavaVersion", 130, JavaVersionEnum.JAVA1_3}
        });
    }

    @Test
    public void testGetEnumByName() throws Exception {
        ValuedEnum actualEnum = ValuedEnum.getEnum(enumClass, enumName);
        assertEquals("Expected enum \"" + enumName + "\" with value " + value + " to equal " + expectedEnum, expectedEnum, actualEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNameNullEnumClass() {
        ValuedEnum.getEnum(null, enumName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNameNullName() {
        ValuedEnum.getEnum(enumClass, null);
    }

    @Test
    public void testGetEnumByValue() throws Exception {
        ValuedEnum actualEnum = ValuedEnum.getEnum(enumClass, value);
        assertEquals("Expected enum with value " + value + " to equal " + expectedEnum, expectedEnum, actualEnum);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumByValueNull() {
        ValuedEnum.getEnum(enumClass, null);
    }

    @Test
    public void testGetEnumMap() throws Exception {
        Map<ValuedEnum, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);
        ValuedEnum actualEnum = enumMap.get(expectedEnum);
        assertEquals("Expected enum in map to equal " + expectedEnum, expectedEnum, actualEnum);
    }

    @Test
    public void testGetEnumList() throws Exception {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        for (ValuedEnum enumElement : enumList) {
            if (enumElement.equals(expectedEnum)) {
                assertEquals("Expected enum in list to equal " + expectedEnum, expectedEnum, enumElement);
                return;
            }
        }
        fail("Expected enum \"" + enumName + "\" with value " + value + " to be in list");
    }

    @Test
    public void testIterator() throws Exception {
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        boolean found = false;
        while (iterator.hasNext() && !found) {
            ValuedEnum next = iterator.next();
            if (next.equals(expectedEnum)) {
                found = true;
                assertEquals("Expected enum in iterator to equal " + expectedEnum, expectedEnum, next);
            }
        }
        if (!found) {
            fail("Expected enum \"" + enumName + "\" with value " + value + " to be in iterator");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapNull() {
        ValuedEnum.getEnumMap(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumListNull() {
        ValuedEnum.getEnumList(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorUnsupportedOperationException() throws Exception {
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        iterator.remove();
    }

    @Test
    public void testCompareTo() throws Exception {
        int comparison = expectedEnum.compareTo(this.expectedEnum);
        assertEquals("Expected comparison to be zero: " + comparison, 0, comparison);

        int comparisonWithLower = expectedEnum.compareTo(new ValuedEnum("LowerEnum", -1));
        assertEquals("Expected comparison to be negative: " + comparisonWithLower, -1, comparisonWithLower);

        int comparisonWithHigher = expectedEnum.compareTo(new ValuedEnum("HigherEnum", Integer.MAX_VALUE));
        assertEquals("Expected comparison to be positive: " + comparisonWithHigher, 1, comparisonWithHigher);
    }
}
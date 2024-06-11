package org.apache.commons.lang.enums;

import org.apache.commons.lang.ClassUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Parameterized.class)
public class ValuedEnumTest {

    private Class<ValuedEnum> enumClass;
    private String name;
    private int value;

    public ValuedEnumTest(Class<ValuedEnum> enumClass, String name, int value) {
        this.enumClass = enumClass;
        this.name = name;
        this.value = value;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_0", 100},
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_1", 110},
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_2", 120},
                {ValuedEnum.class, "JavaVersionEnum$JAVA1_3", 130}
        });
    }

    @Test
    public void testGetEnum() {
        ValuedEnum enumInstance = getEnumByName(name);
        assertEquals("Expected name to match", name, enumInstance.getName());
        assertEquals("Expected value to match", value, enumInstance.getValue());
    }

    @Test
    public void testGetEnumNull() {
        assertNull("Expected null for unknown enum", getEnumByName("UnknownEnum"));
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum enumInstance = getEnumByValue(value);
        assertEquals("Expected name to match", name, enumInstance.getName());
        assertEquals("Expected value to match", value, enumInstance.getValue());
    }

    @Test
    public void testGetEnumByValueNull() {
        assertNull("Expected null for unknown enum by value", getEnumByValue(Integer.MAX_VALUE));
    }

    @Test
    public void testGetEnumMap() {
        Map<ValuedEnum, ValuedEnum> enumMap = ValuedEnum.getEnumMap();
        ValuedEnum enumInstance = enumMap.get(getEnumByName(name));
        assertEquals("Expected name to match", name, enumInstance.getName());
        assertEquals("Expected value to match", value, enumInstance.getValue());
    }

    @Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList();
        for (ValuedEnum enumInstance : enumList) {
            if (enumInstance.getName().equals(name)) {
                assertEquals("Expected value to match", value, enumInstance.getValue());
                break;
            }
        }
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enumInstance = getEnumByName(name);
        ValuedEnum other = getEnumByName("JavaVersionEnum$JAVA1_1");
        int comparisonResult = enumInstance.compareTo(other);
        if (value < other.getValue()) {
            assertEquals("Expected less than", -1, comparisonResult);
        } else if (value > other.getValue()) {
            assertEquals("Expected greater than", 1, comparisonResult);
        } else {
            assertEquals("Expected equal", 0, comparisonResult);
        }
    }

    private ValuedEnum getEnumByName(String name) {
        try {
            return (ValuedEnum) ClassUtils.getClassLoader(enumClass).loadClass(
                    enumClass.getName() + "." + name).getDeclaredConstructor(String.class, int.class)
                    .newInstance(name, getValueByEnumName(name));
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            throw new RuntimeException("Failed to create enum instance", e);
        }
    }

    private int getValueByEnumName(String name) {
        try {
            Method getValueMethod = ValuedEnum.class.getDeclaredMethod("getValue");
            getValueMethod.setAccessible(true);
            return ((Integer) getValueMethod.invoke(getEnumByName(name))).intValue();
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new RuntimeException("Failed to get value from enum instance", e);
        }
    }

    private ValuedEnum getEnumByValue(int value) {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList();
        for (ValuedEnum enumInstance : enumList) {
            if (enumInstance.getValue() == value) {
                return enumInstance;
            }
        }
        return null;
    }
}
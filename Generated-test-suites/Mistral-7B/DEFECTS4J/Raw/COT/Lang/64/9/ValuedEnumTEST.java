package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    // Test case for constructor ValuedEnum(String name, int value)
    @org.junit.Test
    public void testValuedEnumConstructor() {
        String name = "TestEnum";
        int value = 10;
        ValuedEnum enumInstance = new ValuedEnum(name, value) {
        };
        assertEquals(name, enumInstance.getName());
        assertEquals(value, enumInstance.getValue());
    }

    // Test case for method getEnum(Class enumClass, int value)
    @org.junit.Test
    public void testGetEnumInt() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum expectedEnum = new ValuedEnum("ExpectedEnum", 5) {
        };
        ValuedEnum returnedEnum = (ValuedEnum) ValuedEnum.getEnum(enumClass, 5);
        assertNotNull(returnedEnum);
        assertEquals(expectedEnum.getName(), returnedEnum.getName());
        assertEquals(expectedEnum.getValue(), returnedEnum.getValue());
    }

    // Test case for method getEnum(Class enumClass, String name)
    @org.junit.Test
    public void testGetEnumString() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum expectedEnum = new ValuedEnum("ExpectedEnum", 5) {
        };
        ValuedEnum returnedEnum = (ValuedEnum) ValuedEnum.getEnum(enumClass, "ExpectedEnum");
        assertNotNull(returnedEnum);
        assertEquals(expectedEnum.getName(), returnedEnum.getName());
        assertEquals(expectedEnum.getValue(), returnedEnum.getValue());
    }

    // Test case for method getEnumList()
    @org.junit.Test
    public void testGetEnumList() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        assertFalse(enumList.isEmpty());
    }

    // Test case for method getEnumMap()
    @org.junit.Test
    public void testGetEnumMap() {
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(ValuedEnum.class);
        assertFalse(enumMap.isEmpty());
    }

    // Test case for method iterator()
    @org.junit.Test
    public void testIterator() {
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(ValuedEnum.class);
        assertFalse(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    // Test case for method compareTo(Object other)
    @org.junit.Test(expected = ClassCastException.class)
    public void testCompareToNull() {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum1", 1) {
        };
        Object other = new Object();
        enumInstance.compareTo(other);
    }

    // Test case for method compareTo(Object other) with same class
    @org.junit.Test
    public void testCompareToSameClass() {
        ValuedEnum enumInstance1 = new ValuedEnum("TestEnum1", 1) {
        };
        ValuedEnum enumInstance2 = new ValuedEnum("TestEnum1", 1) {
        };
        int comparisonResult = enumInstance1.compareTo(enumInstance2);
        assertEquals(0, comparisonResult);
    }

    // Test case for method compareTo(Object other) with different classes
    @org.junit.Test
    public void testCompareToDifferentClasses() {
        ValuedEnum enumInstance1 = new ValuedEnum("TestEnum1", 1) {
        };
        ValuedEnum2 enumInstance2 = new ValuedEnum2("TestEnum2", 2) {
        };
        int comparisonResult = enumInstance1.compareTo(enumInstance2);
        assertTrue(comparisonResult < 0);
    }

    // Test case for method toString()
    @org.junit.Test
    public void testToString() {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 123) {
        };
        String expectedString = "TestEnum[name=TestEnum, value=123]";
        String stringResult = enumInstance.toString();
        assertEquals(expectedString, stringResult);
    }

    // Test case for method getValue()
    @org.junit.Test
    public void testGetValue() {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 123) {
        };
        int expectedValue = 123;
        int valueResult = enumInstance.getValue();
        assertEquals(expectedValue, valueResult);
    }

    // Test case for method getName()
    @org.junit.Test
    public void testGetName() {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 123) {
        };
        String expectedName = "TestEnum";
        String nameResult = enumInstance.getName();
        assertEquals(expectedName, nameResult);
    }

    // Test case for method getEnumClass()
    @org.junit.Test
    public void testGetEnumClass() {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 123) {
        };
        Class<ValuedEnum> expectedEnumClass = ValuedEnum.class;
        Class<?> enumClassResult = enumInstance.getEnumClass();
        assertEquals(expectedEnumClass, enumClassResult);
    }

    // Test case for method equals(Object obj)
    @org.junit.Test
    public void testEqualsObject() {
        ValuedEnum enumInstance1 = new ValuedEnum("TestEnum1", 1) {
        };
        ValuedEnum enumInstance2 = new ValuedEnum("TestEnum1", 1) {
        };
        assertTrue(enumInstance1.equals(enumInstance2));
    }

    // Test case for method equals(Object obj) with different objects
    @org.junit.Test
    public void testEqualsObjectDifferent() {
        ValuedEnum enumInstance1 = new ValuedEnum("TestEnum1", 1) {
        };
        Object otherInstance = new Object();
        assertFalse(enumInstance1.equals(otherInstance));
    }

    // Test case for method hashCode()
    @org.junit.Test
    public void testHashCode() {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 1) {
        };
        int expectedHashCode = 1;
        int hashCodeResult = enumInstance.hashCode();
        assertEquals(expectedHashCode, hashCodeResult);
    }

    // Test case for method hashCode() with different values
    @org.junit.Test
    public void testHashCodeDifferent() {
        ValuedEnum enumInstance1 = new ValuedEnum("TestEnum1", 1) {
        };
        ValuedEnum enumInstance2 = new ValuedEnum("TestEnum2", 2) {
        };
        int expectedHashCode1 = 1;
        int expectedHashCode2 = 2;
        int hashCodeResult1 = enumInstance1.hashCode();
        int hashCodeResult2 = enumInstance2.hashCode();
        assertEquals(expectedHashCode1, hashCodeResult1);
        assertEquals(expectedHashCode2, hashCodeResult2);
    }

    // Test case for method getDeclaredMethods()
    @org.junit.Test
    public void testGetDeclaredMethods() throws NoSuchMethodException, SecurityException {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method compareToMethod = enumClass.getDeclaredMethod("compareTo", Object.class);
        Method getValueMethod = enumClass.getDeclaredMethod("getValue");
        Method getNameMethod = enumClass.getDeclaredMethod("getName");
        Method getEnumClassMethod = enumClass.getDeclaredMethod("getEnumClass");
        Method equalsMethod = enumClass.getDeclaredMethod("equals", Object.class);
        Method hashCodeMethod = enumClass.getDeclaredMethod("hashCode");

        assertNotNull(compareToMethod);
        assertNotNull(getValueMethod);
        assertNotNull(getNameMethod);
        assertNotNull(getEnumClassMethod);
        assertNotNull(equalsMethod);
        assertNotNull(hashCodeMethod);
    }

    // Test case for exception handling: getEnum(Class enumClass, int value) with non-existing value
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumIntNullEnumClass() {
        int value = 123;
        ValuedEnum.getEnum(null, value);
    }

    // Test case for exception handling: getEnum(Class enumClass, int value) with non-existing value
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumIntInvalidValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum.getEnum(enumClass, 1000);
    }

    // Test case for exception handling: getEnum(Class enumClass, String name) with null name
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumStringNullName() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum.getEnum(enumClass, null);
    }

    // Test case for exception handling: getEnum(Class enumClass, String name) with non-existing name
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetEnumStringInvalidName() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum.getEnum(enumClass, "InvalidName");
    }

    // Test case for exception handling: getEnumList() with null enumClass
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetEnumListNullEnumClass() {
        ValuedEnum.getEnumList(null);
    }

    // Test case for exception handling: getEnumMap() with null enumClass
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetEnumMapNullEnumClass() {
        ValuedEnum.getEnumMap(null);
    }

    // Test case for exception handling: iterator() with null enumClass
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testIteratorNullEnumClass() {
        ValuedEnum.iterator(null);
    }

    // Test case for exception handling: getClass() with null obj
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetClassNullObj() {
        Object obj = null;
        obj.getClass();
    }
}

class ValuedEnum2 extends ValuedEnum {
    ValuedEnum2(String name, int value) {
        super(name, value);
    }
}
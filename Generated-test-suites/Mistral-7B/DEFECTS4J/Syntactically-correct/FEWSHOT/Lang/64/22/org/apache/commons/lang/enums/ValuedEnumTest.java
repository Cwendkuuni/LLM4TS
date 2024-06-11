package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enumClass = new TestValuedEnum("TestEnum", 10);
        TestValuedEnum enumValue1 = TestValuedEnum.getValueByInt(10);
        TestValuedEnum enumValue2 = TestValuedEnum.getValueByInt(20);

        TestValuedEnum result = (TestValuedEnum) ValuedEnum.getEnum(TestValuedEnum.class, 10);
        assertSame(enumValue1, result);

        result = (TestValuedEnum) ValuedEnum.getEnum(TestValuedEnum.class, 20);
        assertNull(result);

        result = (TestValuedEnum) ValuedEnum.getEnum(TestValuedEnum.class, "TestEnum2_20");
        assertSame(enumValue2, result);

        result = (TestValuedEnum) ValuedEnum.getEnum(TestValuedEnum.class, "TestEnum1_10");
        assertSame(enumValue1, result);

        result = (TestValuedEnum) ValuedEnum.getEnum(TestValuedEnum.class, "TestEnum3_30");
        assertNull(result);

        // Test with null class
        try {
            ValuedEnum.getEnum(null, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ValuedEnum.getEnum(TestValuedEnum.class, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum enumClass = new TestValuedEnum("TestEnum", 10);
        List<TestValuedEnum> enumList = Arrays.asList(enumClass.getEnumValues());

        Map<String, TestValuedEnum> enumMap = ValuedEnum.getEnumMap(TestValuedEnum.class);
        for (TestValuedEnum enumValue : enumList) {
            TestValuedEnum value = enumMap.get(enumValue.getName());
            assertEquals(enumValue, value);
        }

        try {
            ValuedEnum.getEnumMap(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum enumClass = new TestValuedEnum("TestEnum", 10);
        List<TestValuedEnum> enumList = Arrays.asList(enumClass.getEnumValues());

        List<TestValuedEnum> resultList = ValuedEnum.getEnumList(TestValuedEnum.class);
        assertSame(enumList, resultList);

        try {
            ValuedEnum.getEnumList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetEnumIterator() {
        ValuedEnum enumClass = new TestValuedEnum("TestEnum", 10);
        List<TestValuedEnum> enumList = Arrays.asList(enumClass.getEnumValues());

        Iterator<TestValuedEnum> resultIterator = ValuedEnum.iterator(TestValuedEnum.class);
        for (TestValuedEnum enumValue : enumList) {
            TestValuedEnum nextValue = resultIterator.next();
            assertEquals(enumValue, nextValue);
        }

        assertFalse(resultIterator.hasNext());

        try {
            ValuedEnum.iterator(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enumClass1 = new TestValuedEnum1("TestEnum1", 1);
        ValuedEnum enumClass2 = new TestValuedEnum2("TestEnum2", 2);

        int result = enumClass1.compareTo(enumClass1);
        assertEquals(0, result);

        result = enumClass1.compareTo(enumClass2);
        assertEquals(-1, result);

        result = enumClass2.compareTo(enumClass1);
        assertEquals(1, result);
    }

    @Test
    public void testToString() {
        ValuedEnum enumClass = new TestValuedEnum("TestEnum", 10);
        String expected = "TestEnum[name=TestEnum,value=10]";
        String result = enumClass.toString();
        assertEquals(expected, result);
    }

    private static class TestValuedEnum extends ValuedEnum {
        private static final long serialVersionUID = 1L;

        TestValuedEnum(String name, int value) {
            super(name, value);
        }

        @Override
        protected List<ValuedEnum> getEnumValues() {
            return Arrays.asList(new TestValuedEnum1(this.getName(), this.getValue() + 1), this,
                    new TestValuedEnum2(this.getName() + "_2", this.getValue() * 2));
        }

        static TestValuedEnum getValueByInt(int value) {
            TestValuedEnum[] values = TestValuedEnum.class.getEnumConstants();
            for (TestValuedEnum testValuedEnum : values) {
                if (testValuedEnum.getValue() == value) {
                    return testValuedEnum;
                }
            }
            return null;
        }
    }

    private static class TestValuedEnum1 extends TestValuedEnum {
        TestValuedEnum1(String name, int value) {
            super(name, value);
        }

        @Override
        public String getName() {
            return "TestEnum1";
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    private static class TestValuedEnum2 extends TestValuedEnum {
        TestValuedEnum2(String name, int value) {
            super(name, value);
        }

        @Override
        public String getName() {
            return "TestEnum2";
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}

class ValuedEnumUtils {
    static Method getCompareToMethod(Class<?> clazz) {
        try {
            return clazz.getMethod("compareTo", Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found: " + clazz.getName() + ".compareTo(Object)", e);
        }
    }

    static int invokeCompareToMethod(Object obj, Object comparedObj) {
        try {
            return ((Integer) MethodUtils.invokeMethod(obj, getCompareToMethod(obj.getClass()), comparedObj)).intValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error invoking compareTo method", e);
        }
    }
}
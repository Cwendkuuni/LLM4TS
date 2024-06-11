package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    public static class TestEnum extends ValuedEnum {
        public TestEnum(String name, int value) {
            super(name, value);
        }

        private static final long serialVersionUID = 1L;

        public int customCompareTo(Object other) {
            ValuedEnum otherEnum = (ValuedEnum) other;
            return super.compareTo(otherEnum) * 2;
        }
    }

    @Test
    public void testGetEnum() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        TestEnum testEnum1 = TestEnum.getEnum(testEnumClass, 1);
        TestEnum testEnum2 = TestEnum.getEnum(testEnumClass, 2);

        assertNotNull(testEnum1);
        assertNotNull(testEnum2);
        assertEquals(1, testEnum1.getValue());
        assertEquals(2, testEnum2.getValue());
    }

    @Test
    public void testGetName() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        TestEnum testEnum1 = TestEnum.getEnum(testEnumClass, 1);
        TestEnum testEnum2 = TestEnum.getEnum(testEnumClass, 2);

        assertEquals("TestEnum_1", testEnum1.getName());
        assertEquals("TestEnum_2", testEnum2.getName());
    }

    @Test
    public void testValues() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        ValuedEnum[] values = testEnumClass.getEnumConstants();

        List<ValuedEnum> list = Arrays.asList(values);
        Iterator<ValuedEnum> iterator = list.iterator();

        TestEnum testEnum1 = (TestEnum) iterator.next();
        TestEnum testEnum2 = (TestEnum) iterator.next();

        assertNotNull(testEnum1);
        assertNotNull(testEnum2);
        assertEquals(1, testEnum1.getValue());
        assertEquals(2, testEnum2.getValue());
    }

    @Test
    public void testCompareTo() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        TestEnum testEnum1 = TestEnum.getEnum(testEnumClass, 1);
        TestEnum testEnum2 = TestEnum.getEnum(testEnumClass, 2);

        assertEquals(-1, testEnum1.compareTo(testEnum2));
        assertEquals(1, testEnum2.compareTo(testEnum1));
        assertEquals(0, testEnum1.compareTo(testEnum1));

        // Test custom compareTo
        assertEquals(-4, testEnum1.customCompareTo(testEnum2));
        assertEquals(4, testEnum2.customCompareTo(testEnum1));
        assertEquals(0, testEnum1.customCompareTo(testEnum1));
    }

    @Test
    public void testToString() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        TestEnum testEnum1 = TestEnum.getEnum(testEnumClass, 1);
        TestEnum testEnum2 = TestEnum.getEnum(testEnumClass, 2);

        assertEquals("TestEnum[TestEnum_1=1]", testEnum1.toString());
        assertEquals("TestEnum[TestEnum_2=2]", testEnum2.toString());
    }

    @Test
    public void testGetEnumMap() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        Map<String, ValuedEnum> map = ValuedEnum.getEnumMap(testEnumClass);

        ValuedEnum testEnum1 = map.get("TestEnum_1");
        ValuedEnum testEnum2 = map.get("TestEnum_2");

        assertNotNull(testEnum1);
        assertNotNull(testEnum2);
        assertEquals(1, testEnum1.getValue());
        assertEquals(2, testEnum2.getValue());
    }

    @Test
    public void testGetEnumList() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        List<ValuedEnum> list = ValuedEnum.getEnumList(testEnumClass);

        ValuedEnum testEnum1 = list.get(0);
        ValuedEnum testEnum2 = list.get(1);

        assertNotNull(testEnum1);
        assertNotNull(testEnum2);
        assertEquals(1, testEnum1.getValue());
        assertEquals(2, testEnum2.getValue());
    }

    @Test
    public void testIterator() {
        Class<TestEnum> testEnumClass = TestEnum.class;

        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(testEnumClass);

        ValuedEnum testEnum1 = iterator.next();
        ValuedEnum testEnum2 = iterator.next();

        assertNotNull(testEnum1);
        assertNotNull(testEnum2);
        assertEquals(1, testEnum1.getValue());
        assertEquals(2, testEnum2.getValue());
    }
}
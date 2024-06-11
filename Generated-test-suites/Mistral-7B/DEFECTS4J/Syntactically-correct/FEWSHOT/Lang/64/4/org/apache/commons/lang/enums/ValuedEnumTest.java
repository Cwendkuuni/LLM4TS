package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.Enum;
import org.junit.Test;

public class ValuedEnumTest {
    private static final String TEST_CLASS_NAME = "TestValuedEnum";
    private static final int TEST_ENUM_1_VALUE = 1;
    private static final int TEST_ENUM_2_VALUE = 2;
    private static final int TEST_ENUM_3_VALUE = 3;

    @Test
    public void testGetEnum() {
        Class<TestValuedEnum> testClass = (Class<TestValuedEnum>) Class
                .forName(TEST_CLASS_NAME);
        TestValuedEnum testEnum1 = TestValuedEnum.getEnum(TEST_ENUM_1_VALUE);
        TestValuedEnum testEnum2 = TestValuedEnum.getEnum(TEST_ENUM_2_VALUE);
        TestValuedEnum testEnum3 = TestValuedEnum.getEnum(TEST_ENUM_3_VALUE);
        assertNotNull(testEnum1);
        assertNotNull(testEnum2);
        assertNotNull(testEnum3);
        assertEquals(TestValuedEnum.NAME_1, testEnum1.getName());
        assertEquals(TestValuedEnum.NAME_2, testEnum2.getName());
        assertEquals(TestValuedEnum.NAME_3, testEnum3.getName());
        assertEquals(TEST_ENUM_1_VALUE, testEnum1.getValue());
        assertEquals(TEST_ENUM_2_VALUE, testEnum2.getValue());
        assertEquals(TEST_ENUM_3_VALUE, testEnum3.getValue());
    }

    @Test
    public void testGetEnumMap() {
        Class<TestValuedEnum> testClass = (Class<TestValuedEnum>) Class
                .forName(TEST_CLASS_NAME);
        List<TestValuedEnum> testEnumList = Enum.getEnumList(testClass);
        Map<Integer, TestValuedEnum> testEnumMap = ValuedEnum.getEnumMap(testClass);
        for (TestValuedEnum testEnum : testEnumList) {
            assertEquals(testEnum, testEnumMap.get(testEnum.getValue()));
        }
    }

    @Test
    public void testGetEnumList() {
        Class<TestValuedEnum> testClass = (Class<TestValuedEnum>) Class
                .forName(TEST_CLASS_NAME);
        List<TestValuedEnum> testEnumList = ValuedEnum.getEnumList(testClass);
        assertEquals(3, testEnumList.size());
        for (TestValuedEnum testEnum : testEnumList) {
            assertEquals(testEnum, TestValuedEnum.getEnum(testEnum.getValue()));
        }
    }

    @Test
    public void testIterator() {
        Class<TestValuedEnum> testClass = (Class<TestValuedEnum>) Class
                .forName(TEST_CLASS_NAME);
        Iterator<TestValuedEnum> iterator = ValuedEnum.iterator(testClass);
        List<TestValuedEnum> testEnumList = new ArrayList<>();
        while (iterator.hasNext()) {
            testEnumList.add(iterator.next());
        }
        assertEquals(3, testEnumList.size());
        for (TestValuedEnum testEnum : testEnumList) {
            assertEquals(testEnum, TestValuedEnum.getEnum(testEnum.getValue()));
        }
    }

    @Test
    public void testCompareTo() {
        Class<TestValuedEnum> testClass = (Class<TestValuedEnum>) Class
                .forName(TEST_CLASS_NAME);
        TestValuedEnum testEnum1 = TestValuedEnum.getEnum(TEST_ENUM_1_VALUE);
        TestValuedEnum testEnum2 = TestValuedEnum.getEnum(TEST_ENUM_2_VALUE);
        TestValuedEnum testEnum3 = TestValuedEnum.getEnum(TEST_ENUM_3_VALUE);
        assertEquals(-1, testEnum1.compareTo(testEnum2));
        assertEquals(-1, testEnum1.compareTo(testEnum3));
        assertEquals(0, testEnum2.compareTo(testEnum2));
        assertEquals(1, testEnum2.compareTo(testEnum1));
        assertEquals(1, testEnum2.compareTo(testEnum3));
        assertEquals(2, testEnum3.compareTo(testEnum1));
        assertEquals(2, testEnum3.compareTo(testEnum2));
        assertEquals(0, testEnum3.compareTo(testEnum3));
    }

    static class TestValuedEnum extends ValuedEnum {
        private static final long serialVersionUID = 1L;
        private static final String NAME_1 = "TestEnum1";
        private static final String NAME_2 = "TestEnum2";
        private static final String NAME_3 = "TestEnum3";

        protected TestValuedEnum(String name, int value) {
            super(name, value);
        }
    }
}
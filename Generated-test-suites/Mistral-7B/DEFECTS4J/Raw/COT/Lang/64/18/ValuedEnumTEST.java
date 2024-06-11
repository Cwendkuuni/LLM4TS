import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(enumClass, "JavaVersionEnum", "Java 1.0");
        Assert.assertEquals("JavaVersionEnum_Java_1_0", javaVersion1_0.getName());
        Assert.assertEquals(100, javaVersion1_0.getValue());
    }

    @Test
    public void testGetEnum_invalidEnumName() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum enumInstance = null;
        try {
            enumInstance = (ValuedEnum) ValuedEnum.getEnum(enumClass, "InvalidEnumName");
        } catch (IllegalArgumentException e) {
            // expected
        }
        Assert.assertNull(enumInstance);
    }

    @Test
    public void testGetEnum_invalidEnumValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum enumInstance = null;
        try {
            enumInstance = (ValuedEnum) ValuedEnum.getEnum(enumClass, "JavaVersionEnum", 5);
        } catch (IllegalArgumentException e) {
            // expected
        }
        Assert.assertNull(enumInstance);
    }

    @Test
    public void testGetEnum_nullEnumClass() {
        ValuedEnum enumInstance = null;
        try {
            enumInstance = (ValuedEnum) ValuedEnum.getEnum(null, "JavaVersionEnum", "Java 1.0");
        } catch (IllegalArgumentException e) {
            // expected
        }
        Assert.assertNull(enumInstance);
    }

    @Test
    public void testGetEnumMap() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);
        for (ValuedEnum enumInstance : enumList) {
            String key = enumInstance.getName();
            Assert.assertEquals(enumInstance, enumMap.get(key));
        }
    }

    @Test
    public void testGetEnumMap_nullEnumClass() {
        Map<String, ValuedEnum> enumMap = null;
        try {
            enumMap = ValuedEnum.getEnumMap(null);
        } catch (IllegalArgumentException e) {
            // expected
        }
        Assert.assertNull(enumMap);
    }

    @Test
    public void testGetEnumList() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        Assert.assertNotNull(enumList);
        Assert.assertEquals(6, enumList.size()); // assuming there are 6 subclasses of ValuedEnum

        for (ValuedEnum enumInstance : enumList) {
            Assert.assertNotNull(enumInstance.getName());
            Assert.assertNotNull(enumInstance.getValue());
        }
    }

    @Test
    public void testGetEnumList_nullEnumClass() {
        List<ValuedEnum> enumList = null;
        try {
            enumList = ValuedEnum.getEnumList(null);
        } catch (IllegalArgumentException e) {
            // expected
        }
        Assert.assertNull(enumList);
    }

    @Test
    public void testIterator() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        int counter = 0;
        while (iterator.hasNext()) {
            ValuedEnum next = iterator.next();
            Assert.assertEquals(next, enumList.get(counter++));
        }
    }

    @Test
    public void testIterator_nullEnumClass() {
        Iterator<ValuedEnum> iterator = null;
        try {
            iterator = ValuedEnum.iterator(null);
        } catch (IllegalArgumentException e) {
            // expected
        }
        Assert.assertNull(iterator);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareTo_differentType() throws ClassCastException {
        ValuedEnum enumInstance1 = new TestValuedEnum("Test1", 1);
        ValuedEnum enumInstance2 = new TestValuedEnum("Test2", 2);
        int comparisonResult = enumInstance1.compareTo(enumInstance2);
        // comparison should throw ClassCastException
    }

    @Test
    public void testCompareTo_sameType_sameValue() {
        ValuedEnum enumInstance1 = new TestValuedEnum("Test1", 1);
        ValuedEnum enumInstance2 = new TestValuedEnum("Test1", 1);
        int comparisonResult = enumInstance1.compareTo(enumInstance2);
        Assert.assertEquals(0, comparisonResult);
    }

    @Test
    public void testCompareTo_sameType_differentValue() {
        ValuedEnum enumInstance1 = new TestValuedEnum("Test1", 1);
        ValuedEnum enumInstance2 = new TestValuedEnum("Test2", 2);
        int comparisonResult = enumInstance1.compareTo(enumInstance2);
        Assert.assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareTo_sameType_negativeValue() {
        ValuedEnum enumInstance1 = new TestValuedEnum("Test2", 2);
        ValuedEnum enumInstance2 = new TestValuedEnum("Test1", 1);
        int comparisonResult = enumInstance1.compareTo(enumInstance2);
        Assert.assertEquals(1, comparisonResult);
    }

    @Test
    public void testToString() {
        ValuedEnum enumInstance = new TestValuedEnum("TestEnum", 10);
        String toStringResult = enumInstance.toString();
        String expectedResult = "TestEnum[TestEnum=10]";
        Assert.assertEquals(expectedResult, toStringResult);
    }

    static class TestValuedEnum extends ValuedEnum {
        TestValuedEnum(String name, int value) {
            super(name, value);
        }
    }
}
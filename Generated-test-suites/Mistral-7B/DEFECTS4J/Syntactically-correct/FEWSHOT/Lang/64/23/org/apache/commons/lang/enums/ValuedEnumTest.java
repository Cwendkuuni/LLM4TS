package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testAbstractValuedEnumMethods() {
        // Test abstract class methods
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum enumInstance = new DummyValuedEnum("Dummy", 1);
        assertEquals(1, enumInstance.compareTo(null));
        assertEquals(1, enumInstance.compareTo(new DummyValuedEnum("Dummy", 2)));
        assertNull(ValuedEnum.getEnum(enumClass, 3));
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        assertNotNull(enumList);
        assertEquals(1, enumList.size());
        assertEquals(enumInstance, enumList.get(0));
        assertNull(ValuedEnum.getEnumMap(enumClass).get(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEnumClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnumValue() {
        Class<ValuedEnum> enumClass = DummyValuedEnum.class;
        ValuedEnum.getEnum(enumClass, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnumInstance() {
        Class<ValuedEnum> enumClass = DummyValuedEnum.class;
        ValuedEnum.compareTo(null, new DummyValuedEnum("Dummy", 1));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentClass() {
        Class<ValuedEnum> enumClass1 = DummyValuedEnum.class;
        Class<AnotherDummyValuedEnum> enumClass2 = AnotherDummyValuedEnum.class;
        ValuedEnum enumInstance1 = new DummyValuedEnum("Dummy1", 1);
        ValuedEnum enumInstance2 = new AnotherDummyValuedEnum("Dummy2", 2);
        enumInstance1.compareTo(enumInstance2);
    }

    // Add test cases for each subclass method if necessary

    static class DummyValuedEnum extends ValuedEnum {
        DummyValuedEnum(String name, int value) {
            super(name, value);
        }
    }

    static class AnotherDummyValuedEnum extends ValuedEnum {
        AnotherDummyValuedEnum(String name, int value) {
            super(name, value);
        }
    }
}
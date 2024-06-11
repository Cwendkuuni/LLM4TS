package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.Enum;
import org.apache.commons.lang.ValuedEnum;

/**
 * Unit tests for ValuedEnum class.
 */
public class ValuedEnumTest {

    /**
     * Test constructor for enum item.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        String name = "TestEnum";
        int value = 10;
        ValuedEnum enumInstance = new ValuedEnum(name, value) {
            @Override
            public String toString() {
                return "TestEnum[" + getName() + "=" + getValue() + "]";
            }
        };
        assertEquals(name, enumInstance.getName());
        assertEquals(value, enumInstance.getValue());
        assertEquals("TestEnum[TestEnum=10]", enumInstance.toString());

        Method getEnumMethod = enumClass.getMethod("getEnum", String.class);
        String javaVersion = "TestEnum";
        ValuedEnum getEnumByName = (ValuedEnum) getEnumMethod.invoke(null, javaVersion);
        assertEquals(enumInstance, getEnumByName);

        Method getEnumMethodByValue = enumClass.getMethod("getEnum", int.class);
        ValuedEnum getEnumByValue = (ValuedEnum) getEnumMethodByValue.invoke(null, value);
        assertEquals(enumInstance, getEnumByValue);
    }

    /**
     * Test getEnumList method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testGetEnumList() throws Exception {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = (List<ValuedEnum>) enumClass.getMethod("getEnumList").invoke(null);
        assertNotNull(enumList);
        assertEquals(1, enumList.size());
        assertEquals(this.getClass().getCanonicalName() + "[TestEnum=10]", enumList.get(0).toString());
    }

    /**
     * Test getEnumMap method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testGetEnumMap() throws Exception {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumMapMethod = enumClass.getMethod("getEnumMap");
        Object enumMap = getEnumMapMethod.invoke(null);
        assertNotNull(enumMap);
        assertTrue(enumMap instanceof Map);
    }

    /**
     * Test iterator method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testIterator() throws Exception {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method iteratorMethod = enumClass.getMethod("iterator");
        Iterator iterator = (Iterator) iteratorMethod.invoke(null);
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(this.getClass().getCanonicalName() + "[TestEnum=10]", iterator.next().toString());
        assertFalse(iterator.hasNext());
    }

    /**
     * Test compareTo method with same type enum.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testCompareToSameType() throws Exception {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        String name1 = "TestEnum1";
        int value1 = 1;
        String name2 = "TestEnum2";
        int value2 = 2;

        ValuedEnum enum1 = new ValuedEnum(name1, value1) {
            @Override
            public String toString() {
                return name1;
            }
        };
        ValuedEnum enum2 = new ValuedEnum(name2, value2) {
            @Override
            public String toString() {
                return name2;
            }
        };

        int compareResult = enum1.compareTo(enum1);
        assertEquals(0, compareResult);

        compareResult = enum1.compareTo(enum2);
        assertEquals(-1, compareResult);

        compareResult = enum2.compareTo(enum1);
        assertEquals(1, compareResult);
    }

    /**
     * Test compareTo method with different type enum.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testCompareToDifferentType() throws Exception {
        Class<ValuedEnum> enumClass1 = ValuedEnum.class;
        String name1 = "TestEnum1";
        int value1 = 1;

        Class<ValuedEnum2> enumClass2 = ValuedEnum2.class;
        String name2 = "TestEnum2";
        int value2 = 2;

        ValuedEnum enum1 = new ValuedEnum(name1, value1) {
            @Override
            public String toString() {
                return name1;
            }
        };

        ValuedEnum2 enum2 = new ValuedEnum2(name2, value2) {
            @Override
            public String toString() {
                return name2;
            }
        };

        int compareResult = enum1.compareTo(enum1);
        assertEquals(0, compareResult);

        try {
            compareResult = enum1.compareTo(enum2);
            fail("Expected NullPointerException or IllegalArgumentException");
        } catch (NullPointerException e) {
            // pass
        } catch (IllegalArgumentException e) {
            // pass
        }

        compareResult = enum2.compareTo(enum1);
        assertEquals(1, compareResult);
    }

    /**
     * Test null argument in constructor.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorNullName() throws Exception {
        new ValuedEnum(null, 1);
    }

    /**
     * Test null argument in constructor.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeValue() throws Exception {
        new ValuedEnum("TestEnum", -1);
    }

    /**
     * Test null argument in getEnum method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumNullEnumClass() throws Exception {
        getEnum(null, 1);
    }

    /**
     * Test null argument in getEnum method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumNullName() throws Exception {
        getEnum(ValuedEnum.class, null);
    }

    /**
     * Test non-integer argument in getEnum method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testGetEnumNonIntegerValue() throws Exception {
        getEnum(ValuedEnum.class, "TestEnum10.5");
    }

    /**
     * Test null argument in getEnumMap method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumMapNullEnumClass() throws Exception {
        getEnumMap(null);
    }

    /**
     * Test null argument in getEnumList method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetEnumListNullEnumClass() throws Exception {
        getEnumList(null);
    }

    /**
     * Test null argument in iterator method.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testIteratorNullEnumClass() throws Exception {
        iterator(null);
    }

    /**
     * Test getEnum method with null enumClass.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testGetEnumNullEnum() throws Exception {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 10);
        assertEquals(enumInstance, getEnum(enumInstance.getClass(), enumInstance));
    }

    /**
     * Test getEnum method with null string name.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testGetEnumNullName() throws Exception {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 10);
        assertNull(getEnum(enumInstance.getClass(), null));
    }

    /**
     * Test getEnum method with non-existent enum.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testGetEnumNonExistentEnum() throws Exception {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 10);
        assertNull(getEnum(enumInstance.getClass(), 11));
    }

    /**
     * Test getEnumMap method with null enumClass.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testGetEnumMapNullEnumClass() throws Exception {
        assertNull(getEnumMap(null));
    }

    /**
     * Test getEnumList method with null enumClass.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testGetEnumListNullEnumClass() throws Exception {
        assertNotNull(getEnumList(null));
        assertTrue(getEnumList(null).isEmpty());
    }

    /**
     * Test iterator method with null enumClass.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testIteratorNullEnumClass() throws Exception {
        assertFalse(iterator(null).hasNext());
    }

    /**
     * Test compareTo method with null other.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testCompareToNullOther() throws Exception {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 10);
        enumInstance.compareTo(null);
    }

    /**
     * Test compareTo method with different class other.
     *
     * @throws Exception if an error occurs during testing
     */
    @org.junit.Test
    public void testCompareToDifferentClassOther() throws Exception {
        ValuedEnum enumInstance1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum2 enumInstance2 = new ValuedEnum2("TestEnum2", 2);

        int compareResult = enumInstance1.compareTo(enumInstance2);
        assertEquals(1, compareResult);
    }

    private Object getEnum(Class enumClass, Object value) {
        return enumClass.getMethod("getEnum").invoke(null, value);
    }

    private Object getEnumMap(Class enumClass) {
        return enumClass.getMethod("getEnumMap").invoke(null);
    }

    private List getEnumList(Class enumClass) {
        return (List) enumClass.getMethod("getEnumList").invoke(null);
    }

    private Iterator iterator(Class enumClass) {
        return (Iterator) enumClass.getMethod("iterator").invoke(null);
    }
}

class ValuedEnum2 extends ValuedEnum {
    public ValuedEnum2(String name, int value) {
        super(name, value);
    }
}
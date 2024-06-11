package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

/**
 * Test class for {@link ValuedEnum}
 *
 * @author John Doe
 */
public class ValuedEnumTest {

    /**
     * Test method for {@link ValuedEnum#ValuedEnum(String, int)}
     */
    @Test
    public void testValuedEnumWithNameAndValue() {
        // Create a subclass of ValuedEnum with name and value
        MyValuedEnum myValuedEnum1 = new MyValuedEnum("Name1", 1);
        MyValuedEnum myValuedEnum2 = new MyValuedEnum("Name2", 2);

        // Test name and value
        assertEquals("Name1", myValuedEnum1.getName());
        assertEquals(1, myValuedEnum1.getValue());
        assertEquals("Name2", myValuedEnum2.getName());
        assertEquals(2, myValuedEnum2.getValue());
    }

    /**
     * Test method for {@link ValuedEnum#compareTo(Object)}
     */
    @Test
    public void testCompareTo() {
        MyValuedEnum myValuedEnum1 = new MyValuedEnum("Name1", 1);
        MyValuedEnum myValuedEnum2 = new MyValuedEnum("Name2", 2);
        MyValuedEnum myValuedEnum3 = new MyValuedEnum("Name1", 1);

        // Test comparison with same value
        assertEquals(0, myValuedEnum1.compareTo(myValuedEnum3));

        // Test comparison with different values
        assertEquals(-1, myValuedEnum1.compareTo(myValuedEnum2));
        assertEquals(1, myValuedEnum2.compareTo(myValuedEnum1));
    }

    /**
     * Test method for {@link ValuedEnum#getEnum(Class, String)}
     */
    @Test
    public void testGetEnumWithName() {
        MyValuedEnum myValuedEnum1 = new MyValuedEnum("Name1", 1);
        MyValuedEnum myValuedEnum2 = new MyValuedEnum("Name2", 2);

        MyValuedEnum enumByName = ValuedEnum.getEnum(MyValuedEnum.class, "Name1");
        assertEquals(myValuedEnum1, enumByName);

        MyValuedEnum enumByNameNull = ValuedEnum.getEnum(MyValuedEnum.class, null);
        assertNull(enumByNameNull);

        MyValuedEnum enumByNameInvalid = ValuedEnum.getEnum(MyValuedEnum.class, "InvalidName");
        assertNull(enumByNameInvalid);
    }

    /**
     * Test method for {@link ValuedEnum#getEnum(Class, int)}
     */
    @Test
    public void testGetEnumWithValue() {
        MyValuedEnum myValuedEnum1 = new MyValuedEnum("Name1", 1);
        MyValuedEnum myValuedEnum2 = new MyValuedEnum("Name2", 2);

        MyValuedEnum enumByValue = ValuedEnum.getEnum(MyValuedEnum.class, 1);
        assertEquals(myValuedEnum1, enumByValue);

        MyValuedEnum enumByValueNull = ValuedEnum.getEnum(MyValuedEnum.class, null);
        assertNull(enumByValueNull);

        MyValuedEnum enumByValueInvalid = ValuedEnum.getEnum(MyValuedEnum.class, 3);
        assertNull(enumByValueInvalid);
    }

    /**
     * Test method for {@link ValuedEnum#getEnumMap()}
     */
    @Test
    public void testGetEnumMap() {
        MyValuedEnum myValuedEnum1 = new MyValuedEnum("Name1", 1);
        MyValuedEnum myValuedEnum2 = new MyValuedEnum("Name2", 2);

        List<MyValuedEnum> enumList = ValuedEnum.getEnumList(MyValuedEnum.class);
        Map<Integer, MyValuedEnum> enumMap = new HashMap<>();
        for (MyValuedEnum enumElement : enumList) {
            enumMap.put(enumElement.getValue(), enumElement);
        }

        Map<Integer, MyValuedEnum> map = ValuedEnum.getEnumMap(MyValuedEnum.class);
        assertEquals(enumMap, map);
    }

    /**
     * Test method for {@link ValuedEnum#getEnumList()}
     */
    @Test
    public void testGetEnumList() {
        MyValuedEnum myValuedEnum1 = new MyValuedEnum("Name1", 1);
        MyValuedEnum myValuedEnum2 = new MyValuedEnum("Name2", 2);

        List<MyValuedEnum> enumList = ValuedEnum.getEnumList(MyValuedEnum.class);
        List<MyValuedEnum> expectedList = Arrays.asList(myValuedEnum1, myValuedEnum2);

        assertEquals(expectedList, enumList);
    }

    /**
     * Test method for {@link ValuedEnum#iterator()}
     */
    @Test
    public void testIterator() {
        MyValuedEnum myValuedEnum1 = new MyValuedEnum("Name1", 1);
        MyValuedEnum myValuedEnum2 = new MyValuedEnum("Name2", 2);

        List<MyValuedEnum> enumList = ValuedEnum.getEnumList(MyValuedEnum.class);
        Iterator<MyValuedEnum> iterator = ValuedEnum.iterator(MyValuedEnum.class);

        List<MyValuedEnum> expectedList = Arrays.asList(myValuedEnum1, myValuedEnum2);
        boolean hasNext = true;

        while (hasNext) {
            MyValuedEnum next = iterator.next();
            assertTrue(expectedList.contains(next));
            expectedList.remove(next);
            hasNext = iterator.hasNext();
        }

        assertFalse(iterator.hasNext());
    }

    /**
     * Test method for {@link ValuedEnum#getValue()}
     */
    @Test
    public void testGetValue() {
        MyValuedEnum myValuedEnum = new MyValuedEnum("Name", 1);
        assertEquals(1, myValuedEnum.getValue());
    }

    /**
     * Subclass of ValuedEnum for testing purposes
     */
    public static class MyValuedEnum extends ValuedEnum {

        public MyValuedEnum(String name, int value) {
            super(name, value);
        }

        private static Class<MyValuedEnum> getEnumClass() {
            return MyValuedEnum.class;
        }
    }
}
package org.apache.commons.lang.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    static class MyEnum extends ValuedEnum {
        private static final long serialVersionUID = 1L;

        MyEnum(String name, int value) {
            super(name, value);
        }

        public static MyEnum getEnum(String name) {
            return (MyEnum) ValuedEnum.getEnum(MyEnum.class, name);
        }

        public static MyEnum getEnum(int value) {
            return (MyEnum) ValuedEnum.getEnum(MyEnum.class, value);
        }

        public static List<MyEnum> getEnumList() {
            return (List<MyEnum>) ValuedEnum.getEnumList(MyEnum.class);
        }

        public static Iterator<MyEnum> iterator() {
            return ValuedEnum.iterator(MyEnum.class);
        }

        public static Map<String, MyEnum> getEnumMap() {
            return (Map<String, MyEnum>) ValuedEnum.getEnumMap(MyEnum.class);
        }
    }

    static class MyEnum2 extends ValuedEnum {
        private static final long serialVersionUID = 2L;

        MyEnum2(String name, int value) {
            super(name, value);
        }

        public static MyEnum2 getEnum(String name) {
            return (MyEnum2) ValuedEnum.getEnum(MyEnum2.class, name);
        }

        public static MyEnum2 getEnum(int value) {
            return (MyEnum2) ValuedEnum.getEnum(MyEnum2.class, value);
        }

        public static List<MyEnum2> getEnumList() {
            return (List<MyEnum2>) ValuedEnum.getEnumList(MyEnum2.class);
        }

        public static Iterator<MyEnum2> iterator() {
            return ValuedEnum.iterator(MyEnum2.class);
        }

        public static Map<String, MyEnum2> getEnumMap() {
            return (Map<String, MyEnum2>) ValuedEnum.getEnumMap(MyEnum2.class);
        }
    }

    @Test
    public void testGetEnum() {
        MyEnum myEnum = MyEnum.getEnum("First");
        assertEquals("First", myEnum.getName());
        assertEquals(1, myEnum.getValue());

        MyEnum myEnum2 = MyEnum.getEnum(1);
        assertEquals("First", myEnum2.getName());
        assertEquals(1, myEnum2.getValue());

        assertNull(MyEnum.getEnum("NonExistent"));
    }

    @Test
    public void testGetEnumValue() {
        MyEnum myEnum = MyEnum.getEnum("First");
        assertEquals(1, myEnum.getValue());

        MyEnum myEnum2 = MyEnum.getEnum(2);
        assertEquals(2, myEnum2.getValue());

        assertNull(MyEnum.getEnum("NonExistent"));
    }

    @Test
    public void testGetEnumList() {
        List<MyEnum> enumList = MyEnum.getEnumList();
        assertEquals(3, enumList.size());
        assertSame(MyEnum.First, enumList.get(0));
        assertSame(MyEnum.Second, enumList.get(1));
        assertSame(MyEnum.Third, enumList.get(2));
    }

    @Test
    public void testGetEnumMap() {
        Map<String, MyEnum> enumMap = MyEnum.getEnumMap();
        assertEquals(3, enumMap.size());
        assertEquals(MyEnum.First, enumMap.get("First"));
        assertEquals(MyEnum.Second, enumMap.get("Second"));
        assertEquals(MyEnum.Third, enumMap.get("Third"));
    }

    @Test
    public void testCompareTo() {
        MyEnum first = MyEnum.First;
        MyEnum second = MyEnum.Second;
        MyEnum third = MyEnum.Third;

        assertEquals(first.compareTo(second), -1);
        assertEquals(first.compareTo(third), -2);
        assertEquals(second.compareTo(first), 1);
        assertEquals(second.compareTo(third), -1);
        assertEquals(third.compareTo(first), 2);
        assertEquals(third.compareTo(second), 1);
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToDifferentTypes() {
        MyEnum myEnum = MyEnum.getEnum("First");
        MyEnum2 myEnum2 = new MyEnum2("Different", 42);

        myEnum.compareTo(myEnum2);
    }

    @Test
    public void testToString() {
        MyEnum myEnum = MyEnum.getEnum("First");
        assertEquals("MyEnum[First=1]", myEnum.toString());
    }

    @Test
    public void testReflectionGetValue() throws NoSuchMethodException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Class<MyEnum> myEnumClass = MyEnum.class;
        Method getValueMethod = myEnumClass.getDeclaredMethod("getValue");
        MyEnum myEnum = MyEnum.getEnum("First");

        assertEquals(1, getValueMethod.invoke(myEnum));
    }

    @Test
    public void testIterator() {
        List<MyEnum> enumList = MyEnum.getEnumList();
        Iterator<MyEnum> iterator = MyEnum.iterator();

        assertEquals(enumList, Arrays.asList(iterator.next(), iterator.next(), iterator.next()));
    }

    @Test
    public void testGetEnumMapKeySetValues() {
        Map<String, MyEnum> enumMap = MyEnum.getEnumMap();
        List<MyEnum> enumList = MyEnum.getEnumList();

        assertEquals(enumList, enumMap.values());
        assertEquals(Arrays.asList("First", "Second", "Third"), enumMap.keySet());
    }
}
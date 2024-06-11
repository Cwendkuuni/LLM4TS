import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        String name = "TestEnum";
        int value = 10;
        ValuedEnum enumObject = new ValuedEnum(name, value) {
        };
        assertEquals(name, enumObject.getName());
        assertEquals(value, enumObject.getValue());
    }

    @Test
    public void testGetEnum() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum expectedEnum = new ValuedEnum("ExpectedEnum", 1) {
        };
        ValuedEnum actualEnum = ValuedEnum.getEnum(enumClass, 1);
        assertEquals(expectedEnum, actualEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullEnumClass() {
        Class<? extends ValuedEnum> nullEnumClass = null;
        ValuedEnum.getEnum(nullEnumClass, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithInvalidValue() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum.getEnum(enumClass, 1000);
    }

    @Test
    public void testGetEnumMap() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Map<ValuedEnum, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);
        ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1) {
        };
        ValuedEnum secondEnum = new ValuedEnum("SecondEnum", 2) {
        };
        enumMap.put(firstEnum, firstEnum);
        enumMap.put(secondEnum, secondEnum);
        assertTrue(enumMap.containsKey(firstEnum));
        assertTrue(enumMap.containsValue(firstEnum));
        assertTrue(enumMap.containsKey(secondEnum));
        assertTrue(enumMap.containsValue(secondEnum));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumMapWithNullEnumClass() {
        Class<? extends ValuedEnum> nullEnumClass = null;
        ValuedEnum.getEnumMap(nullEnumClass);
    }

    @Test
    public void testGetEnumList() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1) {
        };
        ValuedEnum secondEnum = new ValuedEnum("SecondEnum", 2) {
        };
        enumList.add(firstEnum);
        enumList.add(secondEnum);
        assertTrue(enumList.contains(firstEnum));
        assertTrue(enumList.contains(secondEnum));
        assertEquals(2, enumList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumListWithNullEnumClass() {
        Class<? extends ValuedEnum> nullEnumClass = null;
        ValuedEnum.getEnumList(nullEnumClass);
    }

    @Test
    public void testIterator() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1) {
        };
        ValuedEnum secondEnum = new ValuedEnum("SecondEnum", 2) {
        };
        iterator.add(firstEnum);
        iterator.add(secondEnum);
        assertTrue(iterator.hasNext());
        ValuedEnum nextEnum = iterator.next();
        assertEquals(firstEnum, nextEnum);
        assertTrue(iterator.hasNext());
        nextEnum = iterator.next();
        assertEquals(secondEnum, nextEnum);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithNullEnumClass() {
        Class<? extends ValuedEnum> nullEnumClass = null;
        ValuedEnum.iterator(nullEnumClass);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1) {
        };
        ValuedEnum secondEnum = new ValuedEnum("SecondEnum", 2) {
        };
        iterator.add(firstEnum);
        iterator.add(secondEnum);
        iterator.remove();
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1) {
        };
        ValuedEnum enum2 = new ValvedEnum("Enum2", 2) {
        };
        int comparison = enum1.compareTo(enum2);
        assertTrue(comparison < 0);
    }

    @Test
    public void testCompareToEquals() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1) {
        };
        ValuedEnum enum2 = new ValuedEnum("Enum1", 1) {
        };
        int comparison = enum1.compareTo(enum2);
        assertEquals(0, comparison);
    }

    @Test
    public void testCompareToSameClass() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1) {
        };
        ValuedEnum enum2 = new ValuedEnum("Enum1", 1) {
        };
        int comparison = enum1.compareTo(enum2);
        assertEquals(0, comparison);
    }

    @Test
    public void testCompareToDifferentClasses() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<? extends ValuedEnum> enumClass1 = ValuedEnum.class;
        Class<? extends ValuedEnum> enumClass2 = ValuedEnum2.class;
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1) {
        };
        ValuedEnum2 enum2 = new ValuedEnum2("Enum2", 2) {
        };
        Method compareToMethod = ValuedEnum.class.getMethod("compareTo", Object.class);
        int comparison = (int) compareToMethod.invoke(enum1, enum2);
        assertTrue(comparison < 0);
    }

    private static class ValuedEnum2 extends ValuedEnum {
        ValuedEnum2(String name, int value) {
            super(name, value);
        }
    }
}
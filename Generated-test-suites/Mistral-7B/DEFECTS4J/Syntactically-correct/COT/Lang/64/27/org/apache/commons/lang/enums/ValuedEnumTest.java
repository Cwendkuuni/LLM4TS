import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        String name = "TestEnum";
        int value = 10;
        ValuedEnum enumInstance = new ValuedEnum(name, value) {
        };
        Assert.assertEquals(name, enumInstance.getName());
        Assert.assertEquals(value, enumInstance.getValue());
    }

    @Test
    public void testGetEnum() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum enumInstance = ValuedEnum.getEnum(enumClass, 10);
        Assert.assertNotNull(enumInstance);
        Assert.assertEquals("ValuedEnum10", enumInstance.getName());
        Assert.assertEquals(10, enumInstance.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullEnumClass() {
        Class<?> nullEnumClass = null;
        ValuedEnum.getEnum(nullEnumClass, 10);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNullValue() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum.getEnum(enumClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithInvalidValue() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        ValuedEnum.getEnum(enumClass, Integer.MIN_VALUE);
    }

    @Test
    public void testGetEnumMap() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap(enumClass);
        for (ValuedEnum valueEnum : enumList) {
            Assert.assertEquals(valueEnum, enumMap.get(valueEnum.getValue()));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapWithNullEnumClass() {
        Class<?> nullEnumClass = null;
        ValuedEnum.getEnumMap(nullEnumClass);
    }

    @Test
    public void testGetEnumList() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        Assert.assertEquals(10, enumList.size());
        for (ValuedEnum valueEnum : enumList) {
            Assert.assertNotNull(valueEnum);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumListWithNullEnumClass() {
        Class<?> nullEnumClass = null;
        ValuedEnum.getEnumList(nullEnumClass);
    }

    @Test
    public void testIterator() {
        Class<? extends ValuedEnum> enumClass = ValuedEnum.class;
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(enumClass);
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(enumClass);
        Assert.assertTrue(iterator.hasNext());
        ValuedEnum next = iterator.next();
        Assert.assertEquals(next, enumList.get(0));
        Assert.assertTrue(iterator.hasNext());
        ValuedEnum next2 = iterator.next();
        Assert.assertEquals(next2, enumList.get(1));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testIteratorWithNullEnumClass() {
        Class<?> nullEnumClass = null;
        ValuedEnum.iterator(nullEnumClass);
    }
}
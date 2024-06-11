import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Object enumInstance = new ValuedEnum("TestEnum", 1) {}.getInstance();
        Method getEnumMethod = enumClass.getMethod("getEnum", String.class);
        Object result = getEnumMethod.invoke(null, "TestEnum");
        Assert.assertEquals(enumInstance, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        Class<ValuedEnum> enumClass = null;
        Method getEnumMethod = null;
        try {
            getEnumMethod = ValuedEnum.class.getMethod("getEnum", int.class);
            getEnumMethod.invoke(null, 1);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        ValuedEnum.getEnum(enumClass, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNegativeValue() {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumMethod = null;
        try {
            getEnumMethod = ValuedEnum.class.getMethod("getEnum", int.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        ValuedEnum.getEnum(enumClass, -1);
    }

    @Test
    public void testGetEnumWithValidValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Object enumInstance = new ValuedEnum("TestEnum", 1) {}.getInstance();
        Method getEnumMethod = enumClass.getMethod("getEnum", int.class);
        Object result = getEnumMethod.invoke(null, 1);
        Assert.assertEquals(enumInstance, result);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = new ValuedEnum("TestEnum2", 2);
        Assert.assertEquals(-1, enum1.compareTo(enum2));
        Assert.assertEquals(1, enum2.compareTo(enum1));
        Assert.assertEquals(0, enum1.compareTo(enum1));
    }

    @Test
    public void testCompareToSameClassLoader() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = new ValuedEnum("TestEnum2", 2);
        Assert.assertEquals(-1, enum1.compareTo(enum2));
        Assert.assertEquals(1, enum2.compareTo(enum1));
        Assert.assertEquals(0, enum1.compareTo(enum1));
    }

    @Test
    public void testCompareToDifferentClassLoader() throws Exception {
        Class<?> enumClass1 = ValuedEnum.class.getClassLoader().loadClass("test.ValuedEnumTest.TestEnum1");
        Class<?> enumClass2 = ValuedEnum.class.getClassLoader().loadClass("test.ValuedEnumTest.TestEnum2");
        Object enum1 = enumClass1.getDeclaredConstructor().newInstance();
        Object enum2 = enumClass2.getDeclaredConstructor().newInstance();
        Method compareToMethod = ValuedEnum.class.getMethod("compareTo", Object.class);
        int result = (Integer) compareToMethod.invoke(enum1, enum2);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testToString() {
        ValuedEnum enumInstance = new ValuedEnum("TestEnum", 1) {}.getInstance();
        String expected = "ValuedEnum[TestEnum=1]";
        String result = enumInstance.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetEnumList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumListMethod = enumClass.getMethod("getEnumList");
        List<ValuedEnum> result = (List<ValuedEnum>) getEnumListMethod.invoke(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetEnumMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method getEnumMapMethod = enumClass.getMethod("getEnumMap");
        Map<Integer, ValuedEnum> result = (Map<Integer, ValuedEnum>) getEnumMapMethod.invoke(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testIterator() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<ValuedEnum> enumClass = ValuedEnum.class;
        Method iteratorMethod = enumClass.getMethod("iterator");
        Iterator<ValuedEnum> result = (Iterator<ValuedEnum>) iteratorMethod.invoke(null);
        Assert.assertNotNull(result);
    }
}
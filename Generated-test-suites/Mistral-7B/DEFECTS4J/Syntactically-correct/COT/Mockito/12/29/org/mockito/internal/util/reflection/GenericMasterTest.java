package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a non-generic field
        Field nonGenericField = getField(GenericMaster.class, "someField");
        Class expected = Object.class;
        Class actual = genericMaster.getGenericType(nonGenericField);
        Assert.assertEquals(expected, actual);

        // Test with a generic field of type List
        Field listField = getField(GenericMaster.class, "listField");
        Class expectedList = java.util.List.class;
        Class actualList = genericMaster.getGenericType(listField);
        Assert.assertEquals(expectedList, actualList);

        // Test with a generic field of type Map
        Field mapField = getField(GenericMaster.class, "mapField");
        Class expectedMap = java.util.Map.class;
        Class actualMap = genericMaster.getGenericType(mapField);
        Assert.assertEquals(expectedMap, actualMap);

        // Test with a generic field of type custom class
        Field customField = getField(GenericMaster.class, "customField");
        Class expectedCustom = MyCustomClass.class;
        Class actualCustom = genericMaster.getGenericType(customField);
        Assert.assertEquals(expectedCustom, actualCustom);

        // Test with a null field
        Field nullField = null;
        Class expectedNull = Object.class;
        Class actualNull = genericMaster.getGenericType(nullField);
        Assert.assertEquals(expectedNull, actualNull);
    }

    private static Field getField(Class clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    // Edge cases and exception handling tests

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field nullField = null;
        genericMaster.getGenericType(nullField);
    }

    @Test
    public void testGetGenericTypeWithAnonymousClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field anonymousField = getField(GenericMaster.class, "anonymousField");
        Class expectedAnonymous = AnonymousClass.class;
        Class actualAnonymous = genericMaster.getGenericType(anonymousField);
        Assert.assertNotNull(actualAnonymous);
        Assert.assertTrue(actualAnonymous.isAssignableFrom(expectedAnonymous));
    }

    @Test(expected = ClassCastException.class)
    public void testGetGenericTypeWithArrayField() {
        GenericMaster genericMaster = new GenericMaster();
        Field arrayField = getField(GenericMaster.class, "arrayField");
        Class expectedArray = int[].class;
        Class actualArray = genericMaster.getGenericType(arrayField);
        Assert.assertEquals(int[].class, actualArray.getComponentType());
        Assert.assertFalse(expectedArray.isAssignableFrom(actualArray));
        Assert.assertEquals(Integer.TYPE, actualArray.getComponentType().getComponentType());
        Assert.assertFalse(Integer.TYPE.isAssignableFrom(actualArray));
        Assert.assertThrows(ClassCastException.class, () -> {
            Assert.assertEquals(expectedArray, actualArray);
        });
    }

    @Test
    public void testGetGenericTypeWithRawTypeField() {
        GenericMaster genericMaster = new GenericMaster();
        Field rawTypeField = getField(GenericMaster.class, "rawTypeField");
        Class expectedRawType = java.util.List.class;
        Class actualRawType = genericMaster.getGenericType(rawTypeField);
        Assert.assertEquals(expectedRawType, actualRawType);
    }

    @Test
    public void testGetGenericTypeWithNoArgumentsField() {
        GenericMaster genericMaster = new GenericMaster();
        Field noArgumentsField = getField(GenericMaster.class, "noArgumentsField");
        Class expectedNoArguments = Object.class;
        Class actualNoArguments = genericMaster.getGenericType(noArgumentsField);
        Assert.assertEquals(expectedNoArguments, actualNoArguments);
    }

    @Test
    public void testGetGenericTypeWithMultipleArgumentsField() {
        GenericMaster genericMaster = new GenericMaster();
        Field multipleArgumentsField = getField(GenericMaster.class, "multipleArgumentsField");
        Class expectedMultipleArguments = java.util.Map.class;
        Class actualMultipleArguments = genericMaster.getGenericType(multipleArgumentsField);
        Assert.assertEquals(expectedMultipleArguments, actualMultipleArguments);
    }

    @Test
    public void testGetGenericTypeWithNestedGenericsField() {
        GenericMaster genericMaster = new GenericMaster();
        Field nestedGenericsField = getField(GenericMaster.class, "nestedGenericsField");
        Class<?> expectedNestedGenerics = java.util.List.class;
        Class<?> actualNestedGenerics = (Class<?>) genericMaster.getGenericType(nestedGenericsField);
        Assert.assertEquals(expectedNestedGenerics.getComponentType(), java.util.List.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWithIllegalArgumentField() {
        GenericMaster genericMaster = new GenericMaster();
        Field illegalArgumentField = getField(GenericMaster.class, "illegalArgumentField");
        genericMaster.getGenericType(illegalArgumentField);
    }

    private static class AnonymousClass {
    }

    private static class MyCustomClass {
    }

    private static class RawTypeClass implements java.util.List<Integer> {
    }

    private static class NoArgumentsClass {
    }

    private static class MultipleArgumentsClass<T, U> {
    }

    private static class NestedGenericsClass<T> {
        private java.util.List<String> list;
    }

    private static class IllegalArgumentClass<T> {
        private List<T> list;
        private int i;
    }
}
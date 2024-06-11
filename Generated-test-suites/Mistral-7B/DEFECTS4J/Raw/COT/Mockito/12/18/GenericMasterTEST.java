package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.GenericMasterTest; // make sure to import the test class itself for testing the test methods

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class clazz = GenericMaster.class;
        Field field = null; // assume a valid field is set here

        // Test when the field is not generic
        Class expectedResult = Object.class;
        Class result = GenericMaster.getGenericType(field);
        Assert.assertEquals(expectedResult, result);

        // Test when the field is generic with a single type argument
        String typeName = "java.util.List";
        Class expectedGenericType = java.util.List.class;
        try {
            Class testClass = Class.forName(typeName);
            Field field = testClass.getDeclaredField("list");
            field.setAccessible(true);
            result = GenericMaster.getGenericType(field);
        } catch (ClassNotFoundException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(expectedGenericType, result);

        // Test when the field is generic with multiple type arguments
        typeName = "java.util.Map";
        try {
            Class testClass = Class.forName(typeName);
            Field field = testClass.getDeclaredField("map");
            field.setAccessible(true);
            result = GenericMaster.getGenericType(field);
        } catch (ClassNotFoundException e) {
            Assert.fail(e.getMessage());
        }
        // Assuming Map<String, Integer>
        expectedGenericType = java.util.Map.class;
        Assert.assertEquals(expectedGenericType, result.getComponentType());
        Assert.assertEquals(String.class, result.getGenericComponentType(0));
        Assert.assertEquals(Integer.class, result.getGenericComponentType(1));

        // Test when the field's generic type is null
        field = null;
        Assert.assertNull(GenericMaster.getGenericType(field));

        // Test when the field has no type argument
        String nonGenericTypeName = "java.lang.String";
        try {
            Class testClass = Class.forName(nonGenericTypeName);
            field = testClass.getDeclaredField("string");
            field.setAccessible(true);
            result = GenericMaster.getGenericType(field);
        } catch (ClassNotFoundException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(String.class, result);

        // Test exception handling: FieldAccessException
        try {
            field.setAccessible(false);
            result = GenericMaster.getGenericType(field);
        } catch (IllegalAccessException e) {
            Assert.assertEquals("Cannot invoke this method while the current thread is blocked in an invocation", e.getMessage());
        }
    }

}
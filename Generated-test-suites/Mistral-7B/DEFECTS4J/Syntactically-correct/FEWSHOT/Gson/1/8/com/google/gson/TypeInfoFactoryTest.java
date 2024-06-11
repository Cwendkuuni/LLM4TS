package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

    @Test
    public void testTypeInfoForArray() {
        Type type = new int[3].getClass().getComponentType();
        TypeInfoArray typeInfoArray = TypeInfoFactory.TypeInfoForArray(type);
        assertNotNull(typeInfoArray);
        assertEquals("java.lang.int[]", typeInfoArray.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeInfoForArrayWithInvalidType() {
        Type type = new Object().getClass();
        TypeInfoFactory.TypeInfoForArray(type);
    }

    @Test
    public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = Integer.TYPE.getClass();
        Field field = clazz.getField("MIN_VALUE");
        Type type = field.getGenericType();
        Type parentType = clazz.getGenericSuperclass();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals("java.lang.Integer", typeInfo.toString());

        field = clazz.getField("MAX_VALUE");
        type = field.getGenericType();
        parentType = clazz.getGenericSuperclass();
        typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals("java.lang.Integer", typeInfo.toString());

        clazz = List.class;
        field = clazz.getField("newArrayList");
        type = field.getGenericType();
        parentType = clazz.getGenericSuperclass();
        typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals("java.util.List", typeInfo.toString());

        clazz = Map.class;
        field = clazz.getField("newHashMap");
        type = field.getGenericType();
        parentType = clazz.getGenericSuperclass();
        typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertEquals("java.util.Map", typeInfo.toString());
    }

    @Test
    public void testGetActualType() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = List.class;
        Field field = clazz.getField("newArrayList");
        Type type = field.getGenericType();
        Type parentType = clazz.getGenericSuperclass();
        Class<?> rawParentClass = clazz.getSuperclass();
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals("java.util.List", actualType.toString());

        clazz = Map.class;
        field = clazz.getField("newHashMap");
        type = field.getGenericType();
        parentType = clazz.getGenericSuperclass();
        rawParentClass = clazz.getSuperclass();
        actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals("java.util.Map", actualType.toString());

        clazz = MyClass.class; // custom class for test
        field = clazz.getField("myField");
        type = field.getGenericType();
        parentType = clazz.getGenericSuperclass();
        rawParentClass = clazz.getSuperclass();
        actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals("com.example.MyClass$MyNestedClass<java.lang.String>", actualType.toString());
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{Integer.TYPE, String.class};
        Type parentType = List.class.getGenericSuperclass();
        Class<?> rawParentClass = ArrayList.class;
        Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertEquals("java.lang.Integer", retTypes[0].toString());
        assertEquals("java.lang.String", retTypes[1].toString());
    }

    @Test
    public void testGetIndex() {
        TypeVariable<Integer> intType = Integer.TYPE.getTypeParameters()[0];
        TypeVariable<String> stringType = String.class.getTypeParameters()[0];

        TypeVariable<?>[] types = {intType, stringType};
        TypeVariable<?> type = intType;
        int index = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, index);

        type = stringType;
        index = TypeInfoFactory.getIndex(types, type);
        assertEquals(1, index);
    }
}
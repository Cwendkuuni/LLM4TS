package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Before;
import org.junit.Test;

public class TypeInfoFactoryTest {

    private Field field;
    private TypeInfoFactory TypeInfoFactory;

    @Before
    public void setUp() {
        field = TypeInfoFactory.class.getDeclaredField("field");
        TypeInfoFactory = new TypeInfoFactory();
    }

    @Test
    public void testGetTypeInfoForArray() {
        Type type = field.getGenericType();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField_withGenericFieldType() {
        Type typeDefiningF = TypeInfoFactory.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(typeInfo);
        assertEquals(field.getGenericType(), typeInfo.getType());   
    }

    @Test
    public void testGetTypeInfoForField_withClassFieldType() {
        Field field = TypeInfoFactory.class.getDeclaredField("classDefiningF");
        Type typeDefiningF = TypeInfoFactory.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(typeInfo);
        assertEquals(field.getGenericType(), typeInfo.getType());   
    }

    @Test
    public void testGetActualType_withClassType() {
        Type typeToEvaluate = field.getGenericType();
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualType_withParameterizedType() {
        Type typeToEvaluate = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualType_withGenericArrayType() {
        Type typeToEvaluate = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualType_withTypeVariable() {
        TypeVariable<?> typeToEvaluate = (TypeVariable<?>) field.getGenericType();
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals("? extends Object", actualType.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_withTypeVariableAndNoParameterizedType() {
        Field field = TypeInfoFactory.class.getDeclaredField("typeToEvaluate");
        Type typeToEvaluate = field.getGenericType();
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testGetActualType_withWildcardType() {
        WildcardType typeToEvaluate = (WildcardType) field.getGenericType();
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals(typeToEvaluate.getUpperBounds()[0], actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_withUnknownType() {
        Type typeToEvaluate = int.class;
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes_withMultipleTypes() {
        Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] actualTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(actualTypes);
        assertEquals(actualTypeArguments.length, actualTypes.length);
        for (int i = 0; i < actualTypeArguments.length; i++) {
            assertEquals(actualTypeArguments[i], actualTypes[i]);
        }
    }

    @Test
    public void testGetIndex_withExistingTypeVariable() {
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        TypeVariable<?>[] types = rawParentClass.getTypeParameters();
        int index = TypeInfoFactory.getIndex(types, types[0]);
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_withNonExistingTypeVariable() {
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        TypeVariable<?>[] types = rawParentClass.getTypeParameters();
        TypeInfoFactory.getIndex(types, TypeInfoFactory.class.getTypeParameters()[0]);
    }
}
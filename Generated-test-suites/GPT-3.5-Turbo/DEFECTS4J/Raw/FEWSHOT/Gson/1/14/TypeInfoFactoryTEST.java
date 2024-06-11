package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new GenericArrayTypeImpl(Integer.class);
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(result);
        assertEquals(Integer.class, result.getComponentType());
        assertEquals("[Ljava.lang.Integer;", result.toString());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = MyTestClass.class.getDeclaredField("myField");
        Type type = field.getGenericType();
        Type typeDefiningF = MyTestClass.class;
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(result);
        assertEquals(String.class, result.getType());
        assertEquals("class java.lang.String", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForField_invalidField() throws NoSuchFieldException {
        Field field = MyTestClass.class.getDeclaredField("invalidField");
        Type type = field.getGenericType();
        Type typeDefiningF = MyTestClass.class;
        TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForField_nullField() {
        Field field = null;
        Type typeDefiningF = MyTestClass.class;
        TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    }

    @Test
    public void testGetActualType_classType() {
        Type typeToEvaluate = String.class;
        Type parentType = MyTestClass.class;
        Class<?> rawParentClass = MyTestClass.class;
        Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testGetActualType_parameterizedType() {
        Type typeToEvaluate = new ParameterizedTypeImpl(String.class);
        Type parentType = MyTestClass.class;
        Class<?> rawParentClass = MyTestClass.class;
        Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(result);
        assertEquals(ParameterizedTypeImpl.class, result.getClass());
    }

    @Test
    public void testGetActualType_genericArrayType() {
        Type typeToEvaluate = new GenericArrayTypeImpl(String.class);
        Type parentType = MyTestClass.class;
        Class<?> rawParentClass = MyTestClass.class;
        Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(result);
        assertEquals(GenericArrayTypeImpl.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_invalidType() {
        Type typeToEvaluate = Integer.valueOf(10);
        Type parentType = MyTestClass.class;
        Class<?> rawParentClass = MyTestClass.class;
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = {String.class, Integer.class};
        Type parentType = MyTestClass.class;
        Class<?> rawParentClass = MyTestClass.class;
        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals(String.class, result[0]);
        assertEquals(Integer.class, result[1]);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = MyTestClass.class.getTypeParameters();
        TypeVariable<?> type = types[0];
        int result = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_typeNotPresent() {
        TypeVariable<?>[] types = MyTestClass.class.getTypeParameters();
        TypeVariable<?> type = new TypeVariableImpl("T");
        TypeInfoFactory.getIndex(types, type);
    }

    private static class MyTestClass<T> {
        private String myField;

        private Integer invalidField;
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {

        private final Type rawType;
        private final Type[] actualTypeArguments;
        private final Type ownerType;

        public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
            this.rawType = rawType;
            this.actualTypeArguments = actualTypeArguments;
            this.ownerType = ownerType;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public String toString() {
            return "ParameterizedTypeImpl [rawType=" + rawType + ", actualTypeArguments=" + Arrays.toString(actualTypeArguments)
                    + ", ownerType=" + ownerType + "]";
        }
    }

    private static class GenericArrayTypeImpl implements GenericArrayType {

        private final Type genericComponentType;

        public GenericArrayTypeImpl(Type genericComponentType) {
            this.genericComponentType = genericComponentType;
        }

        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }

        @Override
        public String toString() {
            return "GenericArrayTypeImpl [genericComponentType=" + genericComponentType + "]";
        }
    }

    private static class TypeVariableImpl implements TypeVariable<Class<?>> {

        private final String name;

        public TypeVariableImpl(String name) {
            this.name = name;
        }

        @Override
        public Type[] getBounds() {
            return new Type[0];
        }

        @Override
        public Class<?> getGenericDeclaration() {
            return null;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "TypeVariableImpl [name=" + name + "]";
        }
    }
}
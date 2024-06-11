package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        Type type = new int[5].getClass().getComponentType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(TypeUtils.INT_TYPE, typeInfo.boxedType);
        assertEquals(int[].class, typeInfo.rawType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeInfoForInvalidType() {
        Field field = new Object() {}.getClass().getDeclaredField("not_a_type");
        TypeInfoFactory.getTypeInfoForField(field, field.getClass());
    }

    @Test
    public void testTypeInfoForField() {
        String testClassName = "TestClassName$TestTypeVariable";
        String testFieldName = "testField";
        String expectedType = "java.lang.String";

        Class<TestField> testClass = (Class<TestField>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestField();
            }
        }.findClass(testClassName);

        try {
            Field field = testClass.getDeclaredField(testFieldName);
            field.setAccessible(true);
            Type type = field.getGenericType();

            TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, testClass);
            assertEquals(TypeUtils.getRawType(String.class), typeInfo.rawType);
            assertEquals(expectedType, typeInfo.typeName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Test class does not have the expected field.", e);
        }
    }

    @Test
    public void testGetActualTypeForClass() {
        String testClassName = "TestClassName$NestedClass";
        Class<TestNestedClass> testClass = (Class<TestNestedClass>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestNestedClass();
            }
        }.findClass(testClassName);

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(
                testClass.getDeclaredField("testField"), testClass);
        assertEquals(TestNestedClass.class, typeInfo.rawType);
        assertEquals("TestClassName$NestedClass", typeInfo.typeName);
    }

    @Test
    public void testGetActualTypeForParameterizedType() {
        String testClassName = "TestClassName$TestList";
        Class<TestList> testClass = (Class<TestList>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestList();
            }
        }.findClass(testClassName);

        Type fieldType = testClass.getDeclaredField("testList").getGenericType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(
                testClass.getDeclaredField("testList"), testClass);
        assertEquals(List.class, typeInfo.rawType);
        assertEquals("java.util.List", typeInfo.typeName);

        ParameterizedType parameterizedType = (ParameterizedType) fieldType;
        assertEquals(ArrayList.class, parameterizedType.getRawType());
        assertEquals(Integer.class, parameterizedType.getActualTypeArguments()[0]);
    }

    @Test
    public void testGetActualTypeForGenericArrayType() {
        String testClassName = "TestClassName$TestArray";
        Class<TestArray> testClass = (Class<TestArray>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestArray();
            }
        }.findClass(testClassName);

        Type fieldType = testClass.getDeclaredField("testArray").getGenericType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(
                testClass.getDeclaredField("testArray"), testClass);
        assertEquals(int[].class, typeInfo.rawType);
        assertEquals("int[]", typeInfo.typeName);

        GenericArrayType genericArrayType = (GenericArrayType) fieldType;
        assertEquals(Integer.TYPE, genericArrayType.getGenericComponentType());
    }

    @Test
    public void testGetActualTypeForTypeVariable() {
        String testClassName = "TestClassName$TestTypeVariable";
        Class<TestTypeVariable> testClass = (Class<TestTypeVariable>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestTypeVariable();
            }
        }.findClass(testClassName);

        Field field = testClass.getDeclaredField("testField");
        field.setAccessible(true);
        Type fieldType = field.getGenericType();
        TypeVariable<String> typeVariable = (TypeVariable<String>) fieldType;

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, testClass);
        assertEquals(String.class, typeInfo.rawType);
        assertEquals("T", typeInfo.typeName);

        Type type = TypeInfoFactory.getActualType(typeVariable, testClass, testClass);
        assertEquals(String.class, type);
    }

    @Test
    public void testGetActualTypeForWildcardType() {
        String testClassName = "TestClassName$TestWildcardType";
        Class<TestWildcardType> testClass = (Class<TestWildcardType>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestWildcardType();
            }
        }.findClass(testClassName);

        Field field = testClass.getDeclaredField("testField");
        field.setAccessible(true);
        Type fieldType = field.getGenericType();
        WildcardType wildcardType = (WildcardType) fieldType;

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, testClass);
        assertEquals(Object.class, typeInfo.rawType);
        assertEquals("?", typeInfo.typeName);

        Type type = TypeInfoFactory.getActualType(wildcardType.getUpperBounds()[0], testClass, testClass);
        assertEquals(String.class, type);
    }

    @Test
    public void testExtractRealTypesForArray() {
        String testClassName = "TestClassName$TestArray";
        Class<TestArray> testClass = (Class<TestArray>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestArray();
            }
        }.findClass(testClassName);

        Field field = testClass.getDeclaredField("testArray");
        field.setAccessible(true);
        Type fieldType = field.getGenericType();
        GenericArrayType genericArrayType = (GenericArrayType) fieldType;

        Type[] actualTypes = TypeInfoFactory.extractRealTypes(genericArrayType.getActualTypeArguments(), null, null);
        assertEquals(1, actualTypes.length);
        assertEquals(Integer.TYPE, actualTypes[0]);
    }

    @Test
    public void testExtractRealTypesForParameterizedType() {
        String testClassName = "TestClassName$TestList";
        Class<TestList> testClass = (Class<TestList>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestList();
            }
        }.findClass(testClassName);

        Field field = testClass.getDeclaredField("testList");
        field.setAccessible(true);
        Type fieldType = field.getGenericType();
        ParameterizedType parameterizedType = (ParameterizedType) fieldType;

        Type[] actualTypes = TypeInfoFactory.extractRealTypes(parameterizedType.getActualTypeArguments(), null, null);
        assertEquals(2, actualTypes.length);
        assertEquals(ArrayList.class, actualTypes[0]);
        assertEquals(Integer.class, actualTypes[1]);
    }

    @Test
    public void testExtractRealTypesForTypeVariable() {
        String testClassName = "TestClassName$TestTypeVariable";
        Class<TestTypeVariable> testClass = (Class<TestTypeVariable>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestTypeVariable();
            }
        }.findClass(testClassName);

        Field field = testClass.getDeclaredField("testField");
        field.setAccessible(true);
        Type fieldType = field.getGenericType();
        TypeVariable<String> typeVariable = (TypeVariable<String>) fieldType;

        Type[] actualTypes = TypeInfoFactory.extractRealTypes(new Type[] { typeVariable }, null, null);
        assertEquals(1, actualTypes.length);
        assertEquals(String.class, actualTypes[0]);
    }

    @Test
    public void testExtractRealTypesForWildcardType() {
        String testClassName = "TestClassName$TestWildcardType";
        Class<TestWildcardType> testClass = (Class<TestWildcardType>) new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                return new TestWildcardType();
            }
        }.findClass(testClassName);

        Field field = testClass.getDeclaredField("testField");
        field.setAccessible(true);
        Type fieldType = field.getGenericType();
        WildcardType wildcardType = (WildcardType) fieldType;

        Type[] actualTypes = TypeInfoFactory.extractRealTypes(wildcardType.getUpperBounds(), null, null);
        assertEquals(1, actualTypes.length);
        assertEquals(String.class, actualTypes[0]);
    }
}

class TestField {
    String testField;
}

class TestList<T> extends ArrayList<T> {
    List<Integer> testList;
}

class TestArray {
    int[] testArray;
}

class TestTypeVariable<T> {
    T testField;
}

class TestWildcardType {
    List<?> testField;
}
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
        Type type = new Integer[10].getClass();
        TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }
    
    
    @Test
    public void testGetTypeInfoForField() {
        Class<?> classDefiningF = Object.class;
        Type type = Double.class;
        Field f = null;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(f, type);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }
    
    @Test
    public void testGetActualType() {
        Type typeToEvaluate = Object.class;
        Type parentType = Double.class;
        Class<?> rawParentClass = Object.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }
    
    @Test
    public void testGetActualType_ParameterizedType() {
        Type typeToEvaluate = Double.class;
        Type parentType = Object.class;
        Class<?> rawParentClass = Integer.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }
    
    @Test
    public void testGetActualType_GenericArrayType() {
        Type typeToEvaluate = new Integer[10].getClass();
        Type parentType = Object.class;
        Class<?> rawParentClass = Integer.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }
    
    @Test
    public void testGetActualType_TypeVariable() {
        Type typeToEvaluate = new TypeToken<Integer>() {}.getType();
        Type parentType = new TypeToken<Double<Integer>>() {}.getType();
        Class<?> rawParentClass = Object.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_typeVariableWithoutParameterizedType() {
        Type typeToEvaluate = new TypeToken<Integer>() {}.getType();
        Type parentType = Object.class;
        Class<?> rawParentClass = Double.class;
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }
    
    @Test
    public void testGetActualType_WildcardType() {
        Type typeToEvaluate = new TypeToken<Integer>() {}.getType();
        Type parentType = new TypeToken<? extends Number>() {}.getType();
        Class<?> rawParentClass = Object.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        Type typeToEvaluate = new TypeToken<Integer>() {}.getType();
        Type parentType = Object.class;
        Class<?> rawParentClass = Double.class;
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }
    
    
    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[] {Integer.class};
        Type parentType = Object.class;
        Class<?> rawParentClass = Double.class;
        Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(retTypes);
        assertEquals(1, retTypes.length);
        assertEquals(Integer.class, retTypes[0]);
    }
    
    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = new TypeVariable<?>[2];
        types[0] = new TypeVariableImpl();
        types[1] = new TypeVariableImpl();
        TypeVariable<?> type = types[1];
        int index = TypeInfoFactory.getIndex(types, type);
        assertEquals(1, index);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetIndex_TypeNotPresent() {
        TypeVariable<?>[] types = new TypeVariable<?>[2];
        types[0] = new TypeVariableImpl();
        types[1] = new TypeVariableImpl();
        TypeVariable<?> type = new TypeVariableImpl();
        TypeInfoFactory.getIndex(types, type);
    }
    
    private static class TypeVariableImpl implements TypeVariable<Void> {
        @Override
        public Type[] getBounds() {
            return null;
        }

        @Override
        public AnnotatedType[] getAnnotatedBounds() {
            return null;
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            return null;
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public TypeVariable<?>[] getBounds() {
            return null;
        }

        @Override
        public AnnotatedType[] getAnnotatedBounds() {
            return null;
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            return null;
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
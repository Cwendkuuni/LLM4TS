package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
    	Type type = new int[10].getClass();
        TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(type, typeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
    	Field field = TypeInfoFactory.class.getDeclaredField("classDefiningF");
    	Type typeDefiningF = field.getType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertEquals(typeDefiningF, typeInfo.getType());
    }

    @Test
    public void testGetActualType() {
        Type type = new ParameterizedTypeImpl(TypeInfoFactory.class, new Type[0], null);
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, actualType);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = {int.class, String.class};
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertEquals(actualTypeArguments.length, realTypes.length);
        for (int i = 0; i < actualTypeArguments.length; i++) {
            assertEquals(actualTypeArguments[i], realTypes[i]);
        }
    }

    @Test
    public void testGetIndex() {
    	TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
    	TypeVariable<?> type = types[0];
    	int index = TypeInfoFactory.getIndex(types, type);
    	assertEquals(0, index);
    }
    
    @Test
    public void testGetIndexNotFound() {
    	TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
    	TypeVariable<?> type = new TypeVariableImpl("T", TypeInfoFactory.class);
    	try {
        	TypeInfoFactory.getIndex(types, type);
    		assertFalse(true);
    	} catch (IllegalStateException e) {
    		assertTrue(true);
        }
    }
}
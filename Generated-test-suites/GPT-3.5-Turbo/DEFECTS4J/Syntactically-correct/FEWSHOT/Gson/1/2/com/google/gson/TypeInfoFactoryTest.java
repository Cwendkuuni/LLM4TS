package com.google.gson;

import com.google.gson.TypeInfoFactory.TypeInfoArray;
import com.google.gson.TypeInfoFactory.TypeInfo;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {
    
    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return "TestArray";
            }
        };
        
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(type, typeInfoArray.getType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayNullType() {
        TypeInfoFactory.getTypeInfoForArray(null);
    }
    
    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = TypeInfoFactory.class.getDeclaredField("typeDefiningF");
        Type typeDefiningF = getClass();
        
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(typeInfo);
        assertEquals(typeDefiningF, typeInfo.getType());
    }
    
    @Test(expected = NoSuchFieldException.class)
    public void testGetTypeInfoForFieldInvalidField() throws NoSuchFieldException {
        TypeInfoFactory.getTypeInfoForField(null, getClass());
    }
    
    @Test
    public void testGetActualType() {
        Type typeToEvaluate = new Type() {
            @Override
            public String getTypeName() {
                return "TestType";
            }
        };
        
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals(typeToEvaluate, actualType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeInvalidType() {
        TypeInfoFactory.getActualType(null, null, null);
    }
    
    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[2];
        actualTypeArguments[0] = new Type() {
            @Override
            public String getTypeName() {
                return "TestType1";
            }
        };
        actualTypeArguments[1] = new Type() {
            @Override
            public String getTypeName() {
                return "TestType2";
            }
        };
        
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        
        Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(retTypes);
        assertEquals(actualTypeArguments.length, retTypes.length);
        assertEquals(actualTypeArguments[0], retTypes[0]);
        assertEquals(actualTypeArguments[1], retTypes[1]);
    }
    
    @Test(expected = NullPointerException.class)
    public void testExtractRealTypesNullArguments() {
        TypeInfoFactory.extractRealTypes(null, null, null);
    }
    
    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = new TypeVariable[2];
        types[0] = new TypeVariable<>() {
            @Override
            public String getName() {
                return "Type1";
            }
        };
        types[1] = new TypeVariable<>() {
            @Override
            public String getName() {
                return "Type2";
            }
        };
        
        TypeVariable<?> type = new TypeVariable<>() {
            @Override
            public String getName() {
                return "Type2";
            }
        };
        
        int index = TypeInfoFactory.getIndex(types, type);
        assertEquals(1, index);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetIndexInvalidType() {
        TypeInfoFactory.getIndex(null, null);
    }
}
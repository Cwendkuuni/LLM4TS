package com.google.gson;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return "String[]";
            }
        };
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals("String[]", result.getType().getTypeName());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = TypeInfoFactory.class.getDeclaredField("classDefiningF");
        Type typeDefiningF = field.getType();
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertEquals("Class<?>", result.getType().getTypeName());
    }

    @Test
    public void testGetActualType() {
        Type typeToEvaluate = new Type() {
            @Override
            public String getTypeName() {
                return "T";
            }
        };
        Type parentType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { typeToEvaluate };
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
            @Override
            public Type getRawType() {
                return null;
            }
            @Override
            public String getTypeName() {
                return null;
            }
        };
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals("T", result.getTypeName());
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = { new Type() {
            @Override
            public String getTypeName() {
                return "T1";
            }
        }, new Type() {
            @Override
            public String getTypeName() {
                return "T2";
            }
        }};
        Type parentType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return actualTypeArguments;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
            @Override
            public Type getRawType() {
                return null;
            }
            @Override
            public String getTypeName() {
                return null;
            }
        };
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertEquals("T1", result[0].getTypeName());
        assertEquals("T2", result[1].getTypeName());
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = { new TypeVariable<Class<?>>() {
            @Override
            public String getName() {
                return "T";
            }
            @Override
            public Type[] getBounds() {
                return new Type[] { Object.class };
            }
        }};
        TypeVariable<?> type = types[0];
        int result = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, result);
    }
}
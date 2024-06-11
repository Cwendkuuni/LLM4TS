package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Before;
import org.junit.Test;

public class TypeInfoFactoryTest {

    private TypeInfoArray typeInfoArray;
    private Field field;
    private Type typeDefiningF;
    private TypeInfo typeInfoField;

    @Before
    public void setUp() {
        typeInfoArray = null;
        field = null;
        typeDefiningF = null;
        typeInfoField = null;
    }

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
        };
        typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertTrue(typeInfoArray instanceof TypeInfoArray);
    }

    @Test
    public void testGetTypeInfoForField() {
        field = new Field() {
            @Override
            public Class<?> getType() {
                return null;
            }
            @Override
            public Type getGenericType() {
                return null;
            }
        };
        typeDefiningF = new Type() {
        };
        typeInfoField = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertTrue(typeInfoField instanceof TypeInfo);
    }

    @Test
    public void testGetTypeInfoForField_typeToEvaluateIsClass() {
        field = new Field() {
            @Override
            public Class<?> getType() {
                return null;
            }
            @Override
            public Type getGenericType() {
                return new Class<>() {
                };
            }
        };
        typeDefiningF = new Type() {
        };
        typeInfoField = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertTrue(typeInfoField instanceof TypeInfo);
    }

    @Test
    public void testGetTypeInfoForField_typeToEvaluateIsParameterizedType() {
        field = new Field() {
            @Override
            public Class<?> getType() {
                return null;
            }
            @Override
            public Type getGenericType() {
                return new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[] { new Type() {
                        } };
                    }
                    @Override
                    public Type getRawType() {
                        return null;
                    }
                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };
            }
        };
        typeDefiningF = new Type() {
        };
        typeInfoField = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertTrue(typeInfoField instanceof TypeInfo);
    }

    @Test
    public void testGetTypeInfoForField_typeToEvaluateIsGenericArrayType() {
        field = new Field() {
            @Override
            public Class<?> getType() {
                return null;
            }
            @Override
            public Type getGenericType() {
                return new GenericArrayType() {
                    @Override
                    public Type getGenericComponentType() {
                        return null;
                    }
                };
            }
        };
        typeDefiningF = new Type() {
        };
        typeInfoField = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertTrue(typeInfoField instanceof TypeInfo);
    }

    @Test
    public void testGetTypeInfoForField_typeToEvaluateIsTypeVariable() {
        field = new Field() {
            @Override
            public Class<?> getType() {
                return null;
            }
            @Override
            public Type getGenericType() {
                return new TypeVariable<Class<?>>() {
                };
            }
        };
        typeDefiningF = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { new Type() {
                } };
            }
            @Override
            public Type getRawType() {
                return null;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        typeInfoField = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertTrue(typeInfoField instanceof TypeInfo);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForField_typeToEvaluateIsTypeVariable_parentTypeIsNotParameterizedType() {
        field = new Field() {
            @Override
            public Class<?> getType() {
                return null;
            }
            @Override
            public Type getGenericType() {
                return new TypeVariable<Class<?>>() {
                };
            }
        };
        typeDefiningF = new TypeVariable<Class<?>>() {
        };
        typeInfoField = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    }

    @Test
    public void testGetTypeInfoForField_typeToEvaluateIsWildcardType() {
        field = new Field() {
            @Override
            public Class<?> getType() {
                return null;
            }
            @Override
            public Type getGenericType() {
                return new WildcardType() {
                    @Override
                    public Type[] getUpperBounds() {
                        return new Type[] { new Type() {
                        } };
                    }
                    @Override
                    public Type[] getLowerBounds() {
                        return null;
                    }
                };
            }
        };
        typeDefiningF = new Type() {
        };
        typeInfoField = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertTrue(typeInfoField instanceof TypeInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_typeToEvaluateIsNotClassParameterizedTypeGenericArrayTypeTypeVariableOrWildcardType() {
        Type typeToEvaluate = new Type() {
        };
        Type parentType = new Type() {
        };
        Class<?> rawParentClass = new Class<>() {
        };
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testGetActualType_typeToEvaluateIsClass() {
        Type typeToEvaluate = new Class<>() {
        };
        Type parentType = new Type() {
        };
        Class<?> rawParentClass = new Class<>() {
        };
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualType_typeToEvaluateIsParameterizedType() {
        Type typeToEvaluate = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { new Type() {
                } };
            }
            @Override
            public Type getRawType() {
                return null;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        Type parentType = new Type() {
        };
        Class<?> rawParentClass = new Class<>() {
        };
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertTrue(actualType instanceof ParameterizedType);
    }

    @Test
    public void testGetActualType_typeToEvaluateIsGenericArrayType() {
        Type typeToEvaluate = new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return null;
            }
        };
        Type parentType = new Type() {
        };
        Class<?> rawParentClass = new Class<>() {
        };
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertTrue(actualType instanceof GenericArrayType);
    }

    @Test
    public void testGetActualType_typeToEvaluateIsTypeVariable() {
        Type typeToEvaluate = new TypeVariable<Class<?>>() {
        };
        Type parentType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { new Type() {
                } };
            }
            @Override
            public Type getRawType() {
                return null;
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        Class<?> rawParentClass = new Class<>() {
        };
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertTrue(actualType instanceof Type);
    }

    @Test
    public void testGetActualType_typeToEvaluateIsWildcardType() {
        Type typeToEvaluate = new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return new Type[] { new Type() {
                } };
            }
            @Override
            public Type[] getLowerBounds() {
                return null;
            }
        };
        Type parentType = new Type() {
        };
        Class<?> rawParentClass = new Class<>() {
        };
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertTrue(actualType instanceof Type);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[] { new Type() {
        } };
        Type parentType = new Type() {
        };
        Class<?> rawParentClass = new Class<>() {
        };
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertEquals(actualTypeArguments.length, realTypes.length);
        for (int i = 0; i < actualTypeArguments.length; i++) {
            assertTrue(realTypes[i] instanceof Type);
        }
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = new TypeVariable<?>[] { new TypeVariable<Class<?>>() {
        } };
        TypeVariable<?> type = new TypeVariable<Class<?>>() {
        };
        int index = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_typeNotPresentInClassDeclaration() {
        TypeVariable<?>[] types = new TypeVariable<?>[] { new TypeVariable<Class<?>>() {
        } };
        TypeVariable<?> type = new TypeVariable<Class<?>>() {
        };
        TypeInfoFactory.getIndex(types, type);
    }
}
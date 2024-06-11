import com.google.gson.Preconditions;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return "java.lang.String[]";
            }
        };
        TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(infoArray);
        assertEquals(type, infoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = TypeInfoFactoryTest.class.getDeclaredField("field");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TypeInfoFactoryTest.class);
        assertNotNull(typeInfo);
        assertEquals(field.getGenericType(), typeInfo.getType());
    }

    @Test
    public void testGetActualType_ClassType() {
        Type type = String.class;
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {String.class, Integer.class};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public String getTypeName() {
                return null;
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertTrue(actualType instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) actualType;
        assertEquals(type.getRawType(), parameterizedType.getRawType());
        assertEquals(type.getOwnerType(), parameterizedType.getOwnerType());
        assertEquals(type.getTypeName(), parameterizedType.getTypeName());
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        Type type = new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return String.class;
            }

            @Override
            public String getTypeName() {
                return null;
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertTrue(actualType instanceof GenericArrayType);
        GenericArrayType genericArrayType = (GenericArrayType) actualType;
        assertEquals(type.getGenericComponentType(), genericArrayType.getGenericComponentType());
        assertEquals(type.getTypeName(), genericArrayType.getTypeName());
    }

    @Test
    public void testGetActualType_TypeVariable_ParameterizedType() {
        Type type = new TypeVariable<Class<?>>() {};
        Type parentType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {String.class};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public String getTypeName() {
                return null;
            }
        };
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualType_TypeVariable_NoParameterizedType() {
        Type type = new TypeVariable<Class<?>>() {};
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        try {
            TypeInfoFactory.getActualType(type, parentType, rawParentClass);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expecting parameterized type, got " + parentType
                    + ".\n Are you missing the use of TypeToken idiom?\n See "
                    + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener", e.getMessage());
        } catch (Exception e) {
            fail("Expected UnsupportedOperationException, but got " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testGetActualType_WildcardType() {
        Type type = new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return new Type[] {String.class};
            }

            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }

            @Override
            public String getTypeName() {
                return null;
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String.class, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return null;
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }

    private Field field;
}
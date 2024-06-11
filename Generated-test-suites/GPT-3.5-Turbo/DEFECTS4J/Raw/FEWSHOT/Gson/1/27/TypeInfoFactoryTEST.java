import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            public String getTypeName() {
                return "java.lang.String[]";
            }
        };
        TypeInfoArray arrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(arrayTypeInfo);
        assertEquals(type, arrayTypeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = TypeInfoFactory.class.getDeclaredField("typeDefiningF");
        Type typeDefiningF = field.getGenericType();
        TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(fieldTypeInfo);
        assertEquals(typeDefiningF, fieldTypeInfo.getType());
    }

    @Test
    public void testGetActualType_Class() {
        Type type = String.class;
        Type parentType = TestClass.class;
        Class<?> rawParentClass = TestClass.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        Type type = new ParameterizedType() {

            public Type getRawType() {
                return String.class;
            }

            public Type getOwnerType() {
                return null;
            }

            public Type[] getActualTypeArguments() {
                return new Type[] { Integer.class };
            }

            public String getTypeName() {
                return "java.lang.String<java.lang.Integer>";
            }
        };
        Type parentType = TestClass.class;
        Class<?> rawParentClass = TestClass.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(type.getTypeName(), actualType.getTypeName());
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        Type type = new GenericArrayType() {

            public Type getGenericComponentType() {
                return String.class;
            }

            public String getTypeName() {
                return "java.lang.String[]";
            }
        };
        Type parentType = TestClass.class;
        Class<?> rawParentClass = TestClass.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(type.getTypeName(), actualType.getTypeName());
    }

    @Test
    public void testGetActualType_TypeVariable_with_ParameterizedTypeParent() {
        Type type = new TypeVariable<Class<?>>() {

            public String getName() {
                return "T";
            }

            public Type[] getBounds() {
                return new Type[] { Object.class };
            }

            public String getTypeName() {
                return "T";
            }
        };
        Type parentType = new ParameterizedType() {

            public Type getRawType() {
                return TestClass.class;
            }

            public Type getOwnerType() {
                return null;
            }

            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }

            public String getTypeName() {
                return "TestClass<java.lang.String>";
            }
        };
        Class<?> rawParentClass = TestClass.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(String.class, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariable_without_ParameterizedTypeParent() {
        Type type = new TypeVariable<Class<?>>() {

            public String getName() {
                return "T";
            }

            public Type[] getBounds() {
                return new Type[] { Object.class };
            }

            public String getTypeName() {
                return "T";
            }
        };
        Type parentType = String.class;
        Class<?> rawParentClass = String.class;
        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }

    @Test
    public void testGetActualType_WildcardType() {
        Type type = new WildcardType() {

            public Type[] getUpperBounds() {
                return new Type[] { Object.class };
            }

            public Type[] getLowerBounds() {
                return new Type[0];
            }

            public String getTypeName() {
                return "? extends java.lang.Object";
            }
        };
        Type parentType = TestClass.class;
        Class<?> rawParentClass = TestClass.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(Object.class, actualType);
    }

    private static class TestClass<T> {

        private T typeDefiningF;
    }
}
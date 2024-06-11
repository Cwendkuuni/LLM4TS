import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new TypeInfoFactoryTest[0].getClass().getGenericSuperclass();
        TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(info);
        assertEquals(type, info.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = TypeInfoFactoryTest.class.getDeclaredField("testField");
        Type declaringType = TypeInfoFactoryTest.class;
        TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, declaringType);
        assertNotNull(info);
        assertEquals(field.getGenericType(), info.getType());
    }

    @Test
    public void testGetActualType_Class() {
        Type type = String.class;
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        ParameterizedType type = new TypeInfoFactoryTest.GenericClass<String>().getClassType();
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type.getRawType(), actualType);

        ParameterizedType actualParameterizedType = (ParameterizedType) actualType;
        Type[] actualTypeArguments = actualParameterizedType.getActualTypeArguments();
        Type[] expectedTypeArguments = type.getActualTypeArguments();

        for (int i = 0; i < expectedTypeArguments.length; i++) {
            assertEquals(expectedTypeArguments[i], actualTypeArguments[i]);
        }
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        GenericArrayType type = new TypeInfoFactoryTest.GenericClass<String[]>().getClassArrayType();
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, actualType);
    }

    @Test
    public void testGetActualType_TypeVariable_ParameterizedType() {
        ParameterizedType parentType = new TypeInfoFactoryTest.GenericClass<Integer>().getClassType();
        TypeVariable<?> typeVariable = Integer.class.getTypeParameters()[0];
        Type actualType = TypeInfoFactory.getActualType(typeVariable, parentType, TypeInfoFactoryTest.GenericClass.class);
        assertEquals(Integer.class, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariable() {
        TypeVariable<?> typeVariable = TypeInfoFactoryTest.GenericClass.class.getTypeParameters()[0];
        TypeInfoFactory.getActualType(typeVariable, TypeInfoFactoryTest.GenericClass.class, TypeInfoFactoryTest.GenericClass.class);
    }

    @Test
    public void testGetActualType_WildcardType() {
        WildcardType type = TypeInfoFactoryTest.GenericClass.class.getTypeParameters()[0].getBounds()[0];
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(type, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        TypeInfoFactory.getActualType(new Object(), TypeInfoFactoryTest.class, TypeInfoFactoryTest.class);
    }

    private static class GenericClass<T> {
        private T[] array;
        private T generic;

        public GenericClass() {
        }

        public T[] getClassArrayType() {
            return array;
        }

        public T getClassType() {
            return generic;
        }
    }
}
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    private TypeInfoArray typeInfoArray;
    private TypeInfo fieldType;

    @Before
    public void setUp() {
        Type type = new int[]{}.getClass();
        typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);

        Field field = null;
        try {
            field = Foo.class.getDeclaredField("a");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        ParameterizedType parameterizedType = (ParameterizedType) Foo.class.getGenericSuperclass();
        fieldType = TypeInfoFactory.getTypeInfoForField(field, parameterizedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArray_InvalidType() {
        Type type = null;
        TypeInfoFactory.getTypeInfoForArray(type);
    }

    @Test
    public void testGetTypeInfoForArray_ValidType() {
        assertNotNull(typeInfoArray);
    }

    @Test
    public void testGetTypeForField() {
        assertNotNull(fieldType);
    }

    @Test
    public void testGetTypeInfoForField() {
        assertNotNull(fieldType);
    }

    @Test
    public void testGetActualType() {
        Type typeToEvaluate = null;
        Type parentType = null;
        Class<?> rawParentClass = null;

        Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariableWithNoParameterizedType() {
        Field field = null;
        try {
            field = Foo.class.getDeclaredField("b");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        TypeInfoFactory.getTypeInfoForField(field, Foo.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariable_ParameterizedTypeMismatch() {
        Field field = null;
        try {
            field = Bar.class.getDeclaredField("c");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        TypeInfoFactory.getTypeInfoForField(field, Bar.class.getGenericSuperclass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        Type typeToEvaluate = null;
        Type parentType = null;
        Class<?> rawParentClass = null;

        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{Integer.class, String.class};
        Type parentType = null;
        Class<?> rawParentClass = null;

        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

        assertNotNull(result);
        assertEquals(Integer.class, result[0]);
        assertEquals(String.class, result[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_TypeVariableNotFound() {
        TypeVariable<?>[] types = new TypeVariable[]{};
        TypeVariable<?> type = null;

        TypeInfoFactory.getIndex(types, type);
    }

    @Test
    public void testGetIndex_TypeVariableFound() {
        TypeVariable<?>[] types = new TypeVariable[]{Foo.class.getTypeParameters()[0]};
        TypeVariable<?> type = Foo.class.getTypeParameters()[0];

        int result = TypeInfoFactory.getIndex(types, type);

        assertEquals(0, result);
    }

    private static class Foo<T> {
        private T a;
        private T b;
    }

    private static class Bar extends Foo<Integer> {
        private Integer c;
    }
}
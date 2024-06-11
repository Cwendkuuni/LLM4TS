import static org.junit.Assert.assertEquals;
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
        Type type = String[].class;
        TypeInfoArray arrayInfo = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(type, arrayInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = DummyClass.class.getDeclaredField("dummyField");
        Type typeDefiningF = DummyClass.class;
        TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        ParameterizedType expectedType = (ParameterizedType) DummyClass.class.getDeclaredField("dummyField").getGenericType();
        assertEquals(expectedType, info.getType());
    }

    @Test
    public void testGetActualTypeClass() {
        Type typeToEvaluate = String.class;
        Type parentType = null;
        Class<?> rawParentClass = null;

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualTypeParameterizedType() {
        Type typeToEvaluate = DummyClass.class.getTypeParameters()[0];
        Type parentType = DummyParameterizedClass.class;
        Class<?> rawParentClass = DummyParameterizedClass.class;

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

        assertEquals(Integer.class, actualType);
    }

    @Test
    public void testGetActualTypeGenericArrayType() {
        Type typeToEvaluate = DummyClass.class.getTypeParameters()[0].getBounds()[0];
        Type parentType = DummyParameterizedClass.class;
        Class<?> rawParentClass = DummyParameterizedClass.class;

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

        assertEquals(Integer.class, actualType);
    }

    @Test
    public void testGetActualTypeWildcardType() {
        Type typeToEvaluate = DummyClass.class.getTypeParameters()[0].getBounds()[0];
        Type parentType = DummyWildcardClass.class;
        Class<?> rawParentClass = DummyWildcardClass.class;

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

        assertEquals(String.class, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeInvalidType() {
        Type typeToEvaluate = new Type() {};
        Type parentType = null;
        Class<?> rawParentClass = null;

        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    private static class DummyClass<T> {
        private T dummyField;
    }

    private static class DummyParameterizedClass extends DummyClass<Integer> {}

    private static class DummyWildcardClass extends DummyClass<? extends String> {}
}
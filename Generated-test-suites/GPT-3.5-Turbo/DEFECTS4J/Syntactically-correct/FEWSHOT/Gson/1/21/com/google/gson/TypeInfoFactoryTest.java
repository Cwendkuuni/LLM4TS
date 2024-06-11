import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {
    @Test
    public void testGetTypeInfoForArray() {
        Type arrayType = String[].class;
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);

        assertEquals(arrayType, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField_ClassType() {
        try {
            Field field = ExampleClass.class.getDeclaredField("classField");
            Type typeDefiningF = ExampleClass.class;

            TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);

            assertEquals(ExampleClass.class, typeInfo.getType());
        } catch (NoSuchFieldException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetTypeInfoForField_ParameterizedType() {
        try {
            Field field = ExampleClass.class.getDeclaredField("parameterizedField");
            Type typeDefiningF = ExampleClass.class;

            TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);

            assertEquals(ParameterizedType.class, typeInfo.getType().getClass());
        } catch (NoSuchFieldException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetTypeInfoForField_GenericArrayType() {
        try {
            Field field = ExampleClass.class.getDeclaredField("genericArrayField");
            Type typeDefiningF = ExampleClass.class;

            TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);

            assertEquals(ParameterizedType.class, typeInfo.getType().getClass());
        } catch (NoSuchFieldException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetTypeInfoForField_TypeVariable() {
        try {
            Field field = ExampleClass.class.getDeclaredField("typeVariableField");
            Type typeDefiningF = ExampleClass.class;

            TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);

            assertEquals(TypeVariable.class, typeInfo.getType().getClass());
        } catch (NoSuchFieldException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetTypeInfoForField_WildcardType() {
        try {
            Field field = ExampleClass.class.getDeclaredField("wildcardField");
            Type typeDefiningF = ExampleClass.class;

            TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);

            assertEquals(WildcardType.class, typeInfo.getType().getClass());
        } catch (NoSuchFieldException e) {
            assertTrue(false);
        }
    }

    private static class ExampleClass {
        private Class<?> classField;
        private ParameterizedType parameterizedField;
        private GenericArrayType genericArrayField;
        private TypeVariable<?> typeVariableField;
        private WildcardType wildcardField;
    }
}
package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.TestFields;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a non-generic field
        Field nonGenericField = TestFields.class.getDeclaredField("nonGenericField");
        Assert.assertEquals(Object.class, genericMaster.getGenericType(nonGenericField));

        // Test with a generic field of String type
        Field genericField = TestFields.class.getDeclaredField("genericField");
        Assert.assertEquals(String.class, genericMaster.getGenericType(genericField));

        // Test with a generic field of List type
        Field listField = TestFields.class.getDeclaredField("listField");
        Assert.assertEquals(List.class, genericMaster.getGenericType(listField));

        // Test with a generic field of nested type
        Field nestedField = TestFields.class.getDeclaredField("nestedField");
        Assert.assertEquals(NestedGenericClass.class, genericMaster.getGenericType(nestedField));

        // Test with null field
        try {
            Assert.assertNull(genericMaster.getGenericType(null));
        } catch (NullPointerException e) {
            // Expected
        }
    }

    private static class TestFields {
        private String genericField;
        private List<String> listField;
        private NestedGenericClass nestedField;

        private static class NestedGenericClass<T> {
        }
    }

    @Test(expected = ClassCastException.class)
    public void testGetGenericTypeWithArray() {
        GenericMaster genericMaster = new GenericMaster();

        Field arrayField = TestFields.class.getDeclaredField("arrayField");
        Array array = (Array) arrayField.get(null); // assume array is not initialized
        Type genericType = arrayField.getGenericType();
        Type componentType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

        Class actual = (Class) genericMaster.getGenericType(new Object(){}.getClass().getDeclaredField("outerField"));
        Class expected = Array.class;
        Assert.assertEquals(expected, actual);

        Object outer = new Object(){};
        Object outerField = outer.getClass().getDeclaredField("outerField");
        outerField.setAccessible(true);
        Object arrayObject = outerField.get(outer);
        Class arrayType = array.getClass().getComponentType();

        actual = (Class) genericMaster.getGenericType(arrayObject.getClass().getDeclaredField("innerField"));
        expected = arrayType;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeWithInterface() {
        GenericMaster genericMaster = new GenericMaster();

        Class<TestInterface> interfaceType = TestInterface.class;
        Field field = TestInterface.class.getDeclaredField("field");

        Class actual = (Class) genericMaster.getGenericType(field);
        Class expected = TestInterface.class;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testGetGenericTypeWithIllegalAccess() {
        GenericMaster genericMaster = new GenericMaster();

        Field illegalAccessField = ReflectionUtils.getIllegalAccessField(TestFields.class, "illegalAccessField");

        Assert.assertThrows(IllegalAccessException.class, () -> genericMaster.getGenericType(illegalAccessField));
    }

    @Test
    public void testGetGenericTypeWithFieldAnnotations() {
        GenericMaster genericMaster = new GenericMaster();

        Field annotatedField = TestFields.class.getDeclaredField("annotatedField");
        annotatedField.setAccessible(true);

        Class actual = (Class) genericMaster.getGenericType(annotatedField);
        Class expected = String[].class;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeWithTypeVariable() {
        GenericMaster genericMaster = new GenericMaster();

        Class<TestTypeVariable> typeVariableClass = TestTypeVariable.class;
        Field typeVariableField = typeVariableClass.getDeclaredField("typeVariable");
        typeVariableField.setAccessible(true);

        Class actual = (Class) genericMaster.getGenericType(typeVariableField);
        Class expected = TypeVariable.class;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeWithMethod() {
        GenericMaster genericMaster = new GenericMaster();

        Class testClass = TestMethods.class;
        Method method = testClass.getDeclaredMethod("methodWithGenericReturnType");
        method.setAccessible(true);

        Class actual = (Class) genericMaster.getGenericType(method.getReturnType());
        Class expected = List.class;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeWithArrayOfPrimitives() {
        GenericMaster genericMaster = new GenericMaster();

        Class byteArrayClass = byte[].class;
        Field byteArrayField = byte[].class.getDeclaredField("componentType");
        byteArrayField.setAccessible(true);

        Class actual = (Class) genericMaster.getGenericType(byteArrayField.get(null));
        Class expected = Byte.TYPE;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeWithAnonymousClass() {
        GenericMaster genericMaster = new GenericMaster();

        Class anonymousClass = new AnonymousClassWithGenericField().getClass();
        Field genericField = anonymousClass.getDeclaredField("genericField");
        genericField.setAccessible(true);

        Class actual = (Class) genericMaster.getGenericType(genericField);
        Class expected = Integer.TYPE;
        Assert.assertEquals(expected, actual);
    }

    private static class ReflectionUtils {
        @SuppressWarnings("unchecked")
        private static <T> Field getIllegalAccessField(Class<?> clazz, String name) {
            try {
                return (Field) clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class TestFields {
        private String genericField;
        private List<String> listField;
        private NestedGenericClass<Integer> nestedField;
        private String[] arrayField;
        private TestInterface testInterface;
        private Object outerField;
        private TypeVariable<?> typeVariable;

        private static class NestedGenericClass<T> {
            private T innerField;
        }

        private static class TestInterface {
            private String field;
        }

        private static class IllegalAccessField {
        }

        private static class AnnotatedField {
            @SuppressWarnings("unused")
            private String[] annotatedField;
        }

        private static class TypeVariable<T> {
        }
    }

    private static class TestMethods {
        @SuppressWarnings("unused")
        public List<String> methodWithGenericReturnType() {
            return null;
        }
    }

    private static class AnonymousClassWithGenericField {
        private Integer genericField;
    }
}
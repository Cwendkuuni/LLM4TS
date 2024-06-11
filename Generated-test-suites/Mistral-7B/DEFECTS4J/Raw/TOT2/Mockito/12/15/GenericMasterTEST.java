package org.mockito.internal.util.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldTestUtils;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field testField = null;

        Class actualType = genericMaster.getGenericType(testField);

        Assertions.assertNull(actualType);
    }

    @Test
    public void testGetGenericTypeWithNonParameterizedType() {
        GenericMaster genericMaster = new GenericMaster();
        Field testField = FieldTestUtils.getField(GenericMaster.class, "testField");

        testField.setAccessible(true);
        testField.set(genericMaster, null);

        Class actualType = genericMaster.getGenericType(testField);

        Assertions.assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericTypeWithMultipleGenericTypes() {
        GenericMaster genericMaster = new GenericMaster();
        Field testField = FieldTestUtils.getField(GenericMaster.class, "testField");

        Class<List<String>> expectedType = List.class;
        Type genericType = testField.getGenericType();
        if (genericType != null && genericType instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            Class<?>[] actualTypeArguments = new Class[actualTypes.length];
            for (int i = 0; i < actualTypes.length; i++) {
                actualTypeArguments[i] = (Class<?>) actualTypes[i];
            }
            expectedType = (Class<List<String>>) Array.newInstance(List.class, 0).getClass()
                    .getGenericSuperclass();
            Assertions.assertArrayEquals(expectedType.getGenericComponentType().getActualTypeArguments(),
                    actualTypeArguments);
        }

        Class actualType = genericMaster.getGenericType(testField);

        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithArrayType() {
        GenericMaster genericMaster = new GenericMaster();
        Field testField = FieldTestUtils.getField(GenericMaster.class, "testField");

        Class<?> expectedType = int[].class;
        Class actualType = genericMaster.getGenericType(testField);

        if (expectedType.isArray()) {
            expectedType = expectedType.getComponentType();
        }

        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithNestedGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field testField = FieldTestUtils.getField(GenericMaster.class, "testField");

        Class<Map<List<String>, Map<String, Integer>>> expectedType = Map.class;
        Type genericType = testField.getGenericType();
        if (genericType != null && genericType instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            Class<?> keyType = (Class<?>) actualTypes[0];
            Class<?> valueType = (Class<?>) actualTypes[1];

            if (keyType.isAssignableFrom(List.class)) {
                Type keyTypes = ((ParameterizedType) keyType.getGenericSuperclass()).getActualTypeArguments()[0];
                expectedType = (Class<Map<List<String>, Map<String, Integer>>>) Array.newInstance(Map.class, 0).getClass()
                        .getGenericSuperclass();
                Assertions.assertArrayEquals(new Class[] { List.class, Map.class },
                        expectedType.getTypeParameters());
                Assertions.assertEquals(List.class, keyType);
                Assertions.assertEquals(Map.class, valueType);
            }
        }

        Class actualType = genericMaster.getGenericType(testField);

        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithComplexType() {
        GenericMaster genericMaster = new GenericMaster();
        Field testField = FieldTestUtils.getField(GenericMaster.class, "testField");

        Class<Map<List<List<String>>, Map<Map<String, String>, Map<Integer, String>>>> expectedType = Map.class;
        Type genericType = testField.getGenericType();
        if (genericType != null && genericType instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            for (int i = 0; i < actualTypes.length; i++) {
                Type type = actualTypes[i];
                if (type instanceof ParameterizedType) {
                    Type[] innerTypes = ((ParameterizedType) type).getActualTypeArguments();
                    expectedType = (Class<Map<List<List<String>>, Map<Map<String, String>, Map<Integer, String>>>>) Array.newInstance(Map.class, 0).getClass()
                            .getGenericSuperclass();
                    Assertions.assertArrayEquals(new Class[] { List.class, List.class, Map.class, Map.class },
                            expectedType.getTypeParameters());
                    Assertions.assertEquals(List.class, innerTypes[0].getTypeName().substring(0, innerTypes[0].getTypeName().lastIndexOf('>')).replaceAll("\\.", "").getClass());
                    Assertions.assertEquals(List.class, innerTypes[1].getTypeName().substring(0, innerTypes[1].getTypeName().lastIndexOf('>')).replaceAll("\\.", "").getClass());
                    Assertions.assertEquals(Map.class, innerTypes[2].getTypeName().substring(0, innerTypes[2].getTypeName().lastIndexOf('>')).replaceAll("\\.", "").getClass());
                    Assertions.assertEquals(Map.class, innerTypes[3].getTypeName().substring(0, innerTypes[3].getTypeName().lastIndexOf('>')).replaceAll("\\.", "").getClass());
                }
            }
        }

        Class actualType = genericMaster.getGenericType(testField);

        Assertions.assertEquals(expectedType, actualType);
    }
}
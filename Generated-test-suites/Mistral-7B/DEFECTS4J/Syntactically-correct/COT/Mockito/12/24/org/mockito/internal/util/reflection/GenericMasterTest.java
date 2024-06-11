package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = TestClassWithGenericField.class;
        Field field = clazz.getDeclaredField("genericField");

        // Test with a generic field
        Class expectedType = Integer.class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);

        // Test with a non-generic field
        field = clazz.getDeclaredField("nonGenericField");
        assertNull(genericMaster.getGenericType(field));

        // Test with null field
        assertNull(genericMaster.getGenericType(null));

        // Test with an array field
        Class arrayType = Integer[].class;
        Field arrayField = clazz.getDeclaredField("arrayField");
        arrayField.setAccessible(true);
        Object array = new Object[]{1, 2, 3};
        arrayField.set(new TestClassWithGenericAndArrayField(), array);
        Class expectedArrayType = Integer[].class;
        Class actualArrayType = genericMaster.getGenericType(arrayField);
        assertEquals(expectedArrayType, actualArrayType);

        // Test with a field with multiple type arguments
        Class nestedClass = NestedClassWithGenericField.class;
        Field nestedField = nestedClass.getDeclaredField("genericNestedField");
        Class expectedNestedType = String.class;
        Class actualNestedType = genericMaster.getGenericType(nestedField);
        assertEquals(expectedNestedType, actualNestedType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field nullField = null;
        genericMaster.getGenericType(nullField);
    }

    @Test
    public void testGetGenericTypeWithInvalidType() {
        GenericMaster genericMaster = new GenericMaster();
        Field invalidField = new Field("invalidField", Object.class) {
            @Override
            public Type getGenericType() {
                return null;
            }
        };
        assertNull(genericMaster.getGenericType(invalidField));
    }

    private static class TestClassWithGenericField {
        @SuppressWarnings("unused")
        private Integer genericField;
    }

    private static class TestClassWithGenericAndArrayField {
        @SuppressWarnings("unused")
        private Integer[] arrayField;
    }

    private static class NestedClassWithGenericField {
        @SuppressWarnings("unused")
        private String genericNestedField;
    }
}
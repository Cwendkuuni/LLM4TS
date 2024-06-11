package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = MyClassWithGenericField.class;
        Field field = clazz.getDeclaredField("myField");
        field.setAccessible(true);

        Class expectedType = Integer.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericTypeWithNoSuchFieldException() {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = MyClassWithNoSuchField.class;
        Field field = null; // no such field

        try {
            genericMaster.getGenericType(field);
        } catch (NoSuchFieldException e) {
            // expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWithIllegalArgumentException() {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = MyClassWithIllegalArgumentException.class;
        Field field = new Field("illegalFieldName") {
            @Override
            public Type getGenericType() {
                return null; // return null instead of ParameterizedType
            }
        };

        try {
            genericMaster.getGenericType(field);
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testGetGenericTypeWithPrimitiveType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = MyClassWithPrimitiveField.class;
        Field field = clazz.getDeclaredField("myField");
        field.setAccessible(true);

        Class expectedType = int.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithArrayType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = MyClassWithArrayField.class;
        Field field = clazz.getDeclaredField("myField");
        field.setAccessible(true);

        Class expectedType = int[].class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithNestedGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = MyClassWithNestedGenericField.class;
        Field field = clazz.getDeclaredField("myNestedField");
        field.setAccessible(true);

        Class expectedType = Pair.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithRawType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = MyClassWithRawType.class;
        Field field = clazz.getDeclaredField("myField");
        field.setAccessible(true);

        Class expectedType = Object.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    static class MyClassWithGenericField {
        private Integer myField;

        public Integer getMyField() {
            return myField;
        }

        public void setMyField(Integer myField) {
            this.myField = myField;
        }
    }

    static class MyClassWithNoSuchField {
        // no such field
    }

    static class MyClassWithIllegalArgumentException {
        private Object illegalFieldName;

        public Object getIllegalFieldName() {
            return illegalFieldName;
        }

        public void setIllegalFieldName(Object illegalFieldName) {
            this.illegalFieldName = illegalFieldName;
        }

        public static class Field<T> {
            private String name;

            public Field(String name) {
                this.name = name;
            }

            public Type getGenericType() {
                throw new IllegalArgumentException("unexpected error");
            }
        }
    }

    static class MyClassWithPrimitiveField {
        private int myField;

        public int getMyField() {
            return myField;
        }

        public void setMyField(int myField) {
            this.myField = myField;
        }
    }

    static class MyClassWithArrayField {
        private int[] myField;

        public int[] getMyField() {
            return myField;
        }

        public void setMyField(int[] myField) {
            this.myField = myField;
        }
    }

    static class MyClassWithNestedGenericField {
        private Pair<String, Integer> myNestedField;

        public Pair<String, Integer> getMyNestedField() {
            return myNestedField;
        }

        public void setMyNestedField(Pair<String, Integer> myNestedField) {
            this.myNestedField = myNestedField;
        }

        static class Pair<X, Y> {
            private X first;
            private Y second;

            public Pair(X first, Y second) {
                this.first = first;
                this.second = second;
            }
        }
    }

    static class MyClassWithRawType {
        private List myField;

        public List getMyField() {
            return myField;
        }

        public void setMyField(List myField) {
            this.myField = myField;
        }
    }
}
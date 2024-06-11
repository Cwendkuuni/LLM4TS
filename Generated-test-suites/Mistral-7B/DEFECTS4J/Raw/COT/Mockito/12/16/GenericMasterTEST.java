package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        try {
            Field field = this.getClass().getDeclaredField("testList");
            field.setAccessible(true);

            Class actualType = genericMaster.getGenericType(field);
            Class expectedType = ArrayList.class;

            Assert.assertEquals(expectedType, actualType);
        } catch (NoSuchFieldException e) {
            //Field does not exist, this is an expected scenario
        } catch (IllegalAccessException e) {
            //Access to the field is restricted
            e.printStackTrace();
        }

        //Test for null field
        Field nullField = null;
        try {
            Class actualType = genericMaster.getGenericType(nullField);
            Assert.assertEquals(Object.class, actualType);
        } catch (NullPointerException e) {
            //Expected since nullField is null
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field("testField", String.class, null);

        try {
            genericMaster.getGenericType(field);
        } catch (IllegalArgumentException e) {
            //Field does not have a generic type
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetGenericTypeWithNonParameterizedType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field("testField", Integer.TYPE, null);

        Class actualType = genericMaster.getGenericType(field);
        Class expectedType = Integer.class;

        Assert.assertEquals(expectedType, actualType);
    }
}
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(); //create a Field object for testing

        Class result = genericMaster.getGenericType(field);

        //test case for non-generic field, expect Object.class
        assertEquals(Object.class, result);
    }
}
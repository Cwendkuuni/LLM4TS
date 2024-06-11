package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {
    @Test
    public void testGetGenericType() {
        Class expectedResult = Object.class;
        Field field = null;
        GenericMaster genericMaster = new GenericMaster();
        assertEquals(expectedResult, genericMaster.getGenericType(field));
    }
}
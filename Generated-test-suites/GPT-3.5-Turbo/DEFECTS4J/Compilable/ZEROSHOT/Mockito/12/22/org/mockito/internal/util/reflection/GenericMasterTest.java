package org.mockito.internal.util.reflection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        // Create a field to test
        Field field = GenericMaster.class.getDeclaredField("fieldToTest");
        
        Class result = genericMaster.getGenericType(field);
        
        Assert.assertEquals(Object.class, result);
    }
}
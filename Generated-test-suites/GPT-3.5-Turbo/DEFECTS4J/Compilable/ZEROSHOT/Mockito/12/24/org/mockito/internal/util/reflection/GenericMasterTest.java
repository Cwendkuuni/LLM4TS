package org.mockito.internal.util.reflection;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("generic");

        // Test when field is null
        Class result = genericMaster.getGenericType(null);
        Assert.assertEquals(Object.class, result);

        // Test when field is not generic
        result = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, result);

        // Test when field is generic
        field = GenericMaster.class.getDeclaredField("genericTypeField");
        result = genericMaster.getGenericType(field);
        Assert.assertEquals(Integer.class, result);
    }
}

package org.mockito.internal.util.reflection;

import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_nonGenericField() {
        GenericMaster genericMaster = new GenericMaster();

        Field field = genericMaster.getClass().getDeclaredField("generic");

        Class result = genericMaster.getGenericType(field);

        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_genericField() {
        GenericMaster genericMaster = new GenericMaster();

        Field field = genericMaster.getClass().getDeclaredField("genericList");

        Class result = genericMaster.getGenericType(field);

        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_nestedGenericField() {
        GenericMaster genericMaster = new GenericMaster();

        Field field = genericMaster.getClass().getDeclaredField("nestedGenericList");

        Class result = genericMaster.getGenericType(field);

        Assert.assertEquals(Object.class, result);
    }
}

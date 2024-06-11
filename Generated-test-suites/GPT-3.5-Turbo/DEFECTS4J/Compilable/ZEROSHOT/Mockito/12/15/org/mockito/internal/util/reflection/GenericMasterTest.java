package org.mockito.internal.util.reflection;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

     private GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType() throws Exception {
        Field field = GenericMaster.class.getDeclaredField("generic");
        Type genericType = field.getGenericType();

        Class expected = Object.class;
        Class actual = genericMaster.getGenericType(field);

        Assert.assertEquals(expected, actual);
    }
}
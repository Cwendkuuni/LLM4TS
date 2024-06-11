package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

    private GenericMaster genericMaster;
    private Field field;

    @Before
    public void setUp() throws Exception {
        genericMaster = new GenericMaster();
        field = GenericMaster.class.getDeclaredField("field");
    }

    @Test
    public void testGetGenericTypeGenericField() {
        Class expected = String.class;
        field.setGenericType(expected);
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeNonGenericField() {
        Class expected = Object.class;
        field.setGenericType(null);
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(expected, actual);
    }

}
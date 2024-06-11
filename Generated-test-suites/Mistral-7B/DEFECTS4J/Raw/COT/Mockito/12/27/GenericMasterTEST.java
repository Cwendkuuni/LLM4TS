package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.reflect.Fields;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a generic field
        Field field = Fields.getField(GenericMaster.class, "someField", Integer.class, int.class);
        Class expected = Integer.class;
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(expected, actual);

        // Test with a non-generic field
        Field nonGenericField = Fields.getField(GenericMaster.class, "nonGenericField", String.class);
        Class expectedNonGeneric = Object.class;
        Class actualNonGeneric = genericMaster.getGenericType(nonGenericField);
        Assert.assertEquals(expectedNonGeneric, actualNonGeneric);

        // Test with null field
        Field nullField = null;
        Class expectedNull = Object.class;
        Class actualNull = genericMaster.getGenericType(nullField);
        Assert.assertEquals(expectedNull, actualNull);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field("someField", Integer.class); // create a fake field
        genericMaster.getGenericType(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWithNonParameterizedType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field("someField", Object.class); // create a fake field with Object.class type
        genericMaster.getGenericType(field);
    }
}
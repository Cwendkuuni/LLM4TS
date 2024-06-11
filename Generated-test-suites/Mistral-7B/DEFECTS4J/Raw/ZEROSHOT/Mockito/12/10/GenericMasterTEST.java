package org.junit.tests.reflection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.reflection.GenericMaster;
import org.mockito.internal.reflection.FieldTestDouble;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a generic field
        Field field = FieldTestDouble.class.getDeclaredField("fieldOfTypeInteger");
        field.setAccessible(true);
        Class expected = Integer.TYPE;
        Class actual = genericMaster.getGenericType(field);
        Assert.assertEquals(expected, actual);

        // Test with a non-generic field
        Field nonGenericField = FieldTestDouble.class.getDeclaredField("nonGenericField");
        nonGenericField.setAccessible(true);
        Class expectedNonGeneric = Object.class;
        Class actualNonGeneric = genericMaster.getGenericType(nonGenericField);
        Assert.assertEquals(expectedNonGeneric, actualNonGeneric);

        // Test with null field
        Field nullField = FieldTestDouble.class.getDeclaredField("nullField");
        nullField.setAccessible(true);
        Class expectedNull = Object.class;
        Class actualNull = genericMaster.getGenericType(nullField);
        Assert.assertEquals(expectedNull, actualNull);

        // Test with a non-existent field
        Field nonExistentField = FieldTestDouble.class.getDeclaredField("nonExistentField");
        nonExistentField.setAccessible(true);
        Class expectedNonExistent = Object.class;
        Class actualNonExistent = genericMaster.getGenericType(nonExistentField);
        Assert.assertEquals(expectedNonExistent, actualNonExistent);

        // Test with a field of type Object
        Field objectField = FieldTestDouble.class.getDeclaredField("objectField");
        objectField.setAccessible(true);
        Class expectedObject = Object.class;
        Class actualObject = genericMaster.getGenericType(objectField);
        Assert.assertEquals(expectedObject, actualObject);
    }
}

class FieldTestDouble {
    int fieldOfTypeInteger;
    Object nonGenericField;
    Object objectField;
    Object nullField;
    Object nonExistentField;
}
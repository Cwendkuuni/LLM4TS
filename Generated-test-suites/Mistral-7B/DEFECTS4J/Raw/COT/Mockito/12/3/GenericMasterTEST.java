package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        // Given - create an instance of GenericMaster and a Field object with a known generic type
        GenericMaster genericMaster = new GenericMaster();
        Field field = this.getClass().getDeclaredField("testField");

        // When - call getGenericType method with the provided Field object
        Class expectedGenericType = String.class;
        Class actualGenericType = genericMaster.getGenericType(field);

        // Then - assert that the returned generic type matches the expected one
        assertEquals(expectedGenericType, actualGenericType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWithNullField() {
        // Given - call getGenericType method with a null Field object
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;

        // Then - assert that the method throws an IllegalArgumentException
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericTypeWithNonExistentField() throws NoSuchFieldException {
        // Given - create an instance of GenericMaster
        GenericMaster genericMaster = new GenericMaster();

        // When - call getGenericType method with a non-existent Field object
        Field nonExistentField = new Field("nonExistentField", Object.class, this.getClass());

        // Then - assert that the method throws a NoSuchFieldException
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
        // Given - create an instance of GenericMaster and a non-generic Field object
        GenericMaster genericMaster = new GenericMaster();
        Field nonGenericField = this.getClass().getDeclaredField("nonGenericField");

        // When - call getGenericType method with the provided Field object
        Class expectedGenericType = Object.class;
        Class actualGenericType = genericMaster.getGenericType(nonGenericField);

        // Then - assert that the returned generic type matches the expected one
        assertEquals(expectedGenericType, actualGenericType);
    }
}
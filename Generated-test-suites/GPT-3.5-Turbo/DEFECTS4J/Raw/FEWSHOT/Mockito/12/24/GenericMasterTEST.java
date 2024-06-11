
package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("nonGenericField");

        Class expected = Object.class;
        Class actual = genericMaster.getGenericType(field);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("genericField");

        Class expected = String.class;
        Class actual = genericMaster.getGenericType(field);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericTypeWithNestedGenericField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("nestedGenericField");

        Class expected = Integer.class;
        Class actual = genericMaster.getGenericType(field);

        assertEquals(expected, actual);
    }

    private String nonGenericField;
    private Field<String> genericField;
    private Field<Field<Integer>> nestedGenericField;

}

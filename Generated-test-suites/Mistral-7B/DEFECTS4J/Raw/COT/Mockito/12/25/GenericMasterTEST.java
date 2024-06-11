package org.mockito.internal.util.reflection.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.ReflectUtils; // Assuming ReflectUtils is the class that contains the Field object

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Field field = ReflectUtils.getDeclaredField(GenericMaster.class, "someField"); // Assuming "someField" is a field in GenericMaster

        Class expectedType = String.class; // Replace with the expected generic type
        Class actualType = new GenericMaster().getGenericType(field);

        Assert.assertEquals(expectedType, actualType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWhenFieldIsNull() {
        Assert.assertNull(new GenericMaster().getGenericType(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWhenFieldHasNoGenericType() {
        Field field = ReflectUtils.getDeclaredField(GenericMaster.class, "someFieldWithoutGenericType"); // Assuming "someFieldWithoutGenericType" is a field in GenericMaster without generic type
        new GenericMaster().getGenericType(field);
    }

    @Test(expected = ClassCastException.class)
    public void testGetGenericTypeWhenGenericTypeIsNotParameterizedType() {
        Field field = ReflectUtils.getDeclaredField(GenericMaster.class, "someFieldWithWrongGenericType"); // Assuming "someFieldWithWrongGenericType" is a field in GenericMaster with a generic type that is not a ParameterizedType
        new GenericMaster().getGenericType(field);
    }

    @Test
    public void testGetGenericTypeWhenFieldHasMultipleGenericTypes() {
        Field field = ReflectUtils.getDeclaredField(GenericMaster.class, "someFieldWithMultipleGenerics"); // Assuming "someFieldWithMultipleGenerics" is a field in GenericMaster with multiple generic types
        Class actualType = new GenericMaster().getGenericType(field);

        Class expectedType1 = Integer.class;
        Class expectedType2 = String.class;

        Assert.assertArrayEquals(new Class[]{expectedType1, expectedType2}, new Object[]{actualType.getComponentType(), actualType.getTypeArguments()[1]});
    }
}
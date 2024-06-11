package org.sourceforge.ifx.basetypes;

import org.junit.Assert;
import org.junit.Test;

public class IFXObjectTest {

    @Test
    public void testEqualsWhenSameObjectThenTrue() {
        IFXObject obj = new IFXObject();
        boolean result = obj.equals(obj);
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWhenDifferentObjectThenFalse() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWhenNullObjectThenFalse() {
        IFXObject obj = new IFXObject();
        boolean result = obj.equals(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWhenDifferentInstanceOfObjectThenFalse() {
        IFXObject obj = new IFXObject();
        boolean result = obj.equals(new Object());
        Assert.assertFalse(result);
    }

    @Test
    public void testToStringWhenDefaultObjectThenStringRepresentation() {
        IFXObject obj = new IFXObject();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWhenCustomObjectThenStringRepresentation() {
        IBaseType baseType = new IBaseType() {
            @Override
            public String getString() {
                return "customString";
            }
        };
        IFXObject obj = new IFXObject();
        obj.setBaseType(baseType);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  baseType = customString\n" +
                "}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWhenNullArrayThenStringRepresentation() {
        IFXObject obj = new IFXObject();
        obj.setArray1(null);
        obj.setArray2(null);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  array1 = NULL,\n" +
                "  array2 = NULL\n" +
                "}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWhenEmptyArrayThenStringRepresentation() {
        IFXObject[] array1 = new IFXObject[0];
        IFXObject[] array2 = new IFXObject[0];
        IFXObject obj = new IFXObject();
        obj.setArray1(array1);
        obj.setArray2(array2);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  array1 = [\n" +
                "  ],\n" +
                "  array2 = [\n" +
                "  ]\n" +
                "}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWhenNonEmptyArrayThenStringRepresentation() {
        IFXObject[] array1 = new IFXObject[2];
        IFXObject[] array2 = new IFXObject[2];
        IFXObject obj = new IFXObject();
        obj.setArray1(array1);
        obj.setArray2(array2);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "  array1 = [\n" +
                "    [0]:org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "    },\n" +
                "    [1]:org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "    }\n" +
                "  ],\n" +
                "  array2 = [\n" +
                "    [0]:org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "    },\n" +
                "    [1]:org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String result = obj.toString();
        Assert.assertEquals(expected, result);
    }
}
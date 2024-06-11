package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IFXObjectTest {
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = null;

        boolean result = obj1.equals(obj2);
        assertEquals(false, result);

        obj2 = new IFXObject();
        result = obj1.equals(obj2);
        assertEquals(true, result);
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();

        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        String result = obj.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToString_WithNestedObject() {
        IFXObject obj = new IFXObject();

        IFXObject nestedObj = new IFXObject();
        obj.setNestedObject(nestedObj);

        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  nestedObject = org.sourceforge.ifx.basetypes.IFXObject = {\n  }\n}";
        String result = obj.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToString_WithArray() {
        IFXObject obj = new IFXObject();

        IFXObject[] array = new IFXObject[2];
        array[0] = new IFXObject();
        array[1] = new IFXObject();
        obj.setArray(array);

        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  array = [\n    [0]:org.sourceforge.ifx.basetypes.IFXObject = {\n    },\n    [1]:org.sourceforge.ifx.basetypes.IFXObject = {\n    }\n  ]\n}";
        String result = obj.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToString_WithIBaseType() {
        IFXObject obj = new IFXObject();

        IBaseType baseType = new IBaseType();
        baseType.setString("value");
        obj.setBaseType(baseType);

        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n  baseType = value\n}";
        String result = obj.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testDoCheckEquals_BothNull() {
        IFXObject obj1 = null;
        IFXObject obj2 = null;

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(true, result);
    }

    @Test
    public void testDoCheckEquals_OneNull() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = null;

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(false, result);
    }

    @Test
    public void testDoCheckEquals_DifferentClass() {
        IFXObject obj1 = new IFXObject();
        IBaseType obj2 = new IBaseType();

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(false, result);
    }

    @Test
    public void testDoCheckEquals_MatchingObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(true, result);
    }

    @Test
    public void testDoCheckEquals_NestedObjectEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        obj1.setNestedObject(obj2);

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(true, result);
    }

    @Test
    public void testDoCheckEquals_DifferentNestedObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        IFXObject nested1 = new IFXObject();
        IFXObject nested2 = new IFXObject();
        obj1.setNestedObject(nested1);
        obj2.setNestedObject(nested2);

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(false, result);
    }

    @Test
    public void testDoCheckEquals_ArrayEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        IFXObject[] array1 = new IFXObject[2];
        array1[0] = new IFXObject();
        array1[1] = new IFXObject();
        obj1.setArray(array1);

        IFXObject[] array2 = new IFXObject[2];
        array2[0] = new IFXObject();
        array2[1] = new IFXObject();
        obj2.setArray(array2);

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(true, result);
    }

    @Test
    public void testDoCheckEquals_DifferentArrayLength() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        IFXObject[] array1 = new IFXObject[2];
        array1[0] = new IFXObject();
        array1[1] = new IFXObject();
        obj1.setArray(array1);

        IFXObject[] array2 = new IFXObject[1];
        array2[0] = new IFXObject();
        obj2.setArray(array2);

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(false, result);
    }

    @Test
    public void testDoCheckEquals_DifferentArrayObjects() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        IFXObject[] array1 = new IFXObject[2];
        array1[0] = new IFXObject();
        array1[1] = new IFXObject();
        obj1.setArray(array1);

        IFXObject[] array2 = new IFXObject[2];
        array2[0] = new IFXObject();
        array2[1] = new IFXObject();
        obj2.setArray(array2);

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(false, result);
    }

    @Test
    public void testDoCheckEquals_WithIBaseTypeEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        IBaseType baseType1 = new IBaseType();
        IBaseType baseType2 = new IBaseType();
        baseType1.setString("value");
        baseType2.setString("value");
        obj1.setBaseType(baseType1);
        obj2.setBaseType(baseType2);

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(true, result);
    }

    @Test
    public void testDoCheckEquals_WithIBaseTypeNotEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();

        IBaseType baseType1 = new IBaseType();
        IBaseType baseType2 = new IBaseType();
        baseType1.setString("value1");
        baseType2.setString("value2");
        obj1.setBaseType(baseType1);
        obj2.setBaseType(baseType2);

        boolean result = obj1.doCheckEquals(obj1, obj2);
        assertEquals(false, result);
    }

    @Test
    public void testTab_WithZeroLevel() {
        IFXObject obj = new IFXObject();

        String expected = "";
        String result = obj.tab(0);
        assertEquals(expected, result);
    }

    @Test
    public void testTab_WithPositiveLevel() {
        IFXObject obj = new IFXObject();

        String expected = "  ";
        String result = obj.tab(1);
        assertEquals(expected, result);
    }
}
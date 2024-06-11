package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws ParseException {
        Object expected = "expectedValue";
        Object actual = TypeHandler.createValue("expectedValue", expected.getClass());
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueInvalidInputType() {
        Assert.assertNull(TypeHandler.createValue("value", int.class));
    }

    @Test
    public void testCreateValueNullInput() {
        Assert.assertNull(TypeHandler.createValue(null, String.class));
    }

    @Test
    public void testCreateValueEmptyString() {
        Assert.assertNull(TypeHandler.createValue("", String.class));
    }

    @Test
    public void testCreateValueInvalidClassName() {
        Assert.assertThrows(ParseException.class, () -> TypeHandler.createValue("InvalidClass", String.class));
    }

    @Test
    public void testCreateValueNonExistentClass() {
        Assert.assertThrows(ParseException.class, () -> TypeHandler.createValue("com.example.NonExistentClass", String.class));
    }

    @Test
    public void testCreateValueClassWithNoDefaultConstructor() {
        Assert.assertThrows(ParseException.class, () -> TypeHandler.createValue("com.example.MyClass", MyClass.class));
    }

    @Test
    public void testCreateValueNonInstantiableClass() {
        Assert.assertThrows(ParseException.class, () -> TypeHandler.createValue("java.lang.String", String[].class));
    }

    @Test
    public void testCreateValueNullReturnedValue() {
        Assert.assertNull(TypeHandler.createValue("", String.class));
    }

    @Test
    public void testCreateNumberLongString() {
        Long expected = 1L;
        Number actual = TypeHandler.createNumber("1");
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberDoubleString() {
        Double expected = 1.0;
        Number actual = TypeHandler.createNumber("1.0");
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberNegativeNumber() {
        Long expected = -1L;
        Number actual = TypeHandler.createNumber("-1");
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberVeryLargeNumber() {
        Long expected = Long.MAX_VALUE;
        Number actual = TypeHandler.createNumber(String.valueOf(Long.MAX_VALUE));
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberLeadingZeros() {
        Long expected = 1L;
        Number actual = TypeHandler.createNumber("001");
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberExponent() {
        Double expected = 1.2e3;
        Number actual = TypeHandler.createNumber("1.2e3");
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberInvalidString() {
        Assert.assertThrows(ParseException.class, () -> TypeHandler.createNumber("abc"));
    }

    @Test
    public void testCreateClassValidClassName() throws ParseException {
        String expected = "com.example.MyClass";
        Class<?> actual = TypeHandler.createClass(expected);
        assertEquals(expected, actual.getName());
    }

    @Test
    public void testCreateClassInvalidClassName() {
        Assert.assertThrows(ParseException.class, () -> TypeHandler.createClass("com.example.MyClass$Invalid"));
    }

    @Test
    public void testCreateClassNonExistentClass() {
        Assert.assertThrows(ParseException.class, () -> TypeHandler.createClass("com.example.NonExistentClass"));
    }

    @Test
    public void testCreateObjectValidClassName() throws ParseException, InstantiationException, IllegalAccessException {
        String expected = "com.example.MyObject";
        Object actual = TypeHandler.createObject(expected);
        assertEquals(expected, actual.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() {
        TypeHandler.createObject("com.example.MyObject$Invalid");
    }
}
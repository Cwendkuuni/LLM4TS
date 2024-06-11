package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() throws ParseException {
        String str = "test";
        Object obj = new Object();
        Object expected = "test";
        Object actual = TypeHandler.createValue(str, obj.getClass());
        assertEquals(expected, actual);

        // Test edge cases
        String emptyStr = "";
        String nullStr = null;
        assertNull(TypeHandler.createValue(nullStr, obj.getClass()));
        assertNull(TypeHandler.createValue(emptyStr, obj.getClass()));

        // Test exception scenarios
        try {
            TypeHandler.createValue("1a", Object.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            TypeHandler.createValue("1234567890123456789012345678901234567890", String.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }

        try {
            TypeHandler.createValue("invalidClass", Object.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "123";
        Number expected = 123;
        Number actual = TypeHandler.createNumber(str);
        assertEquals(expected, actual);

        // Test edge cases
        String whitespaceStr = "  123  ";
        String decimalStr = "123.";
        String noDigitsStr = ".123";
        Number whitespaceNumber = (Number) Long.parseLong(whitespaceStr.trim());
        Number decimalNumber = Double.valueOf(decimalStr);
        Number noDigitsNumber = Double.NaN;
        assertEquals(whitespaceNumber, TypeHandler.createNumber(whitespaceStr));
        assertEquals(decimalNumber, TypeHandler.createNumber(decimalStr));
        assertEquals(noDigitsNumber, TypeHandler.createNumber(noDigitsStr));

        // Test exception scenarios
        try {
            TypeHandler.createNumber("abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }

        try {
            TypeHandler.createNumber("123ABC");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Class<?> expectedClass = String.class;
        Object actual = TypeHandler.createObject(classname);
        assertTrue(expectedClass.isInstance(actual));

        // Test edge cases
        String nullClassname = null;
        String emptyClassname = "";
        assertNull(TypeHandler.createObject(nullClassname));
        assertNull(TypeHandler.createObject(emptyClassname));

        // Test exception scenarios
        try {
            TypeHandler.createObject("java.lang.Xyz");
            fail("Expected ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }
}
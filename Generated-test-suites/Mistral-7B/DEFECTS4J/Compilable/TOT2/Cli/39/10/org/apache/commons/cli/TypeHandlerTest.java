package org.apache.commons.cli;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    @Test
    public void testCreateValueStringObject() throws ParseException {
        Object result = TypeHandler.createValue("testString", Object.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValueStringString() throws ParseException {
        String result = (String) TypeHandler.createValue("testString", String.class);
        assertEquals("testString", result);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException, ClassNotFoundException {
        Class<?> clazz = Class.forName("java.lang.Integer");
        Object result = TypeHandler.createValue("123", clazz);
        assertEquals(123, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidString() throws ParseException {
        TypeHandler.createValue("invalidValue", Object.class);
        fail("Expected ParseException but it didn't occur.");
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateValueUnknownClass() throws ParseException, ClassNotFoundException {
        TypeHandler.createValue("com.unknown.UnknownClass", Object.class);
        fail("Expected ClassNotFoundException but it didn't occur.");
    }
}
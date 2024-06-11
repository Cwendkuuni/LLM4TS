package org.apache.commons.cli;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws ParseException {
        Object result = TypeHandler.createValue("testString", Object.class);
        Assert.assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringObjectInvalidType() throws ParseException {
        TypeHandler.createValue("testString", int.class);
    }

    @Test
    public void testCreateValueStringString() throws ParseException {
        String expected = "testString";
        String result = (String) TypeHandler.createValue("testString", String.class);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException {
        Class<?> clazz = Integer.TYPE;
        Object result = TypeHandler.createValue("123", clazz);
        Integer expected = 123;
        Assert.assertEquals(expected.getClass(), result.getClass());
        Assert.assertEquals(expected.intValue(), ((Number) result).intValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidString() throws ParseException {
        TypeHandler.createValue("invalidString", String.class);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassInvalidType() throws ParseException {
        TypeHandler.createValue("123", Double.TYPE);
    }
}
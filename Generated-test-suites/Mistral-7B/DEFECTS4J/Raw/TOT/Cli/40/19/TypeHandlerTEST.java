import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateStringValue() throws ParseException
    {
        String str = "test";
        String expected = "test";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateObjectValueNullClassname() throws ParseException
    {
        String classname = null;
        Class<?> clazz = Object.class;
        try
        {
            Object actual = TypeHandler.createObject(classname);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateObjectValueInvalidClassname() throws ParseException
    {
        String classname = "InvalidClass";
        Class<?> clazz = Object.class;
        try
        {
            Object actual = TypeHandler.createObject(classname);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            String expectedMessage = "Unable to find the class: InvalidClass";
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testCreateObjectValueSuccess() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<?> clazz = Date.class;
        Object actual = TypeHandler.createObject(classname);
        assertInstanceOf(Date.class, actual);
    }

    @Test
    public void testCreateNumberValueStringWithDecimalPoint() throws ParseException
    {
        String str = "3.14";
        Double expected = 3.14;
        Class<?> clazz = Double.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberValueStringWithoutDecimalPoint() throws ParseException
    {
        String str = "3";
        Long expected = 3L;
        Class<?> clazz = Long.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberValueNonNumericString() throws ParseException
    {
        String str = "xyz";
        Class<?> clazz = Long.class;
        Object actual = TypeHandler.createValue(str, clazz);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateDateValueValidDateString() throws ParseException, ParseException // Nested exception is intended
    {
        String str = "2023-03-16";
        Date expected = new Date(1678988800000L); // March 16, 2023, 12:00:00 AM
        Class<?> clazz = Date.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateDateValueInvalidDateString() throws ParseException, ParseException // Nested exception is intended
    {
        String str = "2023-3-16";
        Class<?> clazz = Date.class;
        Object actual = TypeHandler.createValue(str, clazz);
        fail("Expected ParseException");
    }

    @Test
    public void testCreateURLValueValidURLString() throws ParseException
    {
        String str = "http://example.com";
        URL expected = new URL("http", "example.com", -1, "/");
        Class<?> clazz = URL.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLValueInvalidURLString() throws ParseException
    {
        String str = "http://example.com:80/path#fragment";
        Class<?> clazz = URL.class;
        Object actual = TypeHandler.createValue(str, clazz);
        fail("Expected ParseException");
    }
}
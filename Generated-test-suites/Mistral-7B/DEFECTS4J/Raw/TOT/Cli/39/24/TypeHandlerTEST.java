import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.types.PatternOptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for the TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Tests the createValue method with a String object.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    /**
     * Tests the createValue method with a non-existent class.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueNonExistentClass()
    {
        String classname = "org.apache.commons.cli.NonExistentClass";
        TypeHandler.createValue(classname, String.class);
    }

    /**
     * Tests the createValue method with a Long number.
     */
    @Test
    public void testCreateValueLong()
    {
        String str = "123";
        Object obj = TypeHandler.createValue(str, Long.class);
        assertEquals(Long.valueOf(123), obj);
    }

    /**
     * Tests the createValue method with a Double number.
     */
    @Test
    public void testCreateValueDouble()
    {
        String str = "123.45";
        Object obj = TypeHandler.createValue(str, Double.class);
        assertEquals(Double.valueOf(123.45), obj);
    }

    /**
     * Tests the createValue method with an invalid number.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueInvalidNumber()
    {
        String str = "abc";
        TypeHandler.createValue(str, Number.class);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.DATE_VALUE class.
     * This method is not yet implemented and always throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate()
    {
        String str = "2022-01-01";
        TypeHandler.createValue(str, Date.class);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.URL_VALUE class.
     */
    @Test
    public void testCreateValueURL()
    {
        String str = "http://localhost:8080/";
        Object obj = TypeHandler.createValue(str, URL.class);
        assertEquals(new URL("http://localhost:8080/"), obj);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.FILE_VALUE class.
     */
    @Test
    public void testCreateValueFile()
    {
        String str = "/path/to/file.txt";
        Object obj = TypeHandler.createValue(str, File.class);
        assertEquals(new File("/path/to/file.txt"), obj);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.EXISTING_FILE_VALUE class.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueExistingFile()
    {
        String str = "/path/to/non-existent-file.txt";
        TypeHandler.createValue(str, File.class);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.FILES_VALUE class.
     * This method is not yet implemented and always throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles()
    {
        String str = "/path/to/file1.txt, /path/to/file2.txt";
        TypeHandler.createValue(str, File[].class);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.CLASS_VALUE class.
     */
    @Test
    public void testCreateValueClass()
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createValue(classname, Class.class);
        assertEquals(Class.forName(classname), obj);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.OBJECT_VALUE class.
     */
    @Test
    public void testCreateValueObject()
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createValue(classname, Object.class);
        assertNull(obj);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.NUMBER_VALUE class.
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Object obj = TypeHandler.createValue(str, Number[].class);
        assertNull(obj);
    }

    /**
     * Tests the createValue method with the PatternOptionBuilder.DATE_VALUE class.
     * This method is not yet implemented and always throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate2()
    {
        String str = "2022-01-01";
        TypeHandler.createValue(str, Date[].class);
    }

    /**
     * Tests the createValue method with an invalid class.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass()
    {
        String clazz = "java.util.Date[]";
        TypeHandler.createValue("test", Class.forName(clazz));
    }
}
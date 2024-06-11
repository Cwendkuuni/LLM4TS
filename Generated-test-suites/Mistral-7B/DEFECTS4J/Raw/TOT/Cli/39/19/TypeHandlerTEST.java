import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * TypeHandlerTest class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue(String, Object) method with String type.
     */
    @Test
    public void testCreateValueStringObject()
    {
        String str = "testString";
        Object obj = TypeHandler.createValue(str, String.class);
        Assertions.assertEquals(str, obj);

        // Edge case: empty string
        String emptyStr = "";
        obj = TypeHandler.createValue(emptyStr, String.class);
        Assertions.assertEquals(emptyStr, obj);

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, String.class));
    }

    /**
     * Test createValue(String, Object) method with Object type.
     */
    @Test
    public void testCreateValueStringObjectObject()
    {
        String str = "testString";
        Object obj = new Object() {};
        Object objValue = TypeHandler.createValue(str, obj.getClass());
        Assertions.assertNotSame(obj, objValue);
        Assertions.assertEquals(str, objValue.toString());

        // Edge case: empty string
        String emptyStr = "";
        objValue = TypeHandler.createValue(emptyStr, obj.getClass());
        Assertions.assertEquals("", objValue.toString());

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, obj.getClass()));
    }

    /**
     * Test createValue(String, Object) method with Class type.
     */
    @Test
    public void testCreateValueStringClass()
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assertions.assertEquals(str, obj);

        // Edge case: empty string
        String emptyStr = "";
        obj = TypeHandler.createValue(emptyStr, clazz);
        Assertions.assertEquals(emptyStr, obj);

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, clazz));

        // Test with PatternOptionBuilder.STRING_VALUE
        clazz = PatternOptionBuilder.STRING_VALUE;
        obj = TypeHandler.createValue(str, clazz);
        Assertions.assertEquals(str, obj);
    }

    /**
     * Test createValue(String, Object) method with Object type.
     */
    @Test
    public void testCreateValueStringObjectClass()
    {
        String str = "testClass";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assertions.assertEquals(str, obj.toString());

        // Edge case: empty string
        String emptyStr = "";
        obj = TypeHandler.createValue(emptyStr, clazz);
        Assertions.assertEquals("", obj.toString());

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, clazz));

        // Test with PatternOptionBuilder.OBJECT_VALUE
        clazz = PatternOptionBuilder.OBJECT_VALUE;
        Object testObject = new Object() {};
        obj = TypeHandler.createValue(str, clazz);
        Assertions.assertNotSame(testObject, obj);
        Assertions.assertEquals(str, obj.toString());
    }

    /**
     * Test createValue(String, Object) method with Number type.
     */
    @Test
    public void testCreateValueStringNumber()
    {
        String strLong = "12345678901234567890";
        Object obj = TypeHandler.createValue(strLong, Long.class);
        Long longNumber = (Long) obj;
        Assertions.assertEquals(12345678901234567890L, longNumber);

        String strDouble = "123.45678901234567890";
        obj = TypeHandler.createValue(strDouble, Double.class);
        Double doubleNumber = (Double) obj;
        Assertions.assertEquals(123.45678901234567890, doubleNumber);

        // Edge case: empty string
        String emptyStr = "";
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(emptyStr, Long.class));
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(emptyStr, Double.class));

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, Long.class));
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, Double.class));

        // Test with PatternOptionBuilder.NUMBER_VALUE
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        long numberLong = 12345678901234567890L;
        obj = TypeHandler.createValue(String.valueOf(numberLong), clazz);
        Assertions.assertEquals(numberLong, ((Number) obj).longValue());

        double numberDouble = 123.45678901234567890;
        obj = TypeHandler.createValue(String.valueOf(numberDouble), clazz);
        Assertions.assertEquals(numberDouble, ((Number) obj).doubleValue());
    }

    /**
     * Test createValue(String, Object) method with Date type.
     */
    @Test
    public void testCreateValueStringDate()
    {
        String dateString = "2022-12-31";
        Object obj = TypeHandler.createValue(dateString, Date.class);
        Date date = (Date) obj;
        Assertions.assertEquals("Fri Dec 31 00:00:00 CET 2022", date.toString());

        // Edge case: empty string
        String emptyStr = "";
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(emptyStr, Date.class));

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, Date.class));

        // Test with PatternOptionBuilder.DATE_VALUE
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;
        Date testDate = new Date(0);
        obj = TypeHandler.createValue(dateString, clazz);
        Assertions.assertEquals(testDate, obj);
    }

    /**
     * Test createValue(String, Object) method with Class type.
     */
    @Test
    public void testCreateValueStringClassClass()
    {
        String classname = "java.lang.String";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(classname, clazz);
        Assertions.assertEquals(classname, obj.toString());

        // Edge case: empty string
        String emptyStr = "";
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(emptyStr, clazz));

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, clazz));

        // Test with PatternOptionBuilder.CLASS_VALUE
        Class<?> testClass = Class.forName("java.lang.Integer");
        obj = TypeHandler.createValue(classname, testClass);
        Assertions.assertEquals(clazz.getName(), obj.getClass().getName());
    }

    /**
     * Test createValue(String, Object) method with File type.
     */
    @Test
    public void testCreateValueStringFile() throws IOException
    {
        String filePath = "path/to/existing/file.txt";
        File file = new File(filePath);
        Object obj = TypeHandler.createValue(filePath, File.class);
        Assertions.assertEquals(file, obj);

        // Edge case: invalid file path
        String invalidFilePath = "path/to/non-existent/file.txt";
        File invalidFile = new File(invalidFilePath);
        Assertions.assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue(invalidFilePath, File.class));

        // Test with PatternOptionBuilder.FILE_VALUE
        Class<?> clazz = File.class;
        File testFile = new File("testFile.txt");
        obj = TypeHandler.createValue(filePath, clazz);
        Assertions.assertEquals(testFile, obj);

        // Test with PatternOptionBuilder.EXISTING_FILE_VALUE
        String existingFilePath = "path/to/existing/file.txt";
        obj = TypeHandler.createValue(existingFilePath, clazz);
        Assertions.assertEquals(new File(existingFilePath), obj);
    }

    /**
     * Test createValue(String, Object) method with URL type.
     */
    @Test
    public void testCreateValueStringURL()
    {
        String urlString = "http://example.com";
        Object obj = TypeHandler.createValue(urlString, URL.class);
        URL url = (URL) obj;
        Assertions.assertEquals(urlString, url.toString());

        // Edge case: empty string
        String emptyStr = "";
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(emptyStr, URL.class));

        // Error case: null string
        String nullStr = null;
        Assertions.assertThrows(ParseException.class, () -> TypeHandler.createValue(nullStr, URL.class));

        // Test with PatternOptionBuilder.URL_VALUE
        Class<?> clazz = URL.class;
        URL testURL = new URL("http://example.com");
        obj = TypeHandler.createValue(urlString, clazz);
        Assertions.assertEquals(testURL, obj);
    }
}
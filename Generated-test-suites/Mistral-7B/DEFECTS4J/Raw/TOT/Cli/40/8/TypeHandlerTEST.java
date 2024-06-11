import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ClassTypeHandler;
import org.apache.commons.cli.FilesTypeHandler;
import org.apache.commons.cli.URLTypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TypeHandlerTest class.
 */
public class TypeHandlerTest
{
    /**
     * Test for createValue method with String type.
     */
    @Test
    public void testCreateValueWithString()
    {
        String testString = "testString";
        Class<?> clazz = String.class;
        String expected = testString;

        Object result = null;
        try
        {
            result = TypeHandler.createValue(testString, clazz);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test for createValue method with Object type.
     */
    @Test
    public void testCreateValueWithObject()
    {
        String testClassname = "java.util.Date";
        Class<?> clazz = Class.forName(testClassname);

        Object expected = new Date();
        Object result = null;
        try
        {
            result = TypeHandler.createValue(testClassname, clazz);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected.getClass(), result.getClass());
    }

    /**
     * Test for createValue method with Number type.
     */
    @Test
    public void testCreateValueWithNumber()
    {
        String testNumber = "123";
        Class<Number> clazz = Long.class;
        Long expected = Long.parseLong(testNumber);

        Number result = null;
        try
        {
            result = TypeHandler.createValue(testNumber, clazz);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test for createValue method with Date type.
     */
    @Test
    public void testCreateValueWithDate()
    {
        String testDate = "2022-01-01";
        Class<Date> clazz = Date.class;
        Date expected = new Date(0L);
        expected.setYear(2022);
        expected.setMonth(0);
        expected.setDate(1);

        Date result = null;
        try
        {
            result = TypeHandler.createValue(testDate, clazz);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test for createValue method with Class type.
     */
    @Test
    public void testCreateValueWithClass()
    {
        String testClassname = "java.util.Date";
        Class<Class<?>> clazz = Class.class;
        Class<?> expected = null;

        try
        {
            expected = Class.forName(testClassname);
        }
        catch (ClassNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        Class<?> result = null;
        try
        {
            result = (Class<?>) TypeHandler.createValue(testClassname, clazz);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test for createValue method with File type.
     */
    @Test
    public void testCreateValueWithFile()
    {
        String testFilePath = "testFile.txt";
        File expected = new File(testFilePath);

        File result = null;
        try
        {
            result = TypeHandler.createFile(testFilePath);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test for createValue method with ExistingFile type.
     */
    @Test
    public void testCreateValueWithExistingFile()
    {
        String testFilePath = "testFile.txt";
        File expected = new File(testFilePath);
        FileInputStream testFileInputStream = null;
        try
        {
            testFileInputStream = new FileInputStream(expected);
        }
        catch (FileNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        FileInputStream result = null;
        try
        {
            result = TypeHandler.openFile(testFilePath);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected.getAbsolutePath(), result.getFile().getAbsolutePath());
        try
        {
            result.close();
        }
        catch (IOException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with URL type.
     */
    @Test
    public void testCreateValueWithURL()
    {
        String testURL = "http://example.com";
        Class<URL> clazz = URL.class;
        URL expected = new URL(testURL);

        URL result = null;
        try
        {
            result = TypeHandler.createURL(testURL);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test for createValue method with File[] type.
     */
    @Test
    public void testCreateValueWithFiles()
    {
        String testFilePath1 = "testFile1.txt";
        String testFilePath2 = "testFile2.txt";
        File[] expected = new File[]{new File(testFilePath1), new File(testFilePath2)};

        File[] result = null;
        try
        {
            result = TypeHandler.createFiles(testFilePath1 + File.pathSeparator + testFilePath2);
        }
        catch (UnsupportedOperationException e)
        {
            // Expected since the method is not yet implemented
        }

        assertEquals(expected.length, result.length);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i].getName(), result[i].getName());
        }
    }

    /**
     * Test for createObject method with Class type.
     */
    @Test
    public void testCreateObjectWithClass()
    {
        String testClassname = "java.util.Date";
        Class<Date> expected = new Date();

        Date result = null;
        try
        {
            result = (Date) TypeHandler.createObject(testClassname);
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test for createNumber method with String type.
     */
    @Test
    public void testCreateNumberWithString()
    {
        String testNumber = "123";
        Number expected = 123L;

        Number result = null;
        try
        {
            result = TypeHandler.createNumber(testNumber);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected.longValue(), result.longValue());
    }

    /**
     * Test for createDate method with String type.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateWithString()
    {
        String testDate = "2022-01-01";
        Date result = null;

        try
        {
            result = TypeHandler.createDate(testDate);
        }
        catch (Exception e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        fail("Expected UnsupportedOperationException but got: " + result);
    }
}
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "testString";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringInvalidObjectType() throws ParseException
    {
        String str = "testString";
        int obj = 1;
        TypeHandler.createValue(str, obj);
        fail("Expected ParseException not thrown");
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() throws ParseException
    {
        String str = "testString";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() throws ParseException
    {
        String str = "testClass";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() throws ParseException
    {
        String str = "123";
        Number result = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123L, result.longValue());

        str = "123.45";
        result = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() throws ParseException, UnsupportedOperationException
    {
        String str = "2022-12-31";
        Date result = TypeHandler.createDate(str);
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringPatternOptionBuilderDateValueUnimplemented() throws ParseException, UnsupportedOperationException
    {
        String str = "2022-12-31";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String str = "java.lang.String";
        Class<?> resultClass = TypeHandler.createClass(str);
        assertEquals(String.class, resultClass);

        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertInstanceOf(String.class, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() throws ParseException
    {
        String str = "/test/file.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringPatternOptionBuilderFileValueNonExistent() throws ParseException, FileNotFoundException
    {
        String str = "/test/nonExistentFile.txt";
        TypeHandler.createFile(str);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() throws ParseException, MalformedURLException
    {
        String str = "http://test.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
        assertEquals("//test.com", result.getFile());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValueStringPatternOptionBuilderURLValueInvalid() throws ParseException, MalformedURLException
    {
        String str = "http:test.com";
        TypeHandler.createURL(str);
        fail("Expected MalformedURLException not thrown");
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() throws ParseException, UnsupportedOperationException
    {
        String str = "/test/file1.txt,/test/file2.txt";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringPatternOptionBuilderFilesValueUnimplemented() throws ParseException, UnsupportedOperationException
    {
        String str = "/test/file1.txt,/test/file2.txt";
        TypeHandler.createFiles(str);
    }
}
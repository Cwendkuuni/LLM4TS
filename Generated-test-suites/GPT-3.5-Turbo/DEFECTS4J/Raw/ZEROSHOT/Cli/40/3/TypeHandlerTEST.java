import org.junit.Test;
import org.junit.Assert;
import java.text.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        String str = "test";
        Class<String> clazz = PatternOptionBuilder.STRING_VALUE;
        String result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Object() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_Number() throws ParseException {
        Number result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(123, result);
    }

    @Test
    public void testCreateValue_Date() throws ParseException {
        Date result = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_Class() throws ParseException {
        Class<?> result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_File() throws ParseException {
        File result = TypeHandler.createValue("/path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        Assert.assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testCreateValue_ExistingFile() throws ParseException {
        FileInputStream result = TypeHandler.createValue("/path/to/file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_Files() throws ParseException {
        File[] result = TypeHandler.createValue("/path/to/files/*.txt", PatternOptionBuilder.FILES_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_URL() throws ParseException {
        URL result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        Assert.assertEquals(new URL("http://www.example.com"), result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateNumber_Long() throws ParseException {
        Number result = TypeHandler.createNumber("123");
        Assert.assertEquals(123, result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        Number result = TypeHandler.createNumber("3.14");
        Assert.assertEquals(3.14, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_Invalid() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        Assert.assertEquals(new URL("http://www.example.com"), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_Invalid() throws ParseException {
        TypeHandler.createURL("invalid-url");
    }

    @Test
    public void testCreateFile() throws ParseException {
        File result = TypeHandler.createFile("/path/to/file.txt");
        Assert.assertEquals(new File("/path/to/file.txt"), result);
    }

    @Test
    public void testOpenFile() throws ParseException {
        FileInputStream result = TypeHandler.openFile("/path/to/file.txt");
        Assert.assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_NotFound() throws ParseException {
        TypeHandler.openFile("invalid-file.txt");
    }

    @Test
    public void testCreateFiles() throws ParseException {
        File[] result = TypeHandler.createFiles("/path/to/files/*.txt");
        Assert.assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_NotImplemented() throws ParseException {
        TypeHandler.createFiles("/path/to/files/*.txt");
    }
}
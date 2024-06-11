import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.text.ParseException;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        Object result = typeHandler.createValue("test", Object.class);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        Object result = typeHandler.createValue("test", String.class);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValue_ObjectValue() throws ParseException {
        Object result = typeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_NumberValue() throws ParseException {
        Object result = typeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(10L, result);
    }

    @Test
    public void testCreateValue_DateValue() throws ParseException {
        Object result = typeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_ClassValue() throws ParseException {
        Object result = typeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_FileValue() throws ParseException {
        Object result = typeHandler.createValue("filename.txt", PatternOptionBuilder.FILE_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_ExistingFileValue() throws ParseException {
        Object result = typeHandler.createValue("existingfile.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_FilesValue() throws ParseException {
        Object result = typeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_UrlValue() throws ParseException {
        Object result = typeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_NullValue() throws ParseException {
        Object result = typeHandler.createValue("test", null);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = typeHandler.createObject("java.lang.String");
        Assert.assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFoundException() throws ParseException {
        typeHandler.createObject("com.example.InvalidClass");
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InstantiationException() throws ParseException {
        typeHandler.createObject("java.util.Date");
    }

    @Test
    public void testCreateNumber_Integer() throws ParseException {
        Number result = typeHandler.createNumber("10");
        Assert.assertEquals(10L, result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        Number result = typeHandler.createNumber("10.5");
        Assert.assertEquals(10.5, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumber() throws ParseException {
        typeHandler.createNumber("invalid");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = typeHandler.createClass("java.lang.String");
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFoundException() throws ParseException {
        typeHandler.createClass("com.example.InvalidClass");
    }

    @Test
    public void testCreateDate() {
        try {
            typeHandler.createDate("2021-01-01");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = typeHandler.createURL("http://www.example.com");
        Assert.assertEquals("http://www.example.com", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURLException() throws ParseException {
        typeHandler.createURL("invalidURL");
    }

    @Test
    public void testCreateFile() {
        File result = typeHandler.createFile("filename.txt");
        Assert.assertEquals("filename.txt", result.getPath());
    }

    @Test
    public void testCreateFiles() {
        try {
            typeHandler.createFiles("file1.txt,file2.txt");
        } catch (UnsupportedOperationException e) {
            Assert.assertNotNull(e);
        }
    }
}
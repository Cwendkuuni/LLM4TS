import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    @Test
    public void createValue_String_ReturnsString() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(str, result);
    }

    @Test
    public void createValue_Class_ReturnsObject() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertTrue(result instanceof String);
    }

    @Test
    public void createValue_Number_ReturnsNumber() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertTrue(result instanceof Number);
    }

    @Test(expected = ParseException.class)
    public void createValue_Number_ThrowsParseException() throws ParseException {
        String str = "not a number";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        TypeHandler.createValue(str, obj);
    }

    @Test
    public void createValue_Date_ReturnsNull() {
        String str = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertNull(result);
    }

    @Test
    public void createValue_Class_ReturnsClass() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertTrue(result instanceof Class<?>);
    }

    @Test
    public void createValue_File_ReturnsFile() {
        String str = "path/to/file";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertTrue(result instanceof File);
    }

    @Test
    public void createValue_ExistingFile_ReturnsFile() {
        String str = "path/to/existing/file";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertTrue(result instanceof File);
    }

    @Test
    public void createValue_Files_ReturnsFileArray() {
        String str = "path/to/files";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertTrue(result instanceof File[]);
    }

    @Test
    public void createValue_URL_ReturnsURL() throws ParseException {
        String str = "https://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertTrue(result instanceof URL);
    }

    @Test(expected = ParseException.class)
    public void createValue_URL_ThrowsParseException() throws ParseException {
        String str = "not a url";
        Object obj = PatternOptionBuilder.URL_VALUE;

        TypeHandler.createValue(str, obj);
    }

    @Test
    public void createObject_ReturnsObject() throws ParseException {
        String classname = "java.lang.String";

        Object result = TypeHandler.createObject(classname);

        Assert.assertTrue(result instanceof String);
    }

    @Test(expected = ParseException.class)
    public void createObject_ThrowsParseException() throws ParseException {
        String classname = "non.existent.Class";

        TypeHandler.createObject(classname);
    }

    @Test
    public void createNumber_ReturnsDouble() throws ParseException {
        String str = "10.5";

        Number result = TypeHandler.createNumber(str);

        Assert.assertTrue(result instanceof Double);
    }

    @Test
    public void createNumber_ReturnsLong() throws ParseException {
        String str = "10";

        Number result = TypeHandler.createNumber(str);

        Assert.assertTrue(result instanceof Long);
    }

    @Test(expected = ParseException.class)
    public void createNumber_ThrowsParseException() throws ParseException {
        String str = "not a number";

        TypeHandler.createNumber(str);
    }

    @Test
    public void createClass_ReturnsClass() throws ParseException {
        String classname = "java.lang.String";

        Class<?> result = TypeHandler.createClass(classname);

        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void createClass_ThrowsParseException() throws ParseException {
        String classname = "non.existent.Class";

        TypeHandler.createClass(classname);
    }

    @Test
    public void createDate_ReturnsNull() {
        String str = "2020-01-01";

        Object result = TypeHandler.createDate(str);

        Assert.assertNull(result);
    }

    @Test
    public void createURL_ReturnsURL() throws ParseException {
        String str = "https://www.example.com";

        URL result = TypeHandler.createURL(str);

        Assert.assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void createURL_ThrowsParseException() throws ParseException {
        String str = "not a url";

        TypeHandler.createURL(str);
    }

    @Test
    public void createFile_ReturnsFile() {
        String str = "path/to/file";

        File result = TypeHandler.createFile(str);

        Assert.assertEquals(str, result.getPath());
    }

    @Test
    public void createFiles_ReturnsFileArray() {
        String str = "path/to/files";

        File[] result = TypeHandler.createFiles(str);

        Assert.assertTrue(result instanceof File[]);
    }
}
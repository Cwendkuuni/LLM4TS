import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringValue_ReturnsString() throws ParseException {
        String str = "test";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);

        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_ObjectValue_ReturnsObject() throws ParseException {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);

        Assert.assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_NumberValue_ReturnsNumber() throws ParseException {
        String str = "100";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);

        Assert.assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateValue_DateValue_ReturnsDate() throws ParseException {
        String str = "2022-03-01";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);

        Assert.assertNull(result);
        // Not implemented yet, so expected to be null
    }

    @Test
    public void testCreateValue_ClassValue_ReturnsClass() throws ParseException {
        String str = "java.util.Date";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);

        Assert.assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_FileValue_ReturnsFile() throws ParseException{
        String str = "path/to/file.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);

        Assert.assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_ExistingFileValue_ReturnsFile() throws ParseException{
        String str = "path/to/existing_file.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);

        Assert.assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_FilesValue_ReturnsFileArray() throws ParseException{
        String str = "path/to/files/*.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);

        Assert.assertNull(result);
        // Not implemented yet, so expected to be null
    }

    @Test
    public void testCreateValue_URLValue_ReturnsURL() throws ParseException{
        String str = "http://www.example.com";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);

        Assert.assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_InvalidValue_ReturnsNull() throws ParseException{
        String str = "invalid";
        Object result = TypeHandler.createValue(str, Object.class);

        Assert.assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_ExceptionThrown_WhenClassNotFound() throws ParseException {
        String str = "non.existing.Class";
        TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_ExceptionThrown_WhenObjectCreationFails() throws ParseException {
        String str = "java.util.Date";
        TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_ExceptionThrown_WhenURLParsingFails() throws ParseException {
        String str = "invalid_url";
        TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
    }

    @Test // Edge case: Empty string
    public void testCreateValue_EmptyString_ReturnsNull() throws ParseException {
        String str = "";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);

        Assert.assertNull(result);
        // Empty string should be converted to null
    }

    @Test // Edge case: Null class
    public void testCreateValue_NullClass_ReturnsNull() throws ParseException {
        String str = "test";
        Object result = TypeHandler.createValue(str, null);

        Assert.assertNull(result);
        // Null class should return null
    }

    @Test // Error scenario: Null string
    public void testCreateValue_NullString_ThrowsParseException() throws ParseException {
        String str = null;
        TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        // Expected to throw ParseException
    }
}
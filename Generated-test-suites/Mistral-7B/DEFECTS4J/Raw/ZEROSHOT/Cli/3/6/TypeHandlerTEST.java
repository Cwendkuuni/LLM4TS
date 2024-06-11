import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "hello";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringWrongObjectType() {
        String str = "hello";
        Object obj = Integer.class;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_STRING_VALUE() {
        String str = "hello";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_OBJECT_VALUE() {
        String str = "org.apache.commons.cli.TypeHandler";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_NUMBER_VALUE() {
        String str = "42";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Number result = (Number) TypeHandler.createValue(str, obj);
        Assert.assertEquals(42, result.intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateValue_StringInvalidNumber() {
        String str = "not a number";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_DATE_VALUE() {
        String str = "2022-12-31";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Date result = (Date) TypeHandler.createValue(str, obj);
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringInvalidDateFormat() {
        String str = "not a valid date";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_CLASS_VALUE() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertInstanceOf(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateValue_StringInvalidClass() {
        String str = "org.nonExistingClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_FILE_VALUE() {
        String str = "path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        File result = (File) TypeHandler.createValue(str, obj);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_StringNonExistentFile() {
        String str = "path/to/nonExistentFile.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_EXISTING_FILE_VALUE() {
        String str = "path/to/existingFile.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        File result = (File) TypeHandler.createValue(str, obj);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_StringNonExistentExistingFile() {
        String str = "path/to/nonExistentFile.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_URL_VALUE() {
        String str = "http://example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        URL result = (URL) TypeHandler.createValue(str, obj);
        Assert.assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValue_StringInvalidURL() {
        String str = "not a valid url";
        Object obj = PatternOptionBuilder.URL_VALUE;
        TypeHandler.createValue(str, obj);
    }
}
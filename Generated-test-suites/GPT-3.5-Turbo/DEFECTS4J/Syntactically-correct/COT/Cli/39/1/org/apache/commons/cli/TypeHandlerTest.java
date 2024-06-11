import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        String str = "test";
        Object obj = "testObj";
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_STRING_VALUE() throws ParseException {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_OBJECT_VALUE() throws ParseException {
        String str = "TestClass";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("TestClass", result.getClass().getSimpleName());
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_NUMBER_VALUE() throws ParseException {
        String str = "123";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123L, result);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_DATE_VALUE() throws ParseException {
        String str = "2022-01-01";
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_CLASS_VALUE() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_FILE_VALUE() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_EXISTING_FILE_VALUE() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_FILES_VALUE() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_String_Class_PatternOptionBuilder_URL_VALUE() throws ParseException {
        String str = "https://www.example.com";
        Class<?> clazz = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(new URL(str), result);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        Date result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals(new URL(str), result);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertTrue(result instanceof File);
    }

    @Test(expected = ParseException.class)
    public void testCreateFiles() {
        String str = "test.txt";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }

}
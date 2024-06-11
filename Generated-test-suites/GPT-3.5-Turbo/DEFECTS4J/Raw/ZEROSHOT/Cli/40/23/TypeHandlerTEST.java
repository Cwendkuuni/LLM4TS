import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_string() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_object() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Object.class, result.getClass());
        Assert.assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_number_long() throws ParseException {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Long.class, result.getClass());
        Assert.assertEquals(Long.parseLong(str), result);
    }

    @Test
    public void testCreateValue_number_double() throws ParseException {
        String str = "123.45";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Double.class, result.getClass());
        Assert.assertEquals(Double.parseDouble(str), result);
    }

    @Test
    public void testCreateValue_date() throws ParseException {
        String str = "2021-10-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_class() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Class.class, result.getClass());
        Assert.assertEquals(str, ((Class<?>)result).getName());
    }

    @Test
    public void testCreateValue_file() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals(str, ((File)result).getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_existing_file_not_found() throws ParseException {
        String str = "missing.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_existing_file_found() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(FileInputStream.class, result.getClass());
    }

    @Test
    public void testCreateValue_files() throws ParseException {
        String str = "test1.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_url() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(URL.class, result.getClass());
        Assert.assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_unknown() throws ParseException {
        String str = "test";
        Object obj = "Unknown";
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.Object";
        Object result = TypeHandler.createObject(classname);
        Assert.assertEquals(Object.class, result.getClass());
        Assert.assertEquals(classname, result.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_class_not_found() throws ParseException {
        String classname = "unknown.Class";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_class_instantiation_failed() throws ParseException {
        String classname = "java.lang.Integer";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_long() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Long.class, result.getClass());
        Assert.assertEquals(Long.parseLong(str), result);
    }

    @Test
    public void testCreateNumber_double() throws ParseException {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Double.class, result.getClass());
        Assert.assertEquals(Double.parseDouble(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_invalid_number() throws ParseException {
        String str = "test";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.Object";
        Class<?> result = TypeHandler.createClass(classname);
        Assert.assertEquals(Class.class, result.getClass());
        Assert.assertEquals(classname, result.getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_class_not_found() throws ParseException {
        String classname = "unknown.Class";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2021-10-01";
        Date result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals(URL.class, result.getClass());
        Assert.assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_invalid_url() throws ParseException {
        String str = "invalid-url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals(str, result.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        FileInputStream result = TypeHandler.openFile(str);
        Assert.assertEquals(FileInputStream.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_file_not_found() throws ParseException {
        String str = "missing.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "test1.txt,test2.txt";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}
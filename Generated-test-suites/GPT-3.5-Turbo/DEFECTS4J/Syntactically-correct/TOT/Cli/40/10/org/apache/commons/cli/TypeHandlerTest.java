import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringValue() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_ObjectValue() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateValue_NumberValue() throws ParseException {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateValue_DateValue() throws ParseException {
        String str = "2022-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_ClassValue() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_FileValue() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_ExistingFileValue() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(FileInputStream.class, result.getClass());
    }

    @Test
    public void testCreateValue_FilesValue() throws ParseException {
        String str = "test1.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_URLValue() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(URL.class, result.getClass());
        Assert.assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_Null() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.NUMBER_VALUE + 1; // Invalid type
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFound() throws ParseException {
        String classname = "NonExistentClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_Exception() throws ParseException {
        String classname = "java.io.File";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_Integer() throws ParseException {
        String str = "123";
        Object result = TypeHandler.createNumber(str);
        Assert.assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        String str = "3.14";
        Object result = TypeHandler.createNumber(str);
        Assert.assertEquals(Double.valueOf(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_Exception() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createClass(classname);
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_Exception() throws ParseException {
        String classname = "NonExistentClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate_NotImplemented() {
        String str = "2022-01-01";
        Object result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL_ValidURL() throws ParseException, MalformedURLException {
        String str = "http://www.example.com";
        Object result = TypeHandler.createURL(str);
        Assert.assertEquals(URL.class, result.getClass());
        Assert.assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL() throws ParseException, MalformedURLException {
        String str = "invalidUrl";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        Object result = TypeHandler.createFile(str);
        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        Object result = TypeHandler.openFile(str);
        Assert.assertEquals(FileInputStream.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_FileNotFound() throws ParseException {
        String str = "nonExistentFile.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles_NotImplemented() {
        String str = "test1.txt,test2.txt";
        Object result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }

}
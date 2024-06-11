import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = new Object() {
        };
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(str, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullObject() {
        String str = "test";
        Object obj = null;
        TypeHandler.createValue(str, obj.getClass());
    }

    @Test
    public void testCreateValue_StringStringClass() {
        String str = "java.lang.String";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateValue_UnknownClass() {
        String str = "com.unknown.class";
        Class clazz = Class.forName(str);
        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_InvalidClass() {
        String str = "java.util.Date";
        TypeHandler.createObject(str);
    }

    @Test
    public void testCreateNumber_Long() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(Long.valueOf(123), result);
    }

    @Test
    public void testCreateNumber_Double() {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(Double.valueOf(123.45), result);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidString() {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_ValidClassName() {
        String str = "java.lang.String";
        Class result = TypeHandler.createClass(str);
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_InvalidClassName() {
        String str = "com.unknown.class";
        TypeHandler.createClass(str);
    }

    @Test
    public void testCreateDate_ValidDateString() {
        String str = "2022-12-31";
        Date result = TypeHandler.createDate(str);
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidDateString() {
        String str = "2022-12-32";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL_ValidURLString() {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURLString() {
        String str = "http://example.com/invalid";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_ValidFileString() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertNotNull(result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_InvalidFileString() {
        String str = "/path/to/invalid/file.txt";
        TypeHandler.createFile(str);
    }

    @Test
    public void testCreateFiles_ValidFileStrings() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
    }

    @Test
    public void testCreateFiles_InvalidFileStrings() {
        String str = "/path/to/file1.txt,/path/to/invalid/file.txt";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}
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
        Object obj = new String("test");
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_StringClass_StringType() {
        String str = "test";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass_NumberType() {
        String str = "123";
        Class clazz = int.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals((int) 123, result);

        str = "456.78";
        clazz = double.class;
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(456.78, result, 0.001);
    }

    @Test
    public void testCreateValue_StringClass_DateType() {
        String str = "2022-12-31";
        Class clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Date);
    }

    @Test
    public void testCreateValue_StringClass_ClassType() {
        String str = "java.lang.String";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(clazz, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringClass_InvalidType() {
        String str = "test";
        Class clazz = int.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        Assert.assertEquals(className, result.getClass().getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_UnknownClass() {
        String className = "UnknownClass";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";
        Number result = TypeHandler.createNumber(numberString);
        Assert.assertEquals(123, result.intValue());

        numberString = "456.78";
        result = TypeHandler.createNumber(numberString);
        Assert.assertEquals(456.78, result.doubleValue(), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidNumber() {
        String numberString = "abc";
        TypeHandler.createNumber(numberString);
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        Class result = TypeHandler.createClass(className);
        Assert.assertEquals(className, result.getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_UnknownClass() {
        String className = "UnknownClass";
        TypeHandler.createClass(className);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-12-31";
        Date result = TypeHandler.createDate(dateString);
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidDateFormat() {
        String dateString = "InvalidDate";
        TypeHandler.createDate(dateString);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL result = TypeHandler.createURL(urlString);
        Assert.assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURL() {
        String urlString = "http:/www.example.com";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_NonExistFile() {
        String filePath = "nonExist.txt";
        TypeHandler.createFile(filePath);
    }
}
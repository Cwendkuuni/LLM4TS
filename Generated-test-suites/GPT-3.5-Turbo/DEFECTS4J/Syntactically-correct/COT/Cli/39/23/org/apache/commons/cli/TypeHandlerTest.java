
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        Object obj = "testString";
        String str = "test";
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        Class<?> clazz = String.class;
        String str = "test";
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Class_NULL() throws ParseException {
        Class<?> clazz = null;
        String str = "test";
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_Object_NULL() throws ParseException {
        Object obj = null;
        String str = "test";
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        Assert.assertEquals(String.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFound() throws ParseException {
        String classname = "non.existent.Class";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InstantiationException() throws ParseException {
        String classname = "java.util.Date";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_Integer() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(10, result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(10.5, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_NumberFormatException() throws ParseException {
        String str = "not_a_number";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFound() throws ParseException {
        String classname = "non.existent.Class";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2019-01-01";
        Date result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURLException() throws ParseException {
        String str = "not_a_url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles() {
        String str = "test.txt, test2.txt";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}
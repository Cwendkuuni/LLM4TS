import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = new Object();
        try {
            Object result = TypeHandler.createValue(str, obj.getClass());
            Assert.assertEquals(str, result);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "test";
        Class<String> clazz = String.class;
        try {
            String result = (String) TypeHandler.createValue(str, clazz);
            Assert.assertEquals(str, result);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValue_InvalidClass() {
        String str = "test";
        Class<Integer> clazz = Integer.class;
        try {
            TypeHandler.createValue(str, clazz);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObject_ValidClassname() {
        String classname = "java.lang.String";
        Class<?> result = null;
        try {
            result = TypeHandler.createObject(classname);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.forName(classname), result);
    }

    @Test
    public void testCreateObject_InvalidClassname() {
        String classname = "java.lang.InvalidClass";
        try {
            TypeHandler.createObject(classname);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateNumber_ValidNumber() {
        String str = "123";
        Number result = null;
        try {
            result = TypeHandler.createNumber(str);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(result);
        if (result instanceof Long) {
            Long longResult = (Long) result;
            assertEquals(123L, longResult.longValue());
        } else {
            Double doubleResult = (Double) result;
            assertEquals(123.0, doubleResult.doubleValue(), 0.0);
        }
    }

    @Test
    public void testCreateNumber_InvalidString() {
        String str = "abc";
        try {
            TypeHandler.createNumber(str);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass_ValidClassname() {
        String classname = "java.lang.String";
        Class<?> result = null;
        try {
            result = TypeHandler.createClass(classname);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.forName(classname), result);
    }

    @Test
    public void testCreateClass_InvalidClassname() {
        String classname = "java.lang.InvalidClass";
        try {
            TypeHandler.createClass(classname);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_ValidDateString() {
        String str = "2022-03-21";
        Date result = TypeHandler.createDate(str);
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testCreateURL_ValidURLString() {
        String str = "http://www.example.com";
        URL result = null;
        try {
            result = TypeHandler.createURL(str);
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURLString() {
        String str = "http://www.example.com/invalid";
        try {
            TypeHandler.createURL(str);
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFile_ValidFileString() {
        String str = "path/to/file.txt";
        File result = new File(str);
        File inputResult = TypeHandler.createFile(str);
        Assert.assertEquals(result, inputResult);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFile_InvalidFileString() {
        String str = "path/to/invalid_file.txt";
        try {
            TypeHandler.openFile(str);
            fail("Expected FileNotFoundException");
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_ValidFileStrings() {
        String str = "path/to/file1.txt,path/to/file2.txt";
        try {
            TypeHandler.createFiles(str);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}
import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_string() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Assert.assertEquals(str, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_object() {
        String str = "java.lang.Object";
        Class<?> obj = PatternOptionBuilder.OBJECT_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Object expected = TypeHandler.createObject(str);
            Assert.assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_number() {
        String strLong = "10";
        String strDouble = "10.5";
        Class<?> obj = PatternOptionBuilder.NUMBER_VALUE;
        try {
            Object resultLong = TypeHandler.createValue(strLong, obj);
            Object resultDouble = TypeHandler.createValue(strDouble, obj);
            Long expectedLong = TypeHandler.createNumber(strLong);
            Double expectedDouble = TypeHandler.createNumber(strDouble);
            Assert.assertEquals(expectedLong, resultLong);
            Assert.assertEquals(expectedDouble, resultDouble);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_date() {
        String str = "2022-01-01";
        Class<?> obj = PatternOptionBuilder.DATE_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Object expected = TypeHandler.createDate(str);
            Assert.assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_class() {
        String str = "java.lang.String";
        Class<?> obj = PatternOptionBuilder.CLASS_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Object expected = TypeHandler.createClass(str);
            Assert.assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_file() {
        String str = "test.txt";
        Class<?> obj = PatternOptionBuilder.FILE_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Object expected = TypeHandler.createFile(str);
            Assert.assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_existingFile() {
        String str = "test.txt";
        Class<?> obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Object expected = TypeHandler.openFile(str);
            Assert.assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_files() {
        String str = "test1.txt,test2.txt";
        Class<?> obj = PatternOptionBuilder.FILES_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Object expected = TypeHandler.createFiles(str);
            Assert.assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_url() {
        String str = "http://www.example.com";
        Class<?> obj = PatternOptionBuilder.URL_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Object expected = TypeHandler.createURL(str);
            Assert.assertEquals(expected, result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateValue_null() {
        String str = "test";
        Class<?> obj = Integer.class;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Assert.assertNull(result);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
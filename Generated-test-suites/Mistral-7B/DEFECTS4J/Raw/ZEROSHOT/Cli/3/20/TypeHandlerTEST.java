import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValue_StringClass_StringClass() {
        String str = "test";
        Class clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_StringClass_NullClass() {
        String str = "test";
        Class clazz = null;
        Object obj = TypeHandler.createValue(str, clazz);
        fail("Expected NullPointerException");
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object obj = TypeHandler.createObject(className);
        assertTrue(obj instanceof String);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_NonExistentClass() {
        String className = "org.apache.commons.cli.NonExistentClass";
        Object obj = TypeHandler.createObject(className);
        fail("Expected ClassNotFoundException");
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";
        Number number = TypeHandler.createNumber(numberString);
        assertEquals(123, number.intValue());

        String numberStringWithDecimal = "123.45";
        Number numberWithDecimal = TypeHandler.createNumber(numberStringWithDecimal);
        assertEquals(123.45, numberWithDecimal.doubleValue(), 0.001);

        try {
            TypeHandler.createNumber("abc");
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-01-01";
        Date date = TypeHandler.createDate(dateString);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = sdf.parse(dateString);
        assertEquals(expectedDate, date);

        String invalidDateString = "2022-02-33";
        Date invalidDate = TypeHandler.createDate(invalidDateString);
        assertEquals(null, invalidDate);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL() {
        String urlString = "http://example.com/test";
        URL url = TypeHandler.createURL(urlString);
        assertEquals("http://example.com/test", url.toString());

        String invalidURLString = "http://example.com:test";
        TypeHandler.createURL(invalidURLString);
        fail("Expected MalformedURLException");
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File file = TypeHandler.createFile(filePath);
        assertTrue(file.exists());
        assertTrue(file.delete());

        String filePathDoesNotExist = "/nonexistent/test.txt";
        File fileDoesNotExist = TypeHandler.createFile(filePathDoesNotExist);
        assertEquals(null, fileDoesNotExist);
    }

    @Test
    public void testCreateFiles() {
        String filesPath = "test1.txt,test2.txt";
        File[] files = TypeHandler.createFiles(filesPath);
        assertEquals(2, files.length);

        for (File file : files) {
            assertTrue(file.exists());
            assertTrue(file.delete());
        }
    }
}
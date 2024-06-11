import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "Hello";
        Object obj = new String("World");
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_StringClass_StringType() {
        String str = "abcdef";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass_NumberType() {
        String str = "123";
        Class clazz = Integer.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(123, result.intValue());

        str = "123.45";
        clazz = Double.class;
        result = TypeHandler.createValue(str, clazz);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateValue_StringClass_DateType() {
        String str = "2022-12-31";
        Class clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertTrue(result instanceof Date);

        Date expected = new Date(Long.parseLong("1671232000000"));
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringClass_InvalidType() {
        String str = "Hello";
        Class clazz = Integer.class;
        Object result = TypeHandler.createValue(str, clazz);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testCreateObject_String() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertTrue(result instanceof String);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_UnknownClass() {
        String className = "java.lang.UnknownClass";
        Object result = TypeHandler.createObject(className);
        fail("Expected ClassNotFoundException but got: " + result);
    }

    @Test
    public void testCreateNumber_String() {
        String numberString = "123";
        Number result = TypeHandler.createNumber(numberString);
        assertEquals(123, result.intValue());

        numberString = "123.45";
        result = TypeHandler.createNumber(numberString);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidString() {
        String numberString = "abc";
        Number result = TypeHandler.createNumber(numberString);
        fail("Expected NumberFormatException but got: " + result);
    }

    @Test
    public void testCreateClass_String() {
        String className = "java.lang.String";
        Class result = TypeHandler.createClass(className);
        assertEquals(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_UnknownClass() {
        String className = "java.lang.UnknownClass";
        Class result = TypeHandler.createClass(className);
        fail("Expected ClassNotFoundException but got: " + result);
    }

    @Test
    public void testCreateDate_String() {
        String dateString = "2022-12-31";
        Date result = TypeHandler.createDate(dateString);
        assertTrue(result != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidString() {
        String dateString = "abcd";
        Date result = TypeHandler.createDate(dateString);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    @Test
    public void testCreateURL_String() {
        String urlString = "http://www.example.com";
        URL result = TypeHandler.createURL(urlString);
        assertTrue(result != null);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidString() {
        String urlString = "abcdefg";
        URL result = TypeHandler.createURL(urlString);
        fail("Expected MalformedURLException but got: " + result);
    }

    @Test
    public void testCreateFile_String() {
        String filePath = "/tmp/test.txt";
        File result = TypeHandler.createFile(filePath);
        assertTrue(result.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_NonExistingFile() {
        String filePath = "/tmp/non_existing.txt";
        File result = TypeHandler.createFile(filePath);
        fail("Expected FileNotFoundException but got: " + result);
    }

    @Test
    public void testCreateFiles_String() {
        String filePaths = "/tmp/test1.txt,/tmp/test2.txt";
        File[] result = TypeHandler.createFiles(filePaths);
        assertEquals(2, result.length);
        for (File file : result) {
            assertTrue(file.exists());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFiles_InvalidString() {
        String filePaths = "/tmp/test.txt,abcd";
        File[] result = TypeHandler.createFiles(filePaths);
        fail("Expected IllegalArgumentException but got: " + result);
    }
}
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        String str = "test";
        Object obj = "java.lang.String";
        Object result = TypeHandler.createValue(str, obj);
        
        assertEquals("test", result);   
    }

    @Test
    public void testCreateValue_String_Class() {
        String str = "test";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_Number() {
        String str = "10";
        Class clazz = Number.class;
        Object result = TypeHandler.createValue(str, clazz);

        assertEquals(10, result);
    }

    @Test
    public void testCreateValue_Date() {
        String str = "2022-01-01";
        Class clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
    }

    @Test
    public void testCreateValue_Class() {
        String str = "java.lang.String";
        Class clazz = Class.class;
        Object result = TypeHandler.createValue(str, clazz);

        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_File() {
        String str = "/path/to/file.txt";
        Class clazz = File.class;
        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
    }

    @Test
    public void testCreateValue_ExistingFile() {
        String str = "/path/to/file.txt";
        Class clazz = File.class;
        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
    }

    @Test
    public void testCreateValue_Files() {
        String str = "/path/to/file.txt,/path/to/file2.txt";
        Class clazz = File[].class;
        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
    }

    @Test
    public void testCreateValue_URL() {
        String str = "http://www.example.com";
        Class clazz = URL.class;
        Object result = TypeHandler.createValue(str, clazz);

        assertNotNull(result);
    }

    @Test
    public void testCreateValue_InvalidType() {
        String str = "test";
        Class clazz = Object.class;
        Object result = TypeHandler.createValue(str, clazz);

        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        String str = "java.lang.Object";
        Object result = TypeHandler.createObject(str);

        assertNotNull(result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_ClassNotFound() {
        String str = "non.existent.Class";
        Object result = TypeHandler.createObject(str);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_InstantiationException() {
        String str = "java.util.Date";
        Object result = TypeHandler.createObject(str);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCreateObject_IllegalAccessException() {
        String str = "java.lang.System";
        Object result = TypeHandler.createObject(str);
    }

    @Test
    public void testCreateNumber_Integer() {
        String str = "10";
        Number result = TypeHandler.createNumber(str);

        assertEquals(10, result);
    }

    @Test
    public void testCreateNumber_Double() {
        String str = "3.14";
        Number result = TypeHandler.createNumber(str);

        assertEquals(3.14, result);
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        String str = "test";
        Number result = TypeHandler.createNumber(str);

        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        String str = "java.lang.String";
        Class result = TypeHandler.createClass(str);

        assertEquals(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_ClassNotFound() {
        String str = "non.existent.Class";
        Class result = TypeHandler.createClass(str);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        Date result = TypeHandler.createDate(str);

        assertNotNull(result);
    }

    @Test
    public void testCreateDate_InvalidDate() {
        String str = "test";
        Date result = TypeHandler.createDate(str);

        assertNull(result);
    }

    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);

        assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURL() {
        String str = "invalid_url";
        URL result = TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createFile(str);

        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/file.txt,/path/to/file2.txt";
        File[] result = TypeHandler.createFiles(str);

        assertNotNull(result);
    }
}
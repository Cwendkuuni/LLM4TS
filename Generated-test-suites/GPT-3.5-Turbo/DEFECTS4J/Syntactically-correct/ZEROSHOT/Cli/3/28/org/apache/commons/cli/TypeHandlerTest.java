import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        String str = "test";
        Object obj = String.class;
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
    public void testCreateObject() {
        String str = "java.lang.String";
        Object result = TypeHandler.createObject(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateClass() {
        String str = "java.lang.String";
        Class result = TypeHandler.createClass(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateDate() {
        String str = "2020-01-01";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }

    @Test
    public void testCreateDate_InvalidFormat() {
        String str = "invalid";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }

    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateURL_InvalidFormat() {
        String str = "invalid";
        URL result = TypeHandler.createURL(str);
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        String str = "test.txt,test2.txt";
        File[] result = TypeHandler.createFiles(str);
        assertNull(result);
    }
}
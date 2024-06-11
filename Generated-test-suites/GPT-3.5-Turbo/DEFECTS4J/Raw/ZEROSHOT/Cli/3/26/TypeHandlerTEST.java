import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        Object result = TypeHandler.createValue("test", Object.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringClass() {
        Object result = TypeHandler.createValue("test", Class.class);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.Object");
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testCreateNumber() {
        Number result = TypeHandler.createNumber("123");
        assertNotNull(result);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2021-01-01");
        assertNotNull(result);
    }

    @Test
    public void testCreateDate_Invalid() {
        Date result = TypeHandler.createDate("invalid_date");
        assertNull(result);
    }

    @Test
    public void testCreateURL() throws MalformedURLException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testCreateURL_Invalid() {
        URL result = TypeHandler.createURL("invalid_url");
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("file1.txt,file2.txt,file3.txt");
        assertNull(result);
    }
}
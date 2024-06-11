import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObject() {
        Object obj = new Object();
        Object result = TypeHandler.createValue("test", obj);
        assertEquals(obj, result);
    }

    @Test
    public void testCreateValueWithString() {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithNumber() {
        Object result = TypeHandler.createValue("123", Number.class);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateValueWithDate() {
        Object result = TypeHandler.createValue("2021-01-01", Date.class);
        assertTrue(result instanceof Date);
    }
    
    @Test
    public void testCreateValueWithClass() {
        Object result = TypeHandler.createValue("java.lang.String", Class.class);
        assertTrue(result instanceof Class);
    }

    @Test
    public void testCreateValueWithFile() {
        Object result = TypeHandler.createValue("testfile.txt", File.class);
        assertTrue(result instanceof File);
    }

    @Test
    public void testCreateValueWithFiles() {
        Object result = TypeHandler.createValue("file1.txt,file2.txt", File[].class);
        assertTrue(result instanceof File[]);
    }

    @Test
    public void testCreateValueWithURL() {
        Object result = TypeHandler.createValue("http://www.example.com", URL.class);
        assertTrue(result instanceof URL);
    }

    @Test
    public void testCreateValueWithInvalidType() {
        Object result = TypeHandler.createValue("invalid", Boolean.class);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertTrue(result instanceof String);
    }

    @Test
    public void testCreateNumber() {
        Number result = TypeHandler.createNumber("123");
        assertEquals(123, result);
    }

    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2021-01-01");
        assertNotNull(result);
    }

    @Test
    public void testCreateURL() {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("testfile.txt");
        assertEquals("testfile.txt", result.getName());
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("file1.txt,file2.txt");
        assertEquals(2, result.length);
    }

}
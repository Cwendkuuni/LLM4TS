import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        String str = "test";
        Class<String> clazz = String.class;
        String result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.Object";
        Object result = TypeHandler.createObject(classname);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertTrue(result instanceof Number);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2020-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "/path/to/file.txt";
        FileInputStream result = TypeHandler.openFile(str);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNotFound() throws ParseException {
        String str = "/path/to/nonexistent.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/files/*.txt";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        Object obj = "test";
        String str = "value";
        Object result = TypeHandler.createValue(str, obj);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithStringClass() throws ParseException {
        Class<?> clazz = String.class;
        String str = "test";
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateDate() {
        String str = "2022-10-10";
        try {
            TypeHandler.createDate(str);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/files";
        try {
            TypeHandler.createFiles(str);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e.getMessage());
        }
    }
}
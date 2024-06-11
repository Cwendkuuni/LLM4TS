import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class TypeHandlerTest {

    // Test createValue(String, Object)
    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        Object obj = TypeHandler.createValue("test", String.class);
        assertEquals("test", obj);
    }

    // Test createValue(String, Class<?>)
    @Test
    public void testCreateValueWithStringClass() throws ParseException {
        Object obj = TypeHandler.createValue("10", Integer.class);
        assertEquals(10, obj);
    }

    // Test createObject(String)
    @Test
    public void testCreateObject() throws ParseException {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, obj.getClass());
    }

    // Test createNumber(String)
    @Test
    public void testCreateNumberLong() throws ParseException {
        Number num = TypeHandler.createNumber("10");
        assertEquals(10L, num);
    }

    @Test
    public void testCreateNumberDouble() throws ParseException {
        Number num = TypeHandler.createNumber("10.5");
        assertEquals(10.5, num);
    }

    // Test createClass(String)
    @Test
    public void testCreateClass() throws ParseException {
        Class<?> clazz = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, clazz);
    }

    // Test createDate(String)
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-01-01");
    }

    // Test createURL(String)
    @Test
    public void testCreateURL() throws ParseException {
        URL url = TypeHandler.createURL("http://www.example.com");
        assertNotNull(url);
    }

    // Test createFile(String)
    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }

    // Test createFiles(String)
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("/path/to/files");
    }
}
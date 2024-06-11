import static org.junit.Assert.*;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new Object();
        Object result = TypeHandler.createValue("test", obj);
        assertEquals(obj, result);
    }

    @Test
    public void testCreateValueStringClass() {
        Class clazz = String.class;
        Object result = TypeHandler.createValue("test", clazz);
        assertEquals("test", result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
        assertEquals(className, result.getClass().getName());
    }

    @Test
    public void testCreateNumber() {
        String numberString = "1.23";
        Number result = TypeHandler.createNumber(numberString);
        assertNotNull(result);
        assertEquals(Double.parseDouble(numberString), result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        Class result = TypeHandler.createClass(className);
        assertNotNull(result);
        assertEquals(className, result.getName());
    }

    @Test
    public void testCreateDate() {
        String dateString = "2021-01-01";
        Date result = TypeHandler.createDate(dateString);
        assertNull(result);
    }

    @Test
    public void testCreateURL() {
        String urlString = "https://example.com";
        URL result = TypeHandler.createURL(urlString);
        assertNotNull(result);
        assertEquals(urlString, result.toString());
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
        assertEquals(filePath, result.getPath());
    }

    @Test
    public void testCreateFiles() {
        String filesString = "file1.txt, file2.txt, file3.txt";
        File[] result = TypeHandler.createFiles(filesString);
        assertNull(result);
    }
}
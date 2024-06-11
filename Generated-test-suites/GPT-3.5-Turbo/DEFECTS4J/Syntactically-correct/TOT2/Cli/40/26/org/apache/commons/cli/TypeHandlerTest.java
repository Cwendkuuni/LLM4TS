import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObject() throws ParseException {
        Object result = TypeHandler.createValue("test", Object.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        Number result = TypeHandler.createValue("123", Number.class);
        assertEquals(123, result);
    }

    @Test
    public void testCreateValueWithDate() {
        try {
            TypeHandler.createValue("2022-10-10", Date.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertNotNull(obj);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("123.45");
        assertEquals(123.45, result);
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> clazz = TypeHandler.createClass("java.lang.String");
        assertNotNull(clazz);
    }

    @Test
    public void testCreateDate() {
        try {
            TypeHandler.createDate("2022-10-10");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL url = TypeHandler.createURL("https://www.example.com");
        assertNotNull(url);
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }

    @Test
    public void testOpenFile() throws ParseException {
        try {
            TypeHandler.openFile("non-existent.txt");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("dir");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

}
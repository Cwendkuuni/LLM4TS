import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber() {
        Number result = TypeHandler.createNumber("10");
        assertEquals(10, result);
    }

    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2021-09-15");
        assertNotNull(result);
    }

    @Test
    public void testCreateURL() {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertNotNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("file1.txt,file2.txt");
        assertNotNull(result);
    }
}
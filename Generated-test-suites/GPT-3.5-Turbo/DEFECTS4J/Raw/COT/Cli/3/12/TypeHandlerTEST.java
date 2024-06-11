
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        String str = "testString";
        Object obj = "String";
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateObject() {
        String str = "java.lang.String";
        assertNotNull(TypeHandler.createObject(str));
    }

    @Test
    public void testCreateNumber() {
        String str = "10";
        assertNotNull(TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClass() {
        String str = "java.lang.String";
        assertNotNull(TypeHandler.createClass(str));
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        assertNull(TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        assertNotNull(TypeHandler.createURL(str));
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        assertNotNull(TypeHandler.createFile(str));
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/files";
        assertNull(TypeHandler.createFiles(str));
    }

}

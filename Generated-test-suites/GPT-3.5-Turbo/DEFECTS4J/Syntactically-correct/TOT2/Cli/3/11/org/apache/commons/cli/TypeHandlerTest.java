import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_Object() {
        Object obj = TypeHandler.createValue("test", new Object());
        assertEquals("test", obj);
    }

    @Test
    public void testCreateValue_Class() {
        Object obj = TypeHandler.createValue("test", Class.class);
        assertNull(obj);
    }

    @Test
    public void testCreateObject() {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertNotNull(obj);
    }

    @Test
    public void testCreateNumber() {
        Number num = TypeHandler.createNumber("10");
        assertEquals(10, num);
    }

    @Test
    public void testCreateClass() {
        Class clazz = TypeHandler.createClass("java.lang.String");
        assertNotNull(clazz);
    }

    @Test
    public void testCreateDate() {
        Date date = TypeHandler.createDate("2022-01-01");
        assertNotNull(date);
    }

    @Test
    public void testCreateURL() {
        URL url = TypeHandler.createURL("http://www.example.com");
        assertNotNull(url);
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }

    @Test
    public void testCreateFiles() {
        File[] files = TypeHandler.createFiles("dir");
        assertNull(files);
    }
}
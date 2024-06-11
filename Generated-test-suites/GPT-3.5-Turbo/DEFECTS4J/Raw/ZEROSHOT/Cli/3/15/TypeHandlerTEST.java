import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        Object result = TypeHandler.createValue("test", Object.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class() {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Number() {
        Object result = TypeHandler.createValue("10", Number.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_Date() {
        Object result = TypeHandler.createValue("2020-01-01", Date.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_ClassName() {
        Object result = TypeHandler.createValue("java.lang.String", Class.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_File() {
        Object result = TypeHandler.createValue("test.txt", File.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_ExistingFile() {
        Object result = TypeHandler.createValue("test.txt", File.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_Files() {
        Object result = TypeHandler.createValue("test.txt,test2.txt", File[].class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_URL() {
        Object result = TypeHandler.createValue("http://www.example.com", URL.class);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_UnknownType() {
        Object result = TypeHandler.createValue("test", UnknownClass.class);
        assertNull(result);
    }

    @Test
    public void testCreateValue_NullString() {
        Object result = TypeHandler.createValue(null, String.class);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber_Integer() {
        Number result = TypeHandler.createNumber("10");
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testCreateNumber_Double() {
        Number result = TypeHandler.createNumber("10.5");
        assertNotNull(result);
        assertTrue(result instanceof Double);
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        Number result = TypeHandler.createNumber("abc");
        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateClass_InvalidClass() {
        Class result = TypeHandler.createClass("unknown.class");
        assertNull(result);
    }

    @Test
    public void testCreateDate_Dummy() {
        Date result = TypeHandler.createDate("2020-01-01");
        assertNotNull(result);
    }

    @Test
    public void testCreateDate_InvalidDate() {
        Date result = TypeHandler.createDate("invalidDate");
        assertNull(result);
    }

    @Test
    public void testCreateURL_ValidURL() {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
    }

    @Test
    public void testCreateURL_InvalidURL() {
        URL result = TypeHandler.createURL("invalidURL");
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("test.txt,test2.txt");
        assertNotNull(result);
    }
}
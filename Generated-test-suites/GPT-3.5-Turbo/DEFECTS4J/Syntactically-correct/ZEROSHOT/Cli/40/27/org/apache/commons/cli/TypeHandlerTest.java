import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        String str = "test";
        Object obj = "test";
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        String result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Class_Null() throws ParseException {
        String str = "test";
        Class<?> clazz = null;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "org.apache.commons.cli.TypeHandler";
        Object result = TypeHandler.createObject(classname);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFoundException() throws ParseException {
        String classname = "org.apache.commons.cli.InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_Long() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        String str = "1.23";
        Number result = TypeHandler.createNumber(str);
        assertEquals(1.23, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_ParseException() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_ClassNotFoundException() throws ParseException {
        String classname = "org.apache.commons.cli.InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_MalformedURLException() throws ParseException {
        String str = "invalid-url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "path/to/file.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "path/to/file.txt";
        FileInputStream result = TypeHandler.openFile(str);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_FileNotFoundException() throws ParseException {
        String str = "non-existent-file.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "path/to/files/";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_NotImplemented() {
        String str = "path/to/files/";
        TypeHandler.createFiles(str);
    }
}
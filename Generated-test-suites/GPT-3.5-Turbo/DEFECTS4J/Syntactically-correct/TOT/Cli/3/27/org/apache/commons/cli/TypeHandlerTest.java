import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object_typical() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, obj);

        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Class_typical() {
        String str = "test";
        Class clazz = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, clazz);

        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Object_null() {
        String str = "test";
        Object obj = new Object();

        Object result = TypeHandler.createValue(str, obj);

        assertNull(result);
    }

    @Test
    public void testCreateValue_String_Class_null() {
        String str = "test";
        Class clazz = Object.class;

        Object result = TypeHandler.createValue(str, clazz);

        assertNull(result);
    }

    @Test
    public void testCreateObject_typical() {
        String str = "java.lang.String";

        Object result = TypeHandler.createObject(str);

        assertNotNull(result);
        assertEquals(java.lang.String.class, result.getClass());
    }

    @Test
    public void testCreateObject_invalid() {
        String str = "invalid.class";

        Object result = TypeHandler.createObject(str);

        assertNull(result);
    }

    @Test
    public void testCreateNumber_Double_typical() {
        String str = "10.0";

        Number result = TypeHandler.createNumber(str);

        assertNotNull(result);
        assertEquals(Double.class, result.getClass());
        assertEquals(10.0, result);
    }

    @Test
    public void testCreateNumber_Long_typical() {
        String str = "10";

        Number result = TypeHandler.createNumber(str);

        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
        assertEquals(10L, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_invalid() {
        String str = "invalid";

        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_typical() {
        String str = "java.lang.String";

        Class result = TypeHandler.createClass(str);

        assertNotNull(result);
        assertEquals(java.lang.String.class, result);
    }

    @Test
    public void testCreateClass_invalid() {
        String str = "invalid.class";

        Class result = TypeHandler.createClass(str);

        assertNull(result);
    }

    @Test
    public void testCreateDate_typical() {
        String str = "2021-01-01";

        Date result = TypeHandler.createDate(str);

        assertNotNull(result);
    }

    @Test
    public void testCreateDate_invalid() {
        String str = "invalid.date";

        Date result = TypeHandler.createDate(str);

        assertNull(result);
    }

    @Test
    public void testCreateURL_typical() {
        String str = "http://www.example.com";

        URL result = TypeHandler.createURL(str);

        assertNotNull(result);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateURL_invalid() {
        String str = "invalid.url";

        URL result = TypeHandler.createURL(str);

        assertNull(result);
    }

    @Test
    public void testCreateFile_typical() {
        String str = "/path/to/file.txt";

        File result = TypeHandler.createFile(str);

        assertNotNull(result);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles_typical() {
        String str = "/path/to/file.txt,/path/to/anotherfile.txt";

        File[] result = TypeHandler.createFiles(str);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("/path/to/file.txt", result[0].getPath());
        assertEquals("/path/to/anotherfile.txt", result[1].getPath());
    }

    @Test
    public void testCreateFiles_null() {
        String str = "invalid.path";

        File[] result = TypeHandler.createFiles(str);

        assertNull(result);
    }

}
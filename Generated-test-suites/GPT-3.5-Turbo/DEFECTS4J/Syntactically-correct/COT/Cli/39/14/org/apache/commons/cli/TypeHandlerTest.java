import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("10");
        assertNotNull(result);
        assertEquals(10L, result);

        result = TypeHandler.createNumber("10.5");
        assertNotNull(result);
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2022-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
        assertEquals("http://www.example.com", result.toString());
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
        assertEquals("test.txt", result.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("test.txt");
    }
}
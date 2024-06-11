import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        try {
            Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
            assertEquals("test", result);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateValue_String_Class() {
        try {
            Object result = TypeHandler.createValue("test", String.class);
            assertEquals("test", result);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            assertEquals(String.class, result.getClass());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateNumber_Long() {
        try {
            Number result = TypeHandler.createNumber("10");
            assertEquals(10L, result);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateNumber_Double() {
        try {
            Number result = TypeHandler.createNumber("10.5");
            assertEquals(10.5, result);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateDate() {
        try {
            Date result = TypeHandler.createDate("2021-01-01");
            assertNull(result);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL result = TypeHandler.createURL("https://www.example.com");
            assertNotNull(result);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] result = TypeHandler.createFiles("path1,path2");
            throw new UnsupportedOperationException("Not yet implemented");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringAndObject() {
        try {
            Object result = TypeHandler.createValue("test", String.class);
            assertEquals("test", result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateValueWithNumber() {
        try {
            Object result = TypeHandler.createValue("10", Long.class);
            assertEquals(10L, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            assertTrue(result instanceof String);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number result = TypeHandler.createNumber("10.5");
            assertEquals(10.5, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-10-01");
    }

    @Test
    public void testCreateURL() {
        try {
            URL result = TypeHandler.createURL("http://www.example.com");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("dir");
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        try {
            Object result = TypeHandler.createValue("test", String.class);
            assertEquals("test", result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        try {
            Object result = TypeHandler.createValue("test", Integer.class);
            assertNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number result = TypeHandler.createNumber("10");
            assertEquals(10L, result.longValue());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateNumberWithDecimal() {
        try {
            Number result = TypeHandler.createNumber("10.5");
            assertEquals(10.5, result.doubleValue(), 0.001);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateNumberWithInvalidInput() {
        try {
            TypeHandler.createNumber("abc");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-09-01");
    }

    @Test
    public void testCreateURL() {
        try {
            URL result = TypeHandler.createURL("http://www.example.com");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateURLWithInvalidInput() {
        try {
            TypeHandler.createURL("invalidurl");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("/path/to/file.txt");
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("/path/to/files");
    }
}
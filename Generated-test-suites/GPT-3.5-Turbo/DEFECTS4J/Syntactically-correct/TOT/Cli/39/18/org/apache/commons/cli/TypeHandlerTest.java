import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    //test for createValue() method with a valid string value
    @Test
    public void testCreateValueWithString() {
        try {
            String result = (String) TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
            assertEquals("test", result);
        } catch (Exception e) {
            fail("Fails to create value with valid string");
        }
    }

    //test for createValue() method with an invalid string value
    @Test
    public void testCreateValueWithInvalidString() {
        try {
            Object result = TypeHandler.createValue("test", PatternOptionBuilder.INT_VALUE);
            assertNull(result);
        } catch (Exception e) {
            fail("Fails to handle invalid string value");
        }
    }

    //test for createValue() method with an existing class
    @Test
    public void testCreateValueWithClass() {
        try {
            Class<?> result = (Class<?>) TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
            assertEquals(String.class, result);
        } catch (Exception e) {
            fail("Fails to create value with valid class");
        }
    }

    //test for createValue() method with an invalid class
    @Test
    public void testCreateValueWithInvalidClass() {
        try {
            Object result = TypeHandler.createValue("InvalidClass", PatternOptionBuilder.CLASS_VALUE);
            assertNull(result);
        } catch (Exception e) {
            fail("Fails to handle invalid class value");
        }
    }

    //test for createValue() method with a valid file path
    @Test
    public void testCreateValueWithFile() {
        try {
            File result = (File) TypeHandler.createValue("/path/to/file", PatternOptionBuilder.FILE_VALUE);
            assertNotNull(result);
            assertTrue(result.exists());
        } catch (Exception e) {
            fail("Fails to create value with valid file path");
        }
    }

    //test for createValue() method with an invalid file path
    @Test
    public void testCreateValueWithInvalidFile() {
        try {
            Object result = TypeHandler.createValue("invalidFilePath", PatternOptionBuilder.FILE_VALUE);
            assertNull(result);
        } catch (Exception e) {
            fail("Fails to handle invalid file value");
        }
    }

    //test for createValue() method with a valid url
    @Test
    public void testCreateValueWithURL() {
        try {
            URL result = (URL) TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Fails to create value with valid URL");
        }
    }

    //test for createValue() method with an invalid url
    @Test
    public void testCreateValueWithInvalidURL() {
        try {
            Object result = TypeHandler.createValue("invalidURL", PatternOptionBuilder.URL_VALUE);
            assertNull(result);
        } catch (Exception e) {
            fail("Fails to handle invalid URL value");
        }
    }

    //test for createNumber() method with a valid number
    @Test
    public void testCreateNumberWithValidNumber() {
        try {
            Number result = TypeHandler.createNumber("10");
            assertNotNull(result);
            assertEquals(10L, result);
        } catch (Exception e) {
            fail("Fails to create number with valid number string");
        }
    }

    //test for createNumber() method with an invalid number
    @Test
    public void testCreateNumberWithInvalidNumber() {
        try {
            TypeHandler.createNumber("invalidNumber");
            fail("Fails to throw exception for invalid number string");
        } catch (Exception e) {
            assertTrue(e instanceof ParseException);
        }
    }

    //test for createObject() method with a valid class name
    @Test
    public void testCreateObjectWithValidClass() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            assertNotNull(result);
            assertTrue(result instanceof String);
        } catch (Exception e) {
            fail("Fails to create object with valid class name");
        }
    }

    //test for createObject() method with an invalid class name
    @Test
    public void testCreateObjectWithInvalidClass() {
        try {
            TypeHandler.createObject("InvalidClass");
            fail("Fails to throw exception for invalid class name");
        } catch (Exception e) {
            assertTrue(e instanceof ParseException);
        }
    }

    //test for createDate() method with a valid date string
    @Test
    public void testCreateDateWithValidDate() {
        try {
            Date result = TypeHandler.createDate("2021-01-01");
            assertNotNull(result);
        } catch (Exception e) {
            fail("Fails to create date with valid date string");
        }
    }

    //test for createDate() method with an invalid date string
    @Test
    public void testCreateDateWithInvalidDate() {
        try {
            TypeHandler.createDate("invalidDate");
            fail("Fails to throw exception for invalid date string");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    //test for createFiles() method with a valid file path
    @Test
    public void testCreateFilesWithValidPath() {
        try {
            File[] result = TypeHandler.createFiles("/path/to/files");
            assertNotNull(result);
            assertTrue(result.length > 0);
        } catch (Exception e) {
            fail("Fails to create files with valid file path");
        }
    }

    //test for createFiles() method with an invalid file path
    @Test
    public void testCreateFilesWithInvalidPath() {
        try {
            TypeHandler.createFiles("invalidPath");
            fail("Fails to throw exception for invalid file path");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }
}
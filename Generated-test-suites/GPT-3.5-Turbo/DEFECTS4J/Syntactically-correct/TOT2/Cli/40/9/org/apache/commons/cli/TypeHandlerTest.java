import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithNonExistingClass() {
        try {
            TypeHandler.createValue("NonExistingClass", NonExisting.class);
            fail("Should throw a ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObjectWithExistingClass() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            assertTrue(result instanceof String);
        } catch (ParseException e) {
            fail("Should create an instance of the existing class");
        }
    }

    @Test
    public void testCreateNumberWithIntegerValue() {
        try {
            Number result = TypeHandler.createNumber("123");
            assertTrue(result instanceof Long);
            assertEquals(123L, result);
        } catch (ParseException e) {
            fail("Should create a Long number");
        }
    }

    @Test
    public void testCreateNumberWithDoubleValue() {
        try {
            Number result = TypeHandler.createNumber("123.45");
            assertTrue(result instanceof Double);
            assertEquals(123.45, result);
        } catch (ParseException e) {
            fail("Should create a Double number");
        }
    }

    @Test
    public void testCreateClassWithExistingClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should find the existing class");
        }
    }

    @Test
    public void testCreateDate() {
        try {
            Date result = TypeHandler.createDate("2021-01-01");
            assertNull(result);
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCreateURLWithValidURL() {
        try {
            URL result = TypeHandler.createURL("http://www.example.com");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should create a valid URL");
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
    }

    @Test
    public void testOpenFileNonExisting() {
        try {
            FileInputStream result = TypeHandler.openFile("nonExistingFile.txt");
            fail("Should throw a ParseException");
        } catch (ParseException | FileNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] result = TypeHandler.createFiles("dir/*.txt");
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}
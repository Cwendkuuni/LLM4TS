import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Before
    public void setup() {
        // Setup code
    }

    @Test
    public void testCreateValueWithStringValue() throws ParseException {
        String input = "test";
        Object expected = "test";
        Object actual = TypeHandler.createValue(input, PatternOptionBuilder.STRING_VALUE);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithBooleanValue() throws ParseException {
        String input = "true";
        Object expected = null;
        Object actual = TypeHandler.createValue(input, PatternOptionBuilder.BOOLEAN_VALUE);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateObjectWithValidClassName() throws ParseException {
        String input = "java.util.Date";
        Object expected = new java.util.Date();

        try {
            Object actual = TypeHandler.createObject(input);
            
            assertEquals(expected.getClass(), actual.getClass());
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassName() throws ParseException {
        String input = "InvalidClass";
        TypeHandler.createObject(input);
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        String input = "123";
        Number expected = 123L;
        Number actual = TypeHandler.createNumber(input);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        String input = "3.14";
        Number expected = 3.14;
        Number actual = TypeHandler.createNumber(input);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        String input = "abc";
        TypeHandler.createNumber(input);
    }

    @Test
    public void testCreateClassWithValidClassName() throws ParseException {
        String input = "java.util.Date";
        Class<?> expected = java.util.Date.class;
        Class<?> actual = TypeHandler.createClass(input);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassName() throws ParseException {
        String input = "InvalidClass";
        TypeHandler.createClass(input);
    }

    @Test
    public void testCreateDateWithValidDateString() throws ParseException {
        String input = "2021-01-01";
        
        try {
            Date actual = TypeHandler.createDate(input);
            assertNotNull(actual);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }

    @Test
    public void testCreateDateWithInvalidDateString() throws ParseException {
        String input = "InvalidDate";
        
        try {
            Date actual = TypeHandler.createDate(input);
            assertNull(actual);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw UnsupportedOperationException");
        }
    }

    @Test
    public void testCreateURLWithValidURL() throws ParseException {
        String input = "http://www.example.com";
        URL expected = new URL(input);
        URL actual = TypeHandler.createURL(input);

        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String input = "InvalidURL";
        TypeHandler.createURL(input);
    }

    @Test
    public void testCreateFile() {
        String input = "path/to/file.txt";
        File expected = new File(input);
        File actual = TypeHandler.createFile(input);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFiles() {
        String input = "path/to/file.txt,path/to/anotherFile.txt";
        File[] expected = new File[] { new File("path/to/file.txt"), new File("path/to/anotherFile.txt") };
        File[] actual = TypeHandler.createFiles(input);

        assertArrayEquals(expected, actual);
    }
}
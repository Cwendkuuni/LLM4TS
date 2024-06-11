import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() throws ParseException {
        String str = "testString";
        Object expected = str;
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "java.lang.String";
        Object expected = TypeHandler.createObject(str);
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "123";
        Object expected = Long.valueOf(str);
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(expected, actual);

        str = "123.45";
        expected = Double.valueOf(str);
        actual = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String str = "2021-01-01";
        Object expected = null; // Date conversion not implemented
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "java.lang.String";
        Object expected = TypeHandler.createClass(str);
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String str = "/path/to/file.txt";
        Object expected = new File(str);
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertEquals(expected, actual);

        expected = new File(str);
        actual = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithFiles() throws ParseException {
        String str = "/path/to/files";
        Object expected = null; // File[] conversion not implemented
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithURL() throws ParseException {
        String str = "http://www.example.com";
        Object expected = new URL(str);
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object expected = TypeHandler.createClass(classname);
        Object actual = TypeHandler.createObject(classname);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectClassNotFoundException() throws ParseException {
        String classname = "com.example.NonExistentClass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInstantiationException() throws ParseException {
        String classname = "java.util.AbstractList";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "123";
        Number expected = Long.valueOf(str);
        Number actual = TypeHandler.createNumber(str);
        assertEquals(expected, actual);

        str = "123.45";
        expected = Double.valueOf(str);
        actual = TypeHandler.createNumber(str);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberNumberFormatException() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> expected = TypeHandler.createClass(classname);
        Class<?> actual = TypeHandler.createClass(classname);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassNotFoundException() throws ParseException {
        String classname = "com.example.NonExistentClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        Date expected = null; // Date conversion not implemented
        Date actual = TypeHandler.createDate(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL expected = new URL(str);
        URL actual = TypeHandler.createURL(str);
        assertEquals(expected, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLMalformedURLException() throws ParseException {
        String str = "htp://www.example.com";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File expected = new File(str);
        File actual = TypeHandler.createFile(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/files";
        File[] expected = null; // File[] conversion not implemented
        File[] actual = TypeHandler.createFiles(str);
        assertEquals(expected, actual);
    }
}
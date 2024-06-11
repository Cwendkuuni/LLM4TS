import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        assertEquals(Class.forName(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str1 = "123";
        String str2 = "123.45";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        assertEquals(Long.valueOf(str1), TypeHandler.createValue(str1, obj));
        assertEquals(Double.valueOf(str2), TypeHandler.createValue(str2, obj));
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String str = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        assertEquals(Class.forName(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String str = "path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        assertEquals(new File(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithExistingFile() throws ParseException {
        String str = "path/to/existing_file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        FileInputStream fis = TypeHandler.openFile(str);
        assertEquals(new FileInputStream(str), fis);
    }

    @Test
    public void testCreateValueWithFiles() throws ParseException {
        String str = "path/to/files/*.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithURL() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        assertEquals(new URL(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        assertEquals(Class.forName(classname), TypeHandler.createObject(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectClassNotFound() throws ParseException {
        String classname = "com.example.NonExistentClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        String str = "123";
        assertEquals(Long.valueOf(str), TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        String str = "123.45";
        assertEquals(Double.valueOf(str), TypeHandler.createNumber(str));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumber() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        assertEquals(Class.forName(classname), TypeHandler.createClass(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateClassNotFound() throws ParseException {
        String classname = "com.example.NonExistentClass";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2020-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        assertEquals(new URL(str), TypeHandler.createURL(str));
    }

    @Test(expected = ParseException.class)
    public void testCreateURLMalformedURL() throws ParseException {
        String str = "invalid url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "path/to/file.txt";
        assertEquals(new File(str), TypeHandler.createFile(str));
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "path/to/existing_file.txt";
        FileInputStream fis = TypeHandler.openFile(str);
        assertEquals(new FileInputStream(str), fis);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNotFound() throws ParseException {
        String str = "path/to/non_existent_file.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        // Not yet implemented
        String str = "path/to/files/*.txt";
        // assertEquals(expected, TypeHandler.createFiles(str));
    }
}
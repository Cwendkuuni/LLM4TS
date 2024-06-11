import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        assertEquals(str, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        assertEquals(java.lang.Object.class, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        assertEquals(123L, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String str = "12/31/2021";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        assertEquals(java.lang.String.class, TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        assertEquals(new File(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithExistingFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        assertEquals(new File(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithFiles() throws ParseException {
        String str = "file1.txt,file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateValueWithURL() throws ParseException {
        String str = "https://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        assertEquals(new URL(str), TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        assertEquals("", TypeHandler.createObject(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectClassNotFound() throws ParseException {
        String classname = "nonexistentclass";
        TypeHandler.createObject(classname);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectException() throws ParseException {
        String classname = "java.io.File";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberWithLong() throws ParseException {
        String str = "123";
        assertEquals(123L, TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        String str = "123.456";
        assertEquals(123.456, TypeHandler.createNumber(str));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberException() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        assertEquals(java.lang.String.class, TypeHandler.createClass(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateClassException() throws ParseException {
        String classname = "nonexistentclass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "12/31/2021";
        assertNull(TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "https://www.example.com";
        assertEquals(new URL(str), TypeHandler.createURL(str));
    }

    @Test(expected = ParseException.class)
    public void testCreateURLException() throws ParseException {
        String str = "example.com";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        assertEquals(new File(str), TypeHandler.createFile(str));
    }

    @Test
    public void testCreateFiles() {
        String str = "file1.txt,file2.txt";
        assertNull(TypeHandler.createFiles(str));
    }
}
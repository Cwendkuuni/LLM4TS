import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringType() throws ParseException {
        Object value = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", value);
    }

    @Test
    public void testCreateValueWithObjectType() throws ParseException {
        Object value = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(Object.class, value.getClass());
    }

    @Test
    public void testCreateValueWithNumberType() throws ParseException {
        Object value = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(value instanceof Number);
        assertEquals(10, ((Number) value).intValue());
    }

    @Test
    public void testCreateValueWithDateType() throws ParseException {
        Object value = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(value); // Date parsing is not implemented yet, so it should return null
    }

    @Test
    public void testCreateValueWithClassType() throws ParseException {
        Object value = TypeHandler.createValue("java.util.Date", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(Date.class, value);
    }

    @Test
    public void testCreateValueWithFileType() throws ParseException {
        Object value = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertTrue(value instanceof File);
        assertEquals("test.txt", ((File) value).getName());
    }

    @Test
    public void testCreateValueWithExistingFileType() throws ParseException {
        Object value = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertTrue(value instanceof FileInputStream);
        assertEquals("test.txt", ((FileInputStream) value).getFD().toString());
    }

    @Test
    public void testCreateValueWithFilesType() throws ParseException {
        Object value = TypeHandler.createValue("test1.txt,test2.txt", PatternOptionBuilder.FILES_VALUE);
        assertTrue(value instanceof File[]);
        File[] files = (File[]) value;
        assertEquals(2, files.length);
        assertEquals("test1.txt", files[0].getName());
        assertEquals("test2.txt", files[1].getName());
    }

    @Test
    public void testCreateValueWithURLType() throws ParseException, MalformedURLException {
        Object value = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertTrue(value instanceof URL);
        assertEquals("http://www.example.com", ((URL) value).toString());
    }

    @Test
    public void testCreateValueWithInvalidType() throws ParseException {
        Object value = TypeHandler.createValue("test", Void.class);
        assertNull(value);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object value = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClass() throws ParseException {
        TypeHandler.createObject("com.example.InvalidClass");
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        Number value = TypeHandler.createNumber("10");
        assertEquals(Integer.class, value.getClass());
        assertEquals(10, value.intValue());
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        Number value = TypeHandler.createNumber("10.5");
        assertEquals(Double.class, value.getClass());
        assertEquals(10.5, value.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        TypeHandler.createNumber("ten");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> value = TypeHandler.createClass("java.util.Date");
        assertEquals(Date.class, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClass() throws ParseException {
        TypeHandler.createClass("com.example.InvalidClass");
    }

    @Test
    public void testCreateDate() {
        Date value = TypeHandler.createDate("2020-01-01");
        assertNull(value); // Not implemented yet, should return null
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        URL value = TypeHandler.createURL("http://www.example.com");
        assertEquals("http://www.example.com", value.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        TypeHandler.createURL("not_a_url");
    }

    @Test
    public void testCreateFile() {
        File value = TypeHandler.createFile("test.txt");
        assertEquals("test.txt", value.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        FileInputStream value = TypeHandler.openFile("test.txt");
        assertEquals("test.txt", value.getFD().toString());
    }

    @Test(expected = ParseException.class)
    public void testOpenFileWithInvalidFile() throws ParseException {
        TypeHandler.openFile("non_existent.txt");
    }

    @Test
    public void testCreateFiles() {
        File[] value = TypeHandler.createFiles("test1.txt,test2.txt");
        assertEquals(2, value.length);
        assertEquals("test1.txt", value[0].getName());
        assertEquals("test2.txt", value[1].getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFilesNotImplemented() {
        TypeHandler.createFiles("test.txt");
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValue() throws ParseException {
        Object result = TypeHandler.createValue("test", "java.lang.String");
        assertEquals("test", result);
    }
    
    @Test
    public void testCreateValueWithClass() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderObjectValue() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderNumberValue() throws ParseException {
        Object result = TypeHandler.createValue("1", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Number);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderDateValue() throws ParseException {
        Object result = TypeHandler.createValue("12/31/2022", PatternOptionBuilder.DATE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Date);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderClassValue() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Class<?>);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderFileValue() throws ParseException {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderExistingFileValue() throws ParseException {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof FileInputStream);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderFilesValue() throws ParseException {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
    }
    
    @Test
    public void testCreateValueWithPatternOptionBuilderURLValue() throws ParseException {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof URL);
    }
    
    @Test
    public void testCreateValueWithUnknownValue() throws ParseException {
        Object result = TypeHandler.createValue("test", Integer.class);
        assertNull(result);
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObjectWithUnknownClass() throws ParseException {
        TypeHandler.createObject("com.example.Test");
    }
    
    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        Number result = TypeHandler.createNumber("123");
        assertNotNull(result);
        assertTrue(result instanceof Long);
        assertEquals(123L, result);
    }
    
    @Test
    public void testCreateNumberWithDecimal() throws ParseException {
        Number result = TypeHandler.createNumber("123.45");
        assertNotNull(result);
        assertTrue(result instanceof Double);
        assertEquals(123.45, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        TypeHandler.createNumber("abc");
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClassWithUnknownClass() throws ParseException {
        TypeHandler.createClass("com.example.Test");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("12/31/2022");
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        TypeHandler.createURL("abc");
    }
    
    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
    }
    
    @Test
    public void testOpenFile() throws ParseException {
        FileInputStream result = TypeHandler.openFile("test.txt");
        assertNotNull(result);
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFileWithNonExistingFile() throws ParseException {
        TypeHandler.openFile("nonexisting.txt");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("test.txt");
    }
}
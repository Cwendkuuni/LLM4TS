import org.junit.*;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringValue() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }
    
    @Test
    public void testCreateValueWithObjectValue() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testCreateValueWithNumberValue() {
        Object result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Long.class, result.getClass());
    }

    @Test
    public void testCreateValueWithDateValue() {
        Object result = TypeHandler.createValue("2022-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithClassValue() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateValueWithFileValue() {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals(File.class, result.getClass());
        assertEquals("test.txt", ((File) result).getName());
    }
    
    @Test
    public void testCreateValueWithExistingFileValue() {
        Object result = TypeHandler.createValue("existing.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(File.class, result.getClass());
        assertEquals("existing.txt", ((File) result).getName());
    }
    
    @Test
    public void testCreateValueWithFilesValue() {
        Object result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithURLValue() {
        Object result = TypeHandler.createValue("https://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof URL);
    }
    
    @Test
    public void testCreateValueWithInvalidValue() {
        Object result = TypeHandler.createValue("invalid", Object.class);
        assertNull(result);
    }
    
    @Test
    public void testCreateObjectWithValidClassName() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testCreateObjectWithInvalidClassName() {
        Object result = TypeHandler.createObject("invalid.Class");
        assertNull(result);
    }
    
    @Test
    public void testCreateNumberWithValidNumber() {
        Number result = TypeHandler.createNumber("123");
        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
    }
    
    @Test
    public void testCreateNumberWithInvalidNumber() {
        Number result = TypeHandler.createNumber("abc");
        assertNull(result);
    }
    
    @Test
    public void testCreateClassWithValidClassName() {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateClassWithInvalidClassName() {
        Class<?> result = TypeHandler.createClass("invalid.Class");
        assertNull(result);
    }
    
    @Test
    public void testCreateDateWithValidDate() {
        Date result = TypeHandler.createDate("2022-01-01");
        assertNull(result);
    }
    
    @Test
    public void testCreateURLWithValidURL() {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertNotNull(result);
    }
    
    @Test
    public void testCreateURLWithInvalidURL() {
        URL result = TypeHandler.createURL("invalidURL");
        assertNull(result);
    }
    
    @Test
    public void testCreateFileWithValidFilePath() {
        File result = TypeHandler.createFile("test.txt");
        assertNotNull(result);
        assertEquals("test.txt", result.getName());
    }
    
    @Test
    public void testCreateFilesWithValidFilePaths() {
        File[] result = TypeHandler.createFiles("file1.txt,file2.txt");
        assertNull(result);
    }
}
import static org.junit.Assert.*;

import org.junit.Test;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueWithStringValue() {
        Object obj = TypeHandler.createValue("testString", PatternOptionBuilder.STRING_VALUE);
        assertEquals("testString", obj);
    }
    
    @Test
    public void testCreateValueWithObjectValue() {
        Object obj = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof String);
    }
    
    @Test
    public void testCreateValueWithNumberValue() {
        Object obj = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof Number);
    }
    
    @Test
    public void testCreateValueWithDateValue() {
        Object obj = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof Date);
    }
    
    @Test
    public void testCreateValueWithClassValue() {
        Object obj = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof Class);
    }
    
    @Test
    public void testCreateValueWithFileValue() {
        Object obj = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof File);
    }
    
    @Test
    public void testCreateValueWithExistingFileValue() {
        Object obj = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof File);
    }
    
    @Test
    public void testCreateValueWithFilesValue() {
        Object obj = TypeHandler.createValue("path/to/file1.txt,path/to/file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof File[]);
    }
    
    @Test
    public void testCreateValueWithURLValue() {
        Object obj = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(obj);
        assertTrue(obj instanceof URL);
    }
    
    @Test
    public void testCreateValueWithInvalidValue() {
        Object obj = TypeHandler.createValue("invalid", String.class);
        assertNull(obj);
    }
    
    @Test
    public void testCreateObject() {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertNotNull(obj);
        assertTrue(obj instanceof String);
    }
    
    @Test
    public void testCreateNumber() {
        Number number = TypeHandler.createNumber("123.45");
        assertNotNull(number);
        assertTrue(number instanceof Double);
    }
    
    @Test
    public void testCreateClass() {
        Class<?> clazz = TypeHandler.createClass("java.lang.String");
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }
    
    @Test
    public void testCreateDate() {
        Date date = TypeHandler.createDate("2020-01-01");
        assertNotNull(date);
    }
    
    @Test
    public void testCreateURL() {
        URL url = TypeHandler.createURL("http://www.example.com");
        assertNotNull(url);
        assertEquals("http://www.example.com", url.toString());
    }
    
    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("path/to/file.txt");
        assertNotNull(file);
        assertTrue(file.isFile());
    }
    
    @Test
    public void testCreateFiles() {
        File[] files = TypeHandler.createFiles("path/to/file1.txt,path/to/file2.txt");
        assertNotNull(files);
        assertEquals(2, files.length);
        assertTrue(files[0].isFile());
        assertTrue(files[1].isFile());
    }
}
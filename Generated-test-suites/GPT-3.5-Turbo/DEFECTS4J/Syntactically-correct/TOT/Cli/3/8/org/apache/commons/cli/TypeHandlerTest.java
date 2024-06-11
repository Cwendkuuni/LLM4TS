import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueWithStringType() {
        String result = (String)TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }
    
    @Test
    public void testCreateValueWithObjectType() {
        Object result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }
    
    @Test
    public void testCreateValueWithNumberType() {
        Number result = (Number)TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertEquals(10, result.intValue());
    }
    
    @Test
    public void testCreateValueWithDateType() {
        Date result = (Date)TypeHandler.createValue("2021-03-18", PatternOptionBuilder.DATE_VALUE);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateValueWithClassType() {
        Class result = (Class)TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateValueWithFileType() {
        File result = (File)TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertEquals("path/to/file.txt", result.getPath());
    }
    
    @Test
    public void testCreateValueWithExistingFileType() {
        File result = (File)TypeHandler.createValue("existing/file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
        assertEquals("existing/file.txt", result.getPath());
    }
    
    @Test
    public void testCreateValueWithFilesType() {
        File[] result = (File[])TypeHandler.createValue("dir/file1.txt,dir/file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("dir/file1.txt", result[0].getPath());
        assertEquals("dir/file2.txt", result[1].getPath());
    }
    
    @Test
    public void testCreateValueWithURLType() {
        URL result = (URL)TypeHandler.createValue("http://example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertEquals("http://example.com", result.toString());
    }
    
    @Test
    public void testCreateValueWithUnknownType() {
        Object result = TypeHandler.createValue("unknown", Object.class);
        assertNull(result);
    }
    
    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testCreateNumberWithInt() {
        Number result = TypeHandler.createNumber("10");
        assertNotNull(result);
        assertEquals(10, result.intValue());
    }
    
    @Test
    public void testCreateNumberWithDouble() {
        Number result = TypeHandler.createNumber("10.5");
        assertNotNull(result);
        assertEquals(10.5, result.doubleValue(), 0.01);
    }
    
    @Test
    public void testCreateNumberWithInvalidString() {
        Number result = TypeHandler.createNumber("invalid");
        assertNull(result);
    }
    
    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateClassWithInvalidName() {
        Class result = TypeHandler.createClass("invalid");
        assertNull(result);
    }
    
    @Test
    public void testCreateDateWithValidString() {
        Date result = TypeHandler.createDate("2021-03-18");
        assertNotNull(result);
    }
    
    @Test
    public void testCreateDateWithInvalidString() {
        Date result = TypeHandler.createDate("invalid");
        assertNull(result);
    }
    
    @Test
    public void testCreateURL() {
        URL result = TypeHandler.createURL("http://example.com");
        assertNotNull(result);
        assertEquals("http://example.com", result.toString());
    }
    
    @Test
    public void testCreateURLWithInvalidString() {
        URL result = TypeHandler.createURL("invalid");
        assertNull(result);
    }
    
    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("path/to/file.txt");
        assertNotNull(result);
        assertEquals("path/to/file.txt", result.getPath());
    }
    
    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("dir/file1.txt,dir/file2.txt");
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("dir/file1.txt", result[0].getPath());
        assertEquals("dir/file2.txt", result[1].getPath());
    }
    
    @Test
    public void testCreateFilesWithInvalidString() {
        File[] result = TypeHandler.createFiles("invalid");
        assertNull(result);
    }
}
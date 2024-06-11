import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() {
        String str = "testString";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithObject() {
        String str = "java.lang.Object";
        Class clazz = PatternOptionBuilder.OBJECT_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }
    
    @Test
    public void testCreateValueWithNumber() {
        String str = "123";
        Class clazz = PatternOptionBuilder.NUMBER_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
        assertEquals(123L, result);
    }
    
    @Test
    public void testCreateValueWithDate() {
        String str = "2022-01-01";
        Class clazz = PatternOptionBuilder.DATE_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithClass() {
        String str = "java.lang.String";
        Class clazz = PatternOptionBuilder.CLASS_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNotNull(result);
        assertEquals(Class.class, result.getClass());
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateValueWithFile() {
        String str = "/path/to/file.txt";
        Class clazz = PatternOptionBuilder.FILE_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNotNull(result);
        assertEquals(File.class, result.getClass());
        assertEquals(str, ((File) result).getPath());
    }
    
    @Test
    public void testCreateValueWithExistingFile() {
        String str = "/path/to/file.txt";
        Class clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNotNull(result);
        assertEquals(File.class, result.getClass());
        assertEquals(str, ((File) result).getPath());
    }
    
    @Test
    public void testCreateValueWithFiles() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        Class clazz = PatternOptionBuilder.FILES_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithURL() {
        String str = "http://www.example.com";
        Class clazz = PatternOptionBuilder.URL_VALUE;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNotNull(result);
        assertEquals(URL.class, result.getClass());
        
        try {
            assertEquals(str, ((URL) result).toString());
        } catch (MalformedURLException e) {
            fail("Malformed URL Exception");
        }
    }
    
    @Test
    public void testCreateValueWithInvalidType() {
        String str = "invalidType";
        Class clazz = null;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateObject() {
        String str = "java.lang.String";
        
        Object result = TypeHandler.createObject(str);
        
        assertNotNull(result);
        assertEquals(Class.class, result.getClass());
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateNumber() {
        String str = "123";
        
        Number result = TypeHandler.createNumber(str);
        
        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
        assertEquals(123L, result);
    }
    
    @Test
    public void testCreateNumberWithInvalidFormat() {
        String str = "invalidNumber";
        
        Number result = TypeHandler.createNumber(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateClass() {
        String str = "java.lang.String";
        
        Class result = TypeHandler.createClass(str);
        
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateClassWithInvalidName() {
        String str = "invalidClass";
        
        Class result = TypeHandler.createClass(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        
        Date result = TypeHandler.createDate(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        
        URL result = TypeHandler.createURL(str);
        
        assertNotNull(result);
        
        try {
            assertEquals(str, result.toString());
        } catch (MalformedURLException e) {
            fail("Malformed URL Exception");
        }
    }
    
    @Test
    public void testCreateURLWithInvalidURL() {
        String str = "invalidURL";
        
        URL result = TypeHandler.createURL(str);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        
        File result = TypeHandler.createFile(str);
        
        assertNotNull(result);
        assertEquals(str, result.getPath());
    }
    
    @Test
    public void testCreateFiles() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        
        File[] result = TypeHandler.createFiles(str);
        
        assertNull(result);
    }
}
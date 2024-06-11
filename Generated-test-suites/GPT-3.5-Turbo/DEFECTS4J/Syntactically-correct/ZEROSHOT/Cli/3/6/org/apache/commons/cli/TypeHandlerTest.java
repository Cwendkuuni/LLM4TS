import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringArgument() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithObjectArgument() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Class.forName(str).getName(), result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithNumberArgument() {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Double.parseDouble(str), result);
    }
    
    @Test
    public void testCreateValueWithDateArgument() {
        String str = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithClassArgument() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Class.forName(str).getName(), result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithFileArgument() {
        String str = "/path/to/file";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, ((File) result).getPath());
    }
    
    @Test
    public void testCreateValueWithExistingFileArgument() {
        String str = "/path/to/file";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, ((File) result).getPath());
    }
    
    @Test
    public void testCreateValueWithFilesArgument() {
        String str = "/path/to/files";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithURLArgument() {
        String str = "https://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, ((URL) result).toString());
    }
    
    @Test
    public void testCreateValueWithUnknownArgument() {
        String str = "unknown";
        Object obj = "UnknownArg";
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        String str = "java.lang.String";
        Object result = TypeHandler.createObject(str);
        assertNotNull(result);
        assertEquals(Class.forName(str).getName(), result.getClass().getName());
    }
    
    @Test
    public void testCreateNumberWithValidNumber() {
        String str = "1.5";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(Double.parseDouble(str), result);
    }
    
    @Test
    public void testCreateNumberWithInvalidNumber() {
        String str = "test";
        Number result = TypeHandler.createNumber(str);
        assertNull(result);
    }
    
    @Test
    public void testCreateClassWithValidClassName() {
        String str = "java.lang.String";
        Class result = TypeHandler.createClass(str);
        assertNotNull(result);
        assertEquals(Class.forName(str).getName(), result.getName());
    }
    
    @Test
    public void testCreateClassWithInvalidClassName() {
        String str = "UnknownClass";
        Class result = TypeHandler.createClass(str);
        assertNull(result);
    }
    
    @Test
    public void testCreateDateWithValidDate() {
        String str = "2020-01-01";
        Date result = TypeHandler.createDate(str);
        assertNull(result);
    }
    
    @Test
    public void testCreateURLWithValidURL() {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals(str, result.toString());
    }
    
    @Test
    public void testCreateURLWithInvalidURL() {
        String str = "invalidURL";
        URL result = TypeHandler.createURL(str);
        assertNull(result);
    }
    
    @Test
    public void testCreateFile() {
        String str = "/path/to/file";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals(str, result.getPath());
    }
    
    @Test
    public void testCreateFiles() {
        String str = "/path/to/files";
        File[] result = TypeHandler.createFiles(str);
        assertNull(result);
    }
}
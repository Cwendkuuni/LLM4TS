import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValueWithStringValue() {
        assertEquals("abc", TypeHandler.createValue("abc", PatternOptionBuilder.STRING_VALUE));
    }
    
    @Test
    public void testCreateValueWithObjectValue() {
        String className = "java.lang.String";
        Object result = TypeHandler.createValue(className, PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals(className, result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithNumberValue() {
        assertEquals(123, TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE));
    }
    
    @Test
    public void testCreateValueWithDateValue() {
        Date date = TypeHandler.createValue("2022-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNotNull(date);
        assertEquals("2022-01-01", new SimpleDateFormat("yyyy-MM-dd").format(date));
    }
    
    @Test
    public void testCreateValueWithClassValue() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertEquals("java.lang.String", result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithFileValue() {
        File file = TypeHandler.createValue("test.file", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(file);
        assertEquals("test.file", file.getName());
    }
    
    @Test
    public void testCreateValueWithExistingFileValue() {
        File file = TypeHandler.createValue("existing.file", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(file);
        assertEquals("existing.file", file.getName());
    }
    
    @Test
    public void testCreateValueWithFilesValue() {
        File[] files = TypeHandler.createValue("file1,file2,file3", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(files);
        assertEquals(3, files.length);
        assertEquals("file1", files[0].getName());
        assertEquals("file2", files[1].getName());
        assertEquals("file3", files[2].getName());
    }
    
    @Test
    public void testCreateValueWithURLValue() {
        URL url = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(url);
        assertEquals("http://www.example.com", url.toString());
    }
    
    @Test
    public void testCreateValueWithUnknownValue() {
        assertNull(TypeHandler.createValue("unknown", Object.class));
    }
}
import org.junit.Test;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringValue() {
        String str = "Test String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithObjectValue() {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testCreateValueWithNumberValue() {
        String str = "10";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Long.class, result.getClass());
        assertEquals(10L, result);
        
        str = "10.5";
        result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Double.class, result.getClass());
        assertEquals(10.5, (Double) result, 0.01);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidNumberValue() throws ParseException {
        String str = "abc";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }
    
    @Test
    public void testCreateValueWithDateValue() {
        // TODO: Implement test
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithDateValueUnsupported() {
        String str = "2020-01-01";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }
    
    @Test
    public void testCreateValueWithClassValue() {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result.getClass());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidClassValue() throws ParseException {
        String str = "com.example.InvalidClass";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }
    
    @Test
    public void testCreateValueWithFileValue() {
        String str = "/path/to/file.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertEquals(File.class, result.getClass());
        assertEquals(str, ((File) result).getPath());
    }
    
    @Test
    public void testCreateValueWithExistingFileValue() {
        String str = "/path/to/existing-file.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(File.class, result.getClass());
        assertEquals(str, ((File) result).getPath());
    }
    
    @Test
    public void testCreateValueWithInvalidFileValue() {
        String str = "/path/to/non-existing-file.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithFilesValue() {
        String str = "/path/to/file1.txt,/path/to/file2.txt,/path/to/file3.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertEquals(File[].class, result.getClass());
        File[] files = (File[]) result;
        assertEquals(3, files.length);
        assertEquals("/path/to/file1.txt", files[0].getPath());
        assertEquals("/path/to/file2.txt", files[1].getPath());
        assertEquals("/path/to/file3.txt", files[2].getPath());
    }
    
    @Test
    public void testCreateValueWithInvalidFilesValue() {
        String str = "/path/to/non-existing-file.txt, /path/to/file2.txt,";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithURLValue() {
        String str = "http://www.example.com";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertEquals(URL.class, result.getClass());
        assertEquals(str, result.toString());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidURLValue() throws ParseException {
        String str = "invalid-url";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertEquals(String.class, result.getClass());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassname() throws ParseException {
        String classname = "com.example.InvalidClass";
        Object result = TypeHandler.createObject(classname);
    }
    
    @Test
    public void testCreateNumberFromString() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        assertEquals(Long.class, result.getClass());
        assertEquals(10L, result);
        
        str = "10.5";
        result = TypeHandler.createNumber(str);
        assertEquals(Double.class, result.getClass());
        assertEquals(10.5, (Double) result, 0.01);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumberFromInvalidString() throws ParseException {
        String str = "abc";
        Number result = TypeHandler.createNumber(str);
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassname() throws ParseException {
        String classname = "com.example.InvalidClass";
        Class<?> result = TypeHandler.createClass(classname);
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(str, result.toString());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateInvalidURL() throws ParseException {
        String str = "invalid-url";
        URL result = TypeHandler.createURL(str);
    }
    
    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createFile(str);
        assertEquals(str, result.getPath());
    }
    
    @Test
    public void testCreateFiles() {
        // TODO: Implement test
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFilesUnsupported() {
        String str = "/path/to/file1.txt,/path/to/file2.txt,/path/to/file3.txt";
        File[] result = TypeHandler.createFiles(str);
    }
}
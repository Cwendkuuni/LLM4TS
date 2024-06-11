import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        
        Object result = TypeHandler.createValue(str, obj);
        
        assertNotNull(result);
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithStringClass() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = String.class;
        
        Object result = TypeHandler.createValue(str, clazz);
        
        assertNotNull(result);
        assertEquals(clazz, result.getClass());
    }
    
    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "10";
        
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        
        assertNotNull(result);
        assertEquals(Long.valueOf(10), result);
    }
    
    @Test
    public void testCreateValueWithDate() {
        String str = "12/31/2022";
        
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "java.lang.String";
        
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testCreateValueWithFile() {
        String str = "test.txt";
        
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        
        assertNotNull(result);
        assertEquals(File.class, result.getClass());
    }
    
    @Test
    public void testCreateValueWithExistingFile() {
        String str = "test.txt";
        
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        
        assertNotNull(result);
        assertEquals(File.class, result.getClass());
    }
    
    @Test
    public void testCreateValueWithFiles() {
        String str = "test.txt";
        
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateValueWithURL() throws ParseException {
        String str = "http://www.example.com";
        
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        
        assertNotNull(result);
        assertEquals(URL.class, result.getClass());
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        
        Object result = TypeHandler.createObject(classname);
        
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassname() throws ParseException {
        String classname = "invalid.class";
        
        TypeHandler.createObject(classname);
    }
    
    @Test
    public void testCreateNumber() throws ParseException {
        String str = "10.5";
        
        Number result = TypeHandler.createNumber(str);
        
        assertNotNull(result);
        assertEquals(Double.valueOf(10.5), result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        String str = "test";
        
        TypeHandler.createNumber(str);
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        
        Class<?> result = TypeHandler.createClass(classname);
        
        assertNotNull(result);
        assertEquals(String.class, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassname() throws ParseException {
        String classname = "invalid.class";
        
        TypeHandler.createClass(classname);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "12/31/2022";
        
        TypeHandler.createDate(str);
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        
        URL result = TypeHandler.createURL(str);
        
        assertNotNull(result);
        assertEquals(str, result.toString());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String str = "invalid url";
        
        TypeHandler.createURL(str);
    }
    
    @Test
    public void testCreateFile() {
        String str = "test.txt";
        
        File result = TypeHandler.createFile(str);
        
        assertNotNull(result);
        assertEquals(str, result.getName());
    }
    
    @Test
    public void testCreateFiles() {
        String str = "test.txt";
        
        File[] result = TypeHandler.createFiles(str);
        
        assertNull(result);
    }
}
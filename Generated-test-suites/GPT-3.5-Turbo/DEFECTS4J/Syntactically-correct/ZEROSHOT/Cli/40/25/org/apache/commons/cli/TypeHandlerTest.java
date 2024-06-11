import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {
    
    @Before
    public void setUp() {
        // setup code here
    }

    @Test
    public void testCreateValueWithStringValue() throws ParseException {
        String strValue = "test";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.STRING_VALUE);
        
        assertEquals(strValue, result);
    }
    
    @Test
    public void testCreateValueWithObjectValue() throws ParseException {
        String strValue = "java.lang.Object";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.OBJECT_VALUE);
        
        assertEquals(strValue, result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithNumberValue() throws ParseException {
        String strValue = "10";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.NUMBER_VALUE);
        
        assertEquals(Long.valueOf(strValue), result);
    }
    
    @Test
    public void testCreateValueWithDateValue() throws ParseException {
        String strValue = "2020-01-01";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.DATE_VALUE);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateValueWithClassValue() throws ParseException {
        String strValue = "java.lang.String";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.CLASS_VALUE);
        
        assertEquals(strValue, result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithFileValue() throws ParseException {
        String strValue = "test.txt";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.FILE_VALUE);
        
        assertTrue(result instanceof File);
        assertEquals(strValue, ((File) result).getName());
    }
    
    @Test
    public void testCreateValueWithExistingFileValue() throws ParseException {
        String strValue = "test.txt";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.EXISTING_FILE_VALUE);
        
        assertTrue(result instanceof FileInputStream);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateValueWithFilesValue() throws ParseException {
        String strValue = "test.txt,test2.txt";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.FILES_VALUE);
        
        assertTrue(result instanceof File[]);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateValueWithURLValue() throws ParseException {
        String strValue = "http://www.example.com";
        
        Object result = TypeHandler.createValue(strValue, PatternOptionBuilder.URL_VALUE);
        
        assertTrue(result instanceof URL);
        assertEquals(strValue, ((URL) result).toString());
    }
    
    @Test
    public void testCreateValueWithInvalidType() throws ParseException {
        String strValue = "invalid";
        
        Object result = TypeHandler.createValue(strValue, Integer.class);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        
        Object result = TypeHandler.createObject(classname);
        
        assertEquals(classname, result.getClass().getName());
    }
    
    @Test(expected=ParseException.class)
    public void testCreateObjectWithNonExistentClass() throws ParseException {
        String classname = "nonexistent";
        
        TypeHandler.createObject(classname);
    }
    
    @Test
    public void testCreateNumberWithIntegerValue() throws ParseException {
        String strValue = "10";
        
        Number result = TypeHandler.createNumber(strValue);
        
        assertEquals(Long.valueOf(strValue), result);
    }
    
    @Test
    public void testCreateNumberWithDecimalValue() throws ParseException {
        String strValue = "10.5";
        
        Number result = TypeHandler.createNumber(strValue);
        
        assertEquals(Double.valueOf(strValue), result);
    }
    
    @Test(expected=ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        String strValue = "invalid";
        
        TypeHandler.createNumber(strValue);
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.Integer";
        
        Class<?> result = TypeHandler.createClass(classname);
        
        assertEquals(classname, result.getName());
    }
    
    @Test(expected=ParseException.class)
    public void testCreateClassWithNonExistentClass() throws ParseException {
        String classname = "nonexistent";
        
        TypeHandler.createClass(classname);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testCreateDate() {
        String strValue = "2020-01-01";
        
        TypeHandler.createDate(strValue);
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        String strValue = "http://www.example.com";
        
        URL result = TypeHandler.createURL(strValue);
        
        assertEquals(strValue, result.toString());
    }
    
    @Test(expected=ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String strValue = "invalid";
        
        TypeHandler.createURL(strValue);
    }
    
    @Test
    public void testCreateFile() {
        String strValue = "test.txt";
        
        File result = TypeHandler.createFile(strValue);
        
        assertEquals(strValue, result.getName());
    }
    
    @Test
    public void testOpenFile() throws ParseException {
        String strValue = "test.txt";
        
        FileInputStream result = TypeHandler.openFile(strValue);
        
        assertNotNull(result);
    }
    
    @Test(expected=ParseException.class)
    public void testOpenNonExistentFile() throws ParseException {
        String strValue = "nonexistent.txt";
        
        TypeHandler.openFile(strValue);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testCreateFiles() {
        String strValue = "path1,path2";
        
        TypeHandler.createFiles(strValue);
    }
}
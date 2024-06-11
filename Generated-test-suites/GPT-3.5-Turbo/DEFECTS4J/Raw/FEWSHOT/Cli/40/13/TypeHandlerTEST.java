
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeHandlerTest {
    
    private final String CLASSNAME = "org.apache.commons.cli.TestClass";
    private final String NUMBER = "123";
    private final String DOUBLE = "123.456";
    private final String DATE = "2021-01-01";
    private final String URL = "http://www.example.com";
    private final String FILE = "example.txt";
    private final String NON_EXISTING_FILE = "nonexisting.txt";
    private final String FILES = "file1.txt,file2.txt";
    
    @Test
    public void testCreateValueWithStringObjectValue() {
        // Given
        String str = "foo";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObjectObjectValue() {
        // Given
        String str = CLASSNAME;
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertEquals(CLASSNAME, result.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumberObjectValue() {
        // Given
        String str = NUMBER;
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertTrue(result instanceof Number);
        assertEquals(Long.valueOf(NUMBER), result);
    }
    
    @Test
    public void testCreateValueWithDoubleNumberObjectValue() {
        // Given
        String str = DOUBLE;
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertTrue(result instanceof Double);
        assertEquals(Double.valueOf(DOUBLE), result);
    }

    @Test
    public void testCreateValueWithDateObjectValue() {
        // Given
        String str = DATE;
        Object obj = PatternOptionBuilder.DATE_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertNull(result);
    }

    @Test
    public void testCreateValueWithClassObjectValue() {
        // Given
        String str = CLASSNAME;
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertEquals(CLASSNAME, result.getClass().getName());
    }

    @Test
    public void testCreateValueWithFileObjectValue() {
        // Given
        String str = FILE;
        Object obj = PatternOptionBuilder.FILE_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertTrue(result instanceof File);
        assertEquals(FILE, ((File) result).getName());
    }

    @Test
    public void testCreateValueWithExistingFileObjectValue() {
        // Given
        String str = FILE;
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertTrue(result instanceof FileInputStream);
    }

    @Test
    public void testCreateValueWithFilesObjectValue() {
        // Given
        String str = FILES;
        Object obj = PatternOptionBuilder.FILES_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertNull(result);
    }

    @Test
    public void testCreateValueWithURLObjectValue() {
        // Given
        String str = URL;
        Object obj = PatternOptionBuilder.URL_VALUE;
        
        // When
        Object result = TypeHandler.createValue(str, obj);
        
        // Then
        assertTrue(result instanceof URL);
        assertEquals(URL, ((URL) result).toString());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidObjectValue() throws ParseException {
        // Given
        String str = "invalid";
        Object obj = "InvalidObject";
        
        // When
        TypeHandler.createValue(str, obj);
        
        // Then
        // Expect ParseException
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        // Given
        String classname = CLASSNAME;
        
        // When
        Object result = TypeHandler.createObject(classname);
        
        // Then
        assertEquals(CLASSNAME, result.getClass().getName());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassname() throws ParseException {
        // Given
        String classname = "InvalidClass";
        
        // When
        TypeHandler.createObject(classname);
        
        // Then
        // Expect ParseException
    }
    
    @Test
    public void testCreateNumberWithLongValue() throws ParseException {
        // Given
        String str = NUMBER;
        
        // When
        Number result = TypeHandler.createNumber(str);
        
        // Then
        assertEquals(Long.valueOf(NUMBER), result);
    }
    
    @Test
    public void testCreateNumberWithDoubleValue() throws ParseException {
        // Given
        String str = DOUBLE;
        
        // When
        Number result = TypeHandler.createNumber(str);
        
        // Then
        assertEquals(Double.valueOf(DOUBLE), result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        // Given
        String str = "invalid";
        
        // When
        TypeHandler.createNumber(str);
        
        // Then
        // Expect ParseException
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        // Given
        String classname = CLASSNAME;
        
        // When
        Class<?> result = TypeHandler.createClass(classname);
        
        // Then
        assertEquals(CLASSNAME, result.getName());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassname() throws ParseException {
        // Given
        String classname = "InvalidClass";
        
        // When
        TypeHandler.createClass(classname);
        
        // Then
        // Expect ParseException
    }
    
    @Test
    public void testCreateDate() {
        // Given
        String str = DATE;
        
        // When
        Date result = TypeHandler.createDate(str);
        
        // Then
        assertNull(result);
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        // Given
        String str = URL;
        
        // When
        URL result = TypeHandler.createURL(str);
        
        // Then
        assertEquals(URL, result.toString());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        // Given
        String str = "invalid";
        
        // When
        TypeHandler.createURL(str);
        
        // Then
        // Expect ParseException
    }
    
    @Test
    public void testCreateFile() {
        // Given
        String str = FILE;
        
        // When
        File result = TypeHandler.createFile(str);
        
        // Then
        assertEquals(FILE, result.getName());
    }
    
    @Test
    public void testOpenFile() throws ParseException {
        // Given
        String str = FILE;
        
        // When
        FileInputStream result = TypeHandler.openFile(str);
        
        // Then
        assertTrue(result instanceof FileInputStream);
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFileWithNonExistingFile() throws ParseException {
        // Given
        String str = NON_EXISTING_FILE;
        
        // When
        TypeHandler.openFile(str);
        
        // Then
        // Expect ParseException
    }
    
    @Test
    public void testCreateFiles() {
        // Given
        String str = FILES;
        
        // When
        File[] result = TypeHandler.createFiles(str);
        
        // Then
        assertNull(result);
    }
}

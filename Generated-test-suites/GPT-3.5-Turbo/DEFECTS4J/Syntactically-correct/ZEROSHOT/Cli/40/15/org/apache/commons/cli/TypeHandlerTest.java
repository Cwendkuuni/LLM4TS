import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValue_stringValue() throws ParseException {
        String str = "Hello";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;
        assertEquals(str, TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_objectValue() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;
        assertEquals(Class.forName(str), TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_numberValue() throws ParseException {
        String str = "10";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        assertEquals(Long.valueOf(str), TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_dateValue() throws ParseException {
        String str = "2022-01-01";
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;
        assertNull(TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_classValue() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;
        assertEquals(Class.forName(str), TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_fileValue() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.FILE_VALUE;
        assertEquals(new File(str), TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_existingFileValue() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        assertNotNull(TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_filesValue() throws ParseException {
        String str = "test1.txt,test2.txt,test3.txt";
        Class<?> clazz = PatternOptionBuilder.FILES_VALUE;
        assertNull(TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateValue_urlValue() throws ParseException {
        String str = "http://www.example.com";
        Class<?> clazz = PatternOptionBuilder.URL_VALUE;
        assertEquals(new URL(str), TypeHandler.createValue(str, clazz));
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        assertEquals(Class.forName(classname), TypeHandler.createObject(classname));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObject_classNotFound() throws ParseException {
        String classname = "com.example.NonExistingClass";
        TypeHandler.createObject(classname);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObject_objectCreationFailed() throws ParseException {
        String classname = "java.lang.StringBuilder"; // No empty constructor
        TypeHandler.createObject(classname);
    }
    
    @Test
    public void testCreateNumber_longValue() throws ParseException {
        String str = "10";
        assertEquals(Long.valueOf(str), TypeHandler.createNumber(str));
    }
    
    @Test
    public void testCreateNumber_doubleValue() throws ParseException {
        String str = "10.5";
        assertEquals(Double.valueOf(str), TypeHandler.createNumber(str));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumber_invalidNumber() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        assertEquals(Class.forName(classname), TypeHandler.createClass(classname));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClass_classNotFound() throws ParseException {
        String classname = "com.example.NonExistingClass";
        TypeHandler.createClass(classname);
    }
    
    @Test
    public void testCreateDate() {
        String str = "2022-01-01";
        assertNull(TypeHandler.createDate(str));
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        assertEquals(new URL(str), TypeHandler.createURL(str));
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURL_malformedURL() throws ParseException {
        String str = "example.com";
        TypeHandler.createURL(str);
    }
    
    @Test
    public void testCreateFile() {
        String str = "test.txt";
        assertEquals(new File(str), TypeHandler.createFile(str));
    }
    
    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        assertNotNull(TypeHandler.openFile(str));
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFile_fileNotFound() throws ParseException {
        String str = "non-existing-file.txt";
        TypeHandler.openFile(str);
    }
    
    @Test
    public void testCreateFiles() {
        String str = "test1.txt,test2.txt,test3.txt";
        assertNull(TypeHandler.createFiles(str));
    }
}
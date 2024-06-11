import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValue_stringValue_validString() throws ParseException {
        String str = "testString";
        Object expected = str;
        
        Object result = TypeHandler.createValue(str, String.class);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCreateValue_stringValue_nullString() throws ParseException {
        String str = null;
        Object expected = null;
        
        Object result = TypeHandler.createValue(str, String.class);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCreateValue_stringValue_emptyString() throws ParseException {
        String str = "";
        Object expected = "";
        
        Object result = TypeHandler.createValue(str, String.class);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCreateValue_stringValue_differentString() throws ParseException {
        String str = "testString";
        Object expected = "differentString";
        
        Object result = TypeHandler.createValue(str, String.class);
        
        assertNotEquals(expected, result);
    }
    
    @Test
    public void testCreateValue_objectValue_validObject() throws ParseException {
        String str = "java.lang.String";
        Object expected = String.class;
        
        Object result = TypeHandler.createValue(str, Object.class);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValue_objectValue_invalidObject() throws ParseException {
        String str = "nonExistentClass";
        TypeHandler.createValue(str, Object.class);
        
        fail("Expected ParseException was not thrown");
    }
    
    @Test
    public void testCreateValue_numberValue_validInteger() throws ParseException {
        String str = "123";
        Object expected = 123;
        
        Object result = TypeHandler.createValue(str, Number.class);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCreateValue_numberValue_validDouble() throws ParseException {
        String str = "123.45";
        Object expected = 123.45;
        
        Object result = TypeHandler.createValue(str, Number.class);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValue_numberValue_invalidNumber() throws ParseException {
        String str = "nonNumericValue";
        TypeHandler.createValue(str, Number.class);
        
        fail("Expected ParseException was not thrown");
    }
    
    @Test
    public void testCreateValue_dateValue() {
        String str = "2020-01-01";
        
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createValue(str, Date.class);
        });
        
        String expectedMessage = "Not yet implemented";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testCreateValue_classValue_validClass() throws ParseException {
        String str = "java.lang.String";
        Object expected = String.class;
        
        Object result = TypeHandler.createValue(str, Class.class);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValue_classValue_invalidClass() throws ParseException {
        String str = "nonExistentClass";
        TypeHandler.createValue(str, Class.class);
        
        fail("Expected ParseException was not thrown");
    }
    
    @Test
    public void testCreateValue_fileValue_validFilePath() {
        String str = "testFile.txt";
        File expected = new File(str);
        
        File result = TypeHandler.createValue(str, File.class);
        
        assertEquals(expected, result);
        assertFalse(expected.isDirectory());
    }
    
    @Test
    public void testCreateValue_fileValue_validDirectoryPath() {
        String str = "testDirectory";
        File expected = new File(str);
        
        File result = TypeHandler.createValue(str, File.class);
        
        assertEquals(expected, result);
        assertTrue(expected.isDirectory());
    }
    
    @Test
    public void testCreateValue_existingFileValue_validFilePath() throws ParseException {
        String str = "existingFile.txt";
        FileInputStream expected = mock(FileInputStream.class);
        
        FileInputStream result = TypeHandler.createValue(str, FileInputStream.class);
        
        
        assertNotNull(result);
        verify(expected).close();
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValue_existingFileValue_nonExistentFilePath() throws ParseException {
        String str = "nonExistentFile.txt";
        TypeHandler.createValue(str, FileInputStream.class);
        
        fail("Expected ParseException was not thrown");
    }
    
    @Test(expected = ParseException.class)
    public void testCreateValue_existingFileValue_directoryPath() throws ParseException {
        String str = "testDirectory";
        TypeHandler.createValue(str, FileInputStream.class);
        
        fail("Expected ParseException was not thrown");
    }
    
    @Test
    public void testCreateValue_filesValue() {
        String str = "file1.txt,file2.txt";
        
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createValue(str, File[].class);
        });
        
        String expectedMessage = "Not yet implemented";
        String actualMessage = exception.getMessage();
        
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testCreateURL_validURL() throws ParseException {
        String str = "http://www.example.com";
        URL expected = new URL(str);
        
        URL result = TypeHandler.createURL(str);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURL_invalidURL() throws ParseException {
        String str = "invalidURL";
        TypeHandler.createURL(str);
        
        fail("Expected ParseException was not thrown");
    }
}
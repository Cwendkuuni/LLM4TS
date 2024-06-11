import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValue() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
        
        result = TypeHandler.createValue("org.example.TestClass", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(TestClass.class, result);
        
        result = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals(new File("path/to/file.txt"), result);
        
        result = TypeHandler.createValue("https://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertEquals(new URL("https://www.example.com"), result);
    }
    
    @Test
    public void testCreateNumber() {
        Number result = TypeHandler.createNumber("123.45");
        assertEquals(123.45, result);
        
        result = TypeHandler.createNumber("not a number");
        assertNull(result);
    }
    
    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("org.example.TestClass");
        assertEquals(TestClass.class, result);
        
        result = TypeHandler.createClass("unknown_class");
        assertNull(result);
    }
    
    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2020-01-01");
        assertNotNull(result);
        
        result = TypeHandler.createDate("not a date");
        assertNull(result);
    }
    
    @Test
    public void testCreateURL() {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertNotNull(result);
        
        result = TypeHandler.createURL("not a url");
        assertNull(result);
    }
    
    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("path/to/file.txt");
        assertEquals(new File("path/to/file.txt"), result);
    }
    
    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("path1,path2,path3");
        // implement/port FileW.findFiles() and test the actual result
    }
}
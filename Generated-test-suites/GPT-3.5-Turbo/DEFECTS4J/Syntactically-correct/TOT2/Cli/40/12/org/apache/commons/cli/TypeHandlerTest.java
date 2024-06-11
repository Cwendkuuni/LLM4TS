import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        
        assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "java.lang.String";
        Object result = TypeHandler.createObject(str);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        
        assertEquals(Long.valueOf(10), result);
    }
    
    @Test
    public void testCreateValueWithClassValue() throws ParseException {
        String str = "java.lang.String";
        Class<?> result = TypeHandler.createClass(str);
        
        assertEquals(String.class, result);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException {
        TypeHandler.createDate("2022-10-10");
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        
        assertNotNull(result);
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFile() throws ParseException {
        String str = "nonexistent.txt";
        TypeHandler.openFile(str);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String str = "test";
        TypeHandler.createFiles(str);
    }
}
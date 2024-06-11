import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.ParseException;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class TypeHandlerTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals("test", result);
    }
    
    @Test
    public void testCreateValueWithStringClass() throws ParseException {
        Object result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof Number);
        assertEquals(10, ((Number) result).intValue());
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
        assertTrue(result instanceof String);
    }
    
    @Test
    public void testCreateNumber() throws ParseException {
        Number result = TypeHandler.createNumber("10");
        assertNotNull(result);
        assertEquals(10, result.intValue());
        
        result = TypeHandler.createNumber("10.5");
        assertNotNull(result);
        assertEquals(10.5, result.doubleValue(), 0.001);
        
        exception.expect(ParseException.class);
        TypeHandler.createNumber("invalid");
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertNotNull(result);
        assertEquals(String.class, result);
        
        exception.expect(ParseException.class);
        TypeHandler.createClass("invalidClassName");
    }
    
    @Test
    public void testCreateDate() {
        exception.expect(UnsupportedOperationException.class);
        TypeHandler.createDate("2021-10-10");
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertNotNull(result);
        
        exception.expect(ParseException.class);
        TypeHandler.createURL("invalidUrl");
    }
    
    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("/tmp/testFile.txt");
        assertNotNull(result);
        
        exception.expect(FileNotFoundException.class);
        TypeHandler.createFile("invalidFilePath");
    }
    
    @Test
    public void testCreateFiles() {
        exception.expect(UnsupportedOperationException.class);
        TypeHandler.createFiles("/tmp/test1.txt,/tmp/test2.txt");
    }
}
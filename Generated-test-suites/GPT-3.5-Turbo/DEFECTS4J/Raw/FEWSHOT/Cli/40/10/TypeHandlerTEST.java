import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() throws ParseException {
        String str = "test";
        Object obj = new Object();
        
        assertEquals(str, TypeHandler.createValue(str, String.class));
        assertEquals(obj, TypeHandler.createValue(str, Object.class));
        
        assertNull(TypeHandler.createValue(str, Integer.class));
        assertNull(TypeHandler.createValue(str, Double.class));
        assertNull(TypeHandler.createValue(str, Date.class));
        assertNull(TypeHandler.createValue(str, File.class));
        assertNull(TypeHandler.createValue(str, URL.class));
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.util.ArrayList";
        Object obj = TypeHandler.createObject(classname);
        
        assertTrue(obj instanceof ArrayList);
    }
    
    @Test
    public void testCreateNumber() throws ParseException {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        
        assertEquals(Integer.valueOf(str), number);
        
        str = "123.45";
        number = TypeHandler.createNumber(str);
        
        assertEquals(Double.valueOf(str), number);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateDate() {
        String str = "2022-01-01";
        TypeHandler.createDate(str);
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        
        assertEquals(str, url.toString());
    }
    
    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File file = TypeHandler.createFile(str);
        
        assertEquals(str, file.getPath());
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFile() throws ParseException {
        String str = "/path/to/nonexistent/file.txt";
        TypeHandler.openFile(str);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String str = "/path/to/files/*.txt";
        TypeHandler.createFiles(str);
    }
}
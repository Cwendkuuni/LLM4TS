import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TypeHandlerTest {

    private String str;
    private Class<?> clazz;
    
    @Before
    public void setUp() {
        str = "test";
        clazz = String.class;
    }
    
    @Test
    public void testCreateValue() throws ParseException {
        Object result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
        assertEquals(clazz, result.getClass());
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        
        Object result = TypeHandler.createObject(classname);
        assertNotNull(result);
        assertEquals(classname, result.getClass().getName());
    }
    
    @Test
    public void testCreateNumber() throws ParseException {
        String numberStr1 = "123";
        String numberStr2 = "123.45";
        
        Number number1 = TypeHandler.createNumber(numberStr1);
        assertNotNull(number1);
        assertEquals(Long.valueOf(numberStr1), number1);
        
        Number number2 = TypeHandler.createNumber(numberStr2);
        assertNotNull(number2);
        assertEquals(Double.valueOf(numberStr2), number2);
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        
        Class<?> result = TypeHandler.createClass(classname);
        assertNotNull(result);
        assertEquals(classname, result.getName());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String dateStr = "2021-10-20";
        
        TypeHandler.createDate(dateStr);
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        String urlStr = "http://www.example.com";
        
        URL url = TypeHandler.createURL(urlStr);
        assertNotNull(url);
        assertEquals(urlStr, url.toString());
    }
    
    @Test
    public void testCreateFile() {
        String fileStr = "test.txt";
        
        File file = TypeHandler.createFile(fileStr);
        assertNotNull(file);
        assertEquals(fileStr, file.getName());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String filesStr = "file1.txt,file2.txt";
        
        TypeHandler.createFiles(filesStr);
    }
    
}
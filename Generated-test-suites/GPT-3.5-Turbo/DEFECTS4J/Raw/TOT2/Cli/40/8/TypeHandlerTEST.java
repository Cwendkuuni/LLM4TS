import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValue() throws ParseException {
        Object obj = TypeHandler.createValue("testString", String.class);
        assertNotNull(obj);
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertNotNull(obj);
    }
    
    @Test
    public void testCreateNumber() throws ParseException {
        Number num = TypeHandler.createNumber("123");
        assertNotNull(num);
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        Class<?> clazz = TypeHandler.createClass("java.lang.String");
        assertNotNull(clazz);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-01-01");
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        URL url = TypeHandler.createURL("http://www.example.com");
        assertNotNull(url);
    }
    
    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFile() throws ParseException {
        FileInputStream fis = TypeHandler.openFile("nonexistent.txt");
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("dir/");
    }
}
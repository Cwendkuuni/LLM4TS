import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        Object obj = new Object();
        try {
            Object value = TypeHandler.createValue("test", obj);
            assertNotNull(value);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertNotNull(obj);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCreateNumber() {
        try {
            Number number = TypeHandler.createNumber("10");
            assertNotNull(number);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertNotNull(clazz);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2020-01-01");
    }
    
    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertNotNull(url);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("test1.txt,test2.txt");
    }
}
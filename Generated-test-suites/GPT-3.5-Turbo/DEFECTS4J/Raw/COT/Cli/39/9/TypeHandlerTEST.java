
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueWithString() {
        String str = "test";
        Object obj = "java.lang.String";
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertEquals(str, result);
        } catch (ParseException e) {
            fail("ParseException thrown when not expected");
        }
    }
    
    @Test
    public void testCreateValueWithClass() {
        String str = "test";
        Class<?> clazz = String.class;
        try {
            Object result = TypeHandler.createValue(str, clazz);
            assertEquals(str, result);
        } catch (ParseException e) {
            fail("ParseException thrown when not expected");
        }
    }
    
    @Test
    public void testCreateValueWithObject() {
        String str = "java.lang.Object";
        try {
            Object result = TypeHandler.createObject(str);
            assertEquals(Object.class, result.getClass());
        } catch (ParseException e) {
            fail("ParseException thrown when not expected");
        }
    }
    
    @Test
    public void testCreateValueWithNumber() {
        String str = "10";
        try {
            Object result = TypeHandler.createNumber(str);
            assertTrue(result instanceof Number);
            assertEquals(10L, result);
        } catch (ParseException e) {
            fail("ParseException thrown when not expected");
        }
    }
    
    @Test
    public void testCreateValueWithDate() {
        String str = "2020-01-01";
        try {
            Object result = TypeHandler.createDate(str);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    @Test
    public void testCreateValueWithURL() {
        String str = "http://www.example.com";
        try {
            Object result = TypeHandler.createURL(str);
            assertTrue(result instanceof URL);
            assertEquals(str, result.toString());
        } catch (ParseException e) {
            fail("ParseException thrown when not expected");
        }
    }
    
    @Test
    public void testCreateValueWithFile() {
        String str = "test.txt";
        try {
            Object result = TypeHandler.createFile(str);
            assertTrue(result instanceof File);
            assertEquals(str, ((File) result).getName());
        } catch (ParseException e) {
            fail("ParseException thrown when not expected");
        }
    }
    
    @Test
    public void testCreateValueWithFiles() {
        String str = "dir";
        try {
            Object result = TypeHandler.createFiles(str);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}

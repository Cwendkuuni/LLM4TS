
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        String str = "test";
        Object obj = new Object();
        
        try {
            Object result = TypeHandler.createValue(str, obj);
            assertNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
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
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        
        try {
            Object result = TypeHandler.createObject(className);
            assertNotNull(result);
            assertTrue(result instanceof String);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateObjectWithInvalidClassName() {
        String className = "invalid.Class";
        
        try {
            TypeHandler.createObject(className);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: " + className, e.getMessage());
        }
    }

    @Test
    public void testCreateNumber() {
        String numberStr = "10";
        
        try {
            Number result = TypeHandler.createNumber(numberStr);
            assertNotNull(result);
            assertTrue(result instanceof Long);
            assertEquals(10L, result.longValue());
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateNumberWithDecimal() {
        String numberStr = "10.5";
        
        try {
            Number result = TypeHandler.createNumber(numberStr);
            assertNotNull(result);
            assertTrue(result instanceof Double);
            assertEquals(10.5, result.doubleValue(), 0.001);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateNumberWithInvalidNumber() {
        String numberStr = "test";
        
        try {
            TypeHandler.createNumber(numberStr);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("For input string: \"test\"", e.getMessage());
        }
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        
        try {
            Class<?> result = TypeHandler.createClass(className);
            assertNotNull(result);
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateClassWithInvalidClassName() {
        String className = "invalid.Class";
        
        try {
            TypeHandler.createClass(className);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: " + className, e.getMessage());
        }
    }

    @Test
    public void testCreateDate() {
        String dateStr = "2021-01-01";
        
        try {
            Date result = TypeHandler.createDate(dateStr);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testCreateURL() {
        String urlStr = "http://www.example.com";
        
        try {
            URL result = TypeHandler.createURL(urlStr);
            assertNotNull(result);
            assertEquals(urlStr, result.toString());
        } catch (ParseException e) {
            fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateURLWithInvalidURL() {
        String urlStr = "invalid.url";
        
        try {
            TypeHandler.createURL(urlStr);
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unable to parse the URL: " + urlStr, e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        String path = "test.txt";
        File expected = new File(path);
        
        File result = TypeHandler.createFile(path);
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateFiles() {
        String paths = "file1.txt,file2.txt";
        
        try {
            File[] result = TypeHandler.createFiles(paths);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }
}

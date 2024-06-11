import org.apache.commons.cli.ParseException;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        try {
            String str = "testString";
            Object obj = new Object();
            Object result = TypeHandler.createValue(str, obj);
            
            assertEquals(obj, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateValue_String_Class() {
        try {
            String str = "testString";
            Class<?> clazz = String.class;
            Object result = TypeHandler.createValue(str, clazz);
            
            assertEquals(str, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateValue_String_Class_Invalid() {
        try {
            String str = "testString";
            Class<?> clazz = Integer.class;
            Object result = TypeHandler.createValue(str, clazz);
            
            assertNull(result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateObject() {
        try {
            String classname = "java.lang.String";
            Object result = TypeHandler.createObject(classname);
            
            assertNotNull(result);
            assertEquals(String.class, result.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateObject_ClassNotFound() {
        try {
            String classname = "test.NonExistingClass";
            Object result = TypeHandler.createObject(classname);
            
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: test.NonExistingClass", e.getMessage());
        }
    }

    @Test
    public void testCreateNumber_Integer() {
        try {
            String str = "123";
            Number result = TypeHandler.createNumber(str);
            
            assertNotNull(result);
            assertEquals(Integer.class, result.getClass());
            assertEquals(123, result.intValue());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateNumber_Double() {
        try {
            String str = "123.45";
            Number result = TypeHandler.createNumber(str);
            
            assertNotNull(result);
            assertEquals(Double.class, result.getClass());
            assertEquals(123.45, result.doubleValue(), 0.001);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateNumber_Invalid() {
        try {
            String str = "abc";
            TypeHandler.createNumber(str);
            
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("For input string: \"abc\"", e.getMessage());
        }
    }

    @Test
    public void testCreateClass() {
        try {
            String classname = "java.lang.String";
            Class<?> result = TypeHandler.createClass(classname);
            
            assertNotNull(result);
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateClass_ClassNotFound() {
        try {
            String classname = "test.NonExistingClass";
            TypeHandler.createClass(classname);
            
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: test.NonExistingClass", e.getMessage());
        }
    }

    @Test
    public void testCreateDate_NotImplemented() {
        String str = "2020-01-01";
        try {
            TypeHandler.createDate(str);
            
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testCreateURL() {
        try {
            String str = "http://www.example.com";
            URL result = TypeHandler.createURL(str);
            
            assertNotNull(result);
            assertEquals(URL.class, result.getClass());
            assertEquals(str, result.toString());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        } catch (MalformedURLException e) {
            fail("Unexpected MalformedURLException");
        }
    }

    @Test
    public void testCreateURL_Malformed() {
        try {
            String str = "not_a_url";
            TypeHandler.createURL(str);
            
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unable to parse the URL: not_a_url", e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createFile(str);
        
        assertNotNull(result);
        assertEquals(File.class, result.getClass());
        assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles_NotImplemented() {
        String str = "/path/to/*.txt";
        try {
            TypeHandler.createFiles(str);
            
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Not yet implemented", e.getMessage());
        }
    }

}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.junit.Test;

public class TypeHandlerTest {
    private static final String CLASSNAME = "org.example.TestClass";
    private static final String NUMBER_STR = "123";
    private static final String DECIMAL_NUMBER_STR = "123.45";
    private static final String DATE_STR = "2021-01-01";
    private static final String URL_STR = "http://www.example.com";
    private static final String FILE_STR = "test.txt";
    private static final String FILES_STR = "file1.txt,file2.txt,file3.txt";
    
    @Test
    public void testCreateValue() {
        try {
            Object value = TypeHandler.createValue("test", String.class);
            assertEquals("test", value);
            
            value = TypeHandler.createValue(CLASSNAME, Object.class);
            assertEquals(CLASSNAME, value.getClass().getName());
            
            value = TypeHandler.createValue(NUMBER_STR, Number.class);
            assertTrue(value instanceof Number);
            
            value = TypeHandler.createValue(DECIMAL_NUMBER_STR, Number.class);
            assertTrue(value instanceof Number);
            
            value = TypeHandler.createValue(DATE_STR, Date.class);
            assertNull(value);
            
            value = TypeHandler.createValue(CLASSNAME, Class.class);
            assertEquals(CLASSNAME, ((Class<?>) value).getName());
            
            value = TypeHandler.createValue(FILE_STR, File.class);
            assertTrue(value instanceof File);
            
            value = TypeHandler.createValue(FILE_STR, File.class);
            assertTrue(value instanceof File);
            
            value = TypeHandler.createValue(FILES_STR, File[].class);
            assertTrue(value instanceof File[]);
            
            value = TypeHandler.createValue(URL_STR, URL.class);
            assertTrue(value instanceof URL);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject(CLASSNAME);
            assertEquals(CLASSNAME, obj.getClass().getName());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateNumber() {
        try {
            Number number = TypeHandler.createNumber(NUMBER_STR);
            assertEquals(Long.parseLong(NUMBER_STR), number);
            
            number = TypeHandler.createNumber(DECIMAL_NUMBER_STR);
            assertEquals(Double.parseDouble(DECIMAL_NUMBER_STR), number);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass(CLASSNAME);
            assertEquals(CLASSNAME, clazz.getName());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL(URL_STR);
            assertEquals(URL_STR, url.toString());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile(FILE_STR);
        assertEquals(FILE_STR, file.getPath());
    }
    
    @Test
    public void testCreateFiles() {
        try {
            File[] files = TypeHandler.createFiles(FILES_STR);
            assertEquals(3, files.length);
        } catch (UnsupportedOperationException e) {
            // Test should fail if this method hasn't been implemented yet
            fail(e.getMessage());
        }
    }
}

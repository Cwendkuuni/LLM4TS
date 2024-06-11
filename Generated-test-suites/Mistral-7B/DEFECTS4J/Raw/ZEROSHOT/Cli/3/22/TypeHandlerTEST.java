import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "test";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_STRING_VALUE() {
        String str = "test";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringPatternOptionBuilder_OBJECT_VALUE() {
        String str = "test.class";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        Class clazz = Class.forName(str.substring(0, str.lastIndexOf('.')));
        Object expected = Class.forName(str).newInstance();
        Assert.assertNotNull(result);
        Assert.assertSame(clazz, result.getClass());
    }

    @Test
    public void testCreateValue_StringNumber() {
        String str = "123";
        Number result = TypeHandler.createValue(str, Number.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateValue_StringNumber_Double() {
        String str = "123.45";
        Number result = TypeHandler.createValue(str, Double.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateValue_StringDate() {
        String str = "2023-03-01";
        Date result = TypeHandler.createValue(str, Date.class);
        Assert.assertNotNull(result);
        Assert.assertEquals("Wed Mar 01 00:00:00 GMT+01:00 2023", result.toString());
    }

    @Test
    public void testCreateValue_StringClass_NotFound() {
        String str = "test.NonExistentClass";
        Class clazz = Class.forName(str);
        try {
            Object result = TypeHandler.createValue(str, clazz);
            fail("Expected an exception");
        } catch (ClassNotFoundException ex) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringDate_InvalidFormat() {
        String str = "InvalidDateFormat";
        Date result = TypeHandler.createValue(str, Date.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_StringURL() {
        String str = "http://example.com";
        URL result = TypeHandler.createValue(str, URL.class);
        Assert.assertNotNull(result);
        Assert.assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testCreateValue_StringURL_Malformed() {
        String str = "InvalidURL";
        try {
            URL result = TypeHandler.createValue(str, URL.class);
            fail("Expected an exception");
        } catch (MalformedURLException ex) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringFile() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createValue(str, File.class);
        Assert.assertNotNull(result);
        Assert.assertEquals("/path/to/file.txt", result.getAbsolutePath());
    }

    @Test
    public void testCreateValue_StringFile_NotFound() {
        String str = "/non/existent/file.txt";
        File result = TypeHandler.createValue(str, File.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_StringClassArray() {
        String str = "java.lang.String,java.lang.String";
        Class[] expectedClasses = {String.class, String.class};
        Class[] resultClasses = TypeHandler.createValue(str, Class[].class);
        Assert.assertArrayEquals(expectedClasses, resultClasses);
    }

    @Test
    public void testCreateValue_StringClassArray_InvalidFormat() {
        String str = "java.lang.String,java.lang.Integer";
        try {
            Class[] resultClasses = TypeHandler.createValue(str, Class[].class);
            fail("Expected an exception");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }
}
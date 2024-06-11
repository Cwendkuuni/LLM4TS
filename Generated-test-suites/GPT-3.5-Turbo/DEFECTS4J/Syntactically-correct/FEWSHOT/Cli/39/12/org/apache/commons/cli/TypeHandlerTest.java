import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue() throws ParseException {
        Object result;

        result = TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE);
        assertEquals("foo", result);

        result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals("java.lang.String", result);

        result = TypeHandler.createValue("42", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Long.valueOf(42), result);

        result = TypeHandler.createValue("3.14", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Double.valueOf(3.14), result);

        result = TypeHandler.createValue("2020-05-20", PatternOptionBuilder.DATE_VALUE);
        assertEquals("2020-05-20", result);

        result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(Object.class, result);

        result = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.FILE_VALUE);
        assertEquals(new File("/path/to/file"), result);

        result = TypeHandler.createValue("/path/to/existing/file", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(new File("/path/to/existing/file"), result);

        result = TypeHandler.createValue("/path/to/file1,/path/to/file2", PatternOptionBuilder.FILES_VALUE);
        assertEquals(new File[]{new File("/path/to/file1"), new File("/path/to/file2")}, result);

        result = TypeHandler.createValue("https://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertEquals(new URL("https://www.example.com"), result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result;

        result = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, result);

        result = TypeHandler.createObject("java.lang.Object");
        assertEquals(Object.class, result);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number result;

        result = TypeHandler.createNumber("42");
        assertEquals(Long.valueOf(42), result);

        result = TypeHandler.createNumber("3.14");
        assertEquals(Double.valueOf(3.14), result);
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result;

        result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);

        result = TypeHandler.createClass("java.lang.Object");
        assertEquals(Object.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2020-05-20");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result;

        result = TypeHandler.createURL("https://www.example.com");
        assertEquals(new URL("https://www.example.com"), result);
    }

    @Test
    public void testCreateFile() {
        File result;

        result = TypeHandler.createFile("/path/to/file");
        assertEquals(new File("/path/to/file"), result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("/path/to/file1,/path/to/file2");
    }
}
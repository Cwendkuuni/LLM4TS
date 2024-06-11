import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValueWithString() {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueWithObject() {
        assertEquals("java.lang.Object", TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE).getClass().getName());
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueWithNumber() {
        assertEquals(10, TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE));
        assertNull(TypeHandler.createValue("ten", PatternOptionBuilder.NUMBER_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValueWithDate() {
        Date date = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        assertEquals(2021, date.getYear() + 1900);
        assertEquals(0, date.getMonth());
        assertEquals(1, date.getDate());
        assertNull(TypeHandler.createValue("invalid", PatternOptionBuilder.DATE_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValueWithClass() {
        assertEquals(String.class, TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE));
        assertNull(TypeHandler.createValue("non.existing.Class", PatternOptionBuilder.CLASS_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.CLASS_VALUE));
    }

    @Test
    public void testCreateValueWithFile() {
        File file = TypeHandler.createValue("path/to/file", PatternOptionBuilder.FILE_VALUE);
        assertEquals("path/to/file", file.getPath());
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValueWithExistingFile() {
        File file = TypeHandler.createValue("path/to/existing/file", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals("path/to/existing/file", file.getPath());
        assertNull(TypeHandler.createValue("path/to/non-existing/file", PatternOptionBuilder.EXISTING_FILE_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValueWithFiles() {
        File[] files = TypeHandler.createValue("path/to/files", PatternOptionBuilder.FILES_VALUE);
        assertNull(files);
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValueWithURL() {
        try {
            URL url = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
            assertEquals("http://www.example.com", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            assertNull(e);
        }
        
        try {
            URL url = TypeHandler.createValue("invalid-url", PatternOptionBuilder.URL_VALUE);
            assertNull(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            assertNull(e);
        }
        
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateObject() {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertEquals("java.lang.String", obj.getClass().getName());
        assertNull(TypeHandler.createObject("non.existing.Class"));
        assertNull(TypeHandler.createObject(null));
    }

    @Test
    public void testCreateNumber() {
        assertEquals(10, TypeHandler.createNumber("10"));
        assertNull(TypeHandler.createNumber("ten"));
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertNull(TypeHandler.createClass("non.existing.Class"));
        assertNull(TypeHandler.createClass(null));
    }

    @Test
    public void testCreateDate() {
        Date date = TypeHandler.createDate("2021-01-01");
        assertEquals(2021, date.getYear() + 1900);
        assertEquals(0, date.getMonth());
        assertEquals(1, date.getDate());
        assertNull(TypeHandler.createDate("invalid"));
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertEquals("http://www.example.com", url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            assertNull(e);
        }
        
        try {
            URL url = TypeHandler.createURL("invalid-url");
            assertNull(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            assertNull(e);
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("path/to/file");
        assertEquals("path/to/file", file.getPath());
    }

    @Test
    public void testCreateFiles() {
        File[] files = TypeHandler.createFiles("path/to/files");
        assertNull(files);
    }
}
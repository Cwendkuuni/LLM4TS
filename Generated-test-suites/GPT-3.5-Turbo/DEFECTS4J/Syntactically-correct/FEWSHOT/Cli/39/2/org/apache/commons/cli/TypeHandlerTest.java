package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithValidStringAndClass() throws ParseException {
        Object obj = TypeHandler.createValue("string", String.class);
        assertNotNull(obj);
        assertEquals(String.class, obj.getClass());

        obj = TypeHandler.createValue("java.lang.String", Class.class);
        assertNotNull(obj);
        assertEquals(Class.class, obj.getClass());

        obj = TypeHandler.createValue("42", Integer.class);
        assertNotNull(obj);
        assertEquals(Integer.class, obj.getClass());

        obj = TypeHandler.createValue("3.14", Double.class);
        assertNotNull(obj);
        assertEquals(Double.class, obj.getClass());

        obj = TypeHandler.createValue("true", Boolean.class);
        assertNotNull(obj);
        assertEquals(Boolean.class, obj.getClass());

        obj = TypeHandler.createValue("2022-01-01", Date.class);
        assertNotNull(obj);
        assertEquals(Date.class, obj.getClass());

        obj = TypeHandler.createValue("http://www.example.com", URL.class);
        assertNotNull(obj);
        assertEquals(URL.class, obj.getClass());

        obj = TypeHandler.createValue("file.txt", File.class);
        assertNotNull(obj);
        assertEquals(File.class, obj.getClass());

        obj = TypeHandler.createValue("file.txt", File[].class);
        assertNotNull(obj);
        assertEquals(File[].class, obj.getClass());
    }

    @Test
    public void testCreateValueWithInvalidStringAndClass() {
        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("invalid", String.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("java.lang.InvalidClass", Class.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("42.5", Integer.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("3.1415", Long.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("abc", Double.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("yes", Boolean.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("2022-01-01 10:00:00", Date.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("htp://www.example.com", URL.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("file/", File.class);
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue("file/1.txt,file/2.txt", File[].class);
        });
    }

    @Test
    public void testCreateValueWithEmptyStringAndClass() throws ParseException {
        Object obj = TypeHandler.createValue("", String.class);
        assertNotNull(obj);
        assertEquals(String.class, obj.getClass());
        assertEquals("", obj);

        obj = TypeHandler.createValue("", Class.class);
        assertNotNull(obj);
        assertEquals(Class.class, obj.getClass());
        assertEquals("", obj);

        obj = TypeHandler.createValue("", Integer.class);
        assertNull(obj);

        obj = TypeHandler.createValue("", Double.class);
        assertNull(obj);

        obj = TypeHandler.createValue("", Boolean.class);
        assertNull(obj);

        obj = TypeHandler.createValue("", Date.class);
        assertNull(obj);

        obj = TypeHandler.createValue("", URL.class);
        assertNull(obj);

        obj = TypeHandler.createValue("", File.class);
        assertNull(obj);

        obj = TypeHandler.createValue("", File[].class);
        assertNull(obj);
    }

    @Test
    public void testCreateObjectWithValidClassname() throws ParseException {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertNotNull(obj);
        assertEquals(String.class, obj.getClass());

        obj = TypeHandler.createObject("java.lang.Class");
        assertNotNull(obj);
        assertEquals(Class.class, obj.getClass());
    }

    @Test
    public void testCreateObjectWithInvalidClassname() {
        assertThrows(ParseException.class, () -> {
            TypeHandler.createObject("java.lang.InvalidClass");
        });
    }

    @Test
    public void testCreateNumberWithValidString() throws ParseException {
        Number num = TypeHandler.createNumber("42");
        assertNotNull(num);
        assertEquals(Integer.valueOf(42), num);

        num = TypeHandler.createNumber("3.14");
        assertNotNull(num);
        assertEquals(Double.valueOf(3.14), num);

        num = TypeHandler.createNumber("-10");
        assertNotNull(num);
        assertEquals(Integer.valueOf(-10), num);

        num = TypeHandler.createNumber("-2.5");
        assertNotNull(num);
        assertEquals(Double.valueOf(-2.5), num);
    }

    @Test
    public void testCreateNumberWithInvalidString() {
        assertThrows(ParseException.class, () -> {
            TypeHandler.createNumber("abc");
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createNumber("3.14.15");
        });
    }

    @Test
    public void testCreateClassWithValidClassname() throws ParseException {
        Class<?> clazz = TypeHandler.createClass("java.lang.String");
        assertNotNull(clazz);
        assertEquals(String.class, clazz);

        clazz = TypeHandler.createClass("java.lang.Class");
        assertNotNull(clazz);
        assertEquals(Class.class, clazz);
    }

    @Test
    public void testCreateClassWithInvalidClassname() {
        assertThrows(ParseException.class, () -> {
            TypeHandler.createClass("java.lang.InvalidClass");
        });
    }

    @Test
    public void testCreateDate() {
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createDate("2022-01-01");
        });
    }

    @Test
    public void testCreateURLWithValidURL() throws ParseException, MalformedURLException {
        URL url = TypeHandler.createURL("http://www.example.com");
        assertNotNull(url);
        assertEquals("http://www.example.com", url.toString());

        url = TypeHandler.createURL("https://www.example.com");
        assertNotNull(url);
        assertEquals("https://www.example.com", url.toString());

        url = TypeHandler.createURL("ftp://www.example.com");
        assertNotNull(url);
        assertEquals("ftp://www.example.com", url.toString());
    }

    @Test
    public void testCreateURLWithInvalidURL() {
        assertThrows(ParseException.class, () -> {
            TypeHandler.createURL("htp://www.example.com");
        });

        assertThrows(ParseException.class, () -> {
            TypeHandler.createURL("www.example.com");
        });
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("file.txt");
        assertNotNull(file);
        assertEquals("file.txt", file.getPath());

        file = TypeHandler.createFile("path/to/file.txt");
        assertNotNull(file);
        assertEquals("path/to/file.txt", file.getPath());
    }

    @Test
    public void testCreateFileWithEmptyString() {
        File file = TypeHandler.createFile("");
        assertNotNull(file);
        assertEquals("", file.getPath());
    }

    @Test
    public void testCreateFiles() {
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createFiles("file1.txt,file2.txt");
        });
    }
}
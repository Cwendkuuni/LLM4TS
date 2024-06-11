import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Class_StringValue() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Class_ObjectValue() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.Object", Object.class);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testCreateValue_String_Class_NumberValue_Long() throws ParseException {
        Object result = TypeHandler.createValue("10", Long.class);
        assertTrue(result instanceof Long);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateValue_String_Class_NumberValue_Double() throws ParseException {
        Object result = TypeHandler.createValue("10.5", Double.class);
        assertTrue(result instanceof Double);
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateValue_String_Class_DateValue() throws ParseException {
        try {
            TypeHandler.createValue("2020-01-01", Date.class);
            fail("ParseException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateValue_String_Class_ClassValue() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", Class.class);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_String_Class_FileValue() throws ParseException {
        Object result = TypeHandler.createValue("path/to/file.txt", File.class);
        assertTrue(result instanceof File);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testCreateValue_String_Class_ExistingFileValue() throws ParseException {
        Object result = TypeHandler.createValue("path/to/file.txt", File.class);
        assertTrue(result instanceof File);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testCreateValue_String_Class_FilesValue() throws ParseException {
        try {
            TypeHandler.createValue("path/to/files/*.txt", File[].class);
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateValue_String_Class_URLValue() throws ParseException, MalformedURLException {
        Object result = TypeHandler.createValue("https://www.example.com", URL.class);
        assertTrue(result instanceof URL);
        assertEquals(new URL("https://www.example.com"), result);
    }

    @Test
    public void testCreateValue_String_Object_StringValue() throws ParseException {
        Object result = TypeHandler.createValue("test", (Object) String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_String_Object_ObjectValue() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.Object", (Object) Object.class);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testCreateValue_String_Object_NumberValue_Long() throws ParseException {
        Object result = TypeHandler.createValue("10", (Object) Long.class);
        assertTrue(result instanceof Long);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateValue_String_Object_NumberValue_Double() throws ParseException {
        Object result = TypeHandler.createValue("10.5", (Object) Double.class);
        assertTrue(result instanceof Double);
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateValue_String_Object_DateValue() throws ParseException {
        try {
            TypeHandler.createValue("2020-01-01", (Object) Date.class);
            fail("ParseException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateValue_String_Object_ClassValue() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", (Object) Class.class);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_String_Object_FileValue() throws ParseException {
        Object result = TypeHandler.createValue("path/to/file.txt", (Object) File.class);
        assertTrue(result instanceof File);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testCreateValue_String_Object_ExistingFileValue() throws ParseException {
        Object result = TypeHandler.createValue("path/to/file.txt", (Object) File.class);
        assertTrue(result instanceof File);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testCreateValue_String_Object_FilesValue() throws ParseException {
        try {
            TypeHandler.createValue("path/to/files/*.txt", (Object) File[].class);
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateValue_String_Object_URLValue() throws ParseException, MalformedURLException {
        Object result = TypeHandler.createValue("https://www.example.com", (Object) URL.class);
        assertTrue(result instanceof URL);
        assertEquals(new URL("https://www.example.com"), result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        try {
            TypeHandler.createObject("java.lang.Object");
            fail("ParseException should have been thrown");
        } catch (ParseException e) {
            assertEquals("Unable to create an instance of: java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testCreateNumber_Long() throws ParseException {
        Number result = TypeHandler.createNumber("10");
        assertTrue(result instanceof Long);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        Number result = TypeHandler.createNumber("10.5");
        assertTrue(result instanceof Double);
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateNumber_Exception() {
        try {
            TypeHandler.createNumber("not a number");
            fail("ParseException should have been thrown");
        } catch (ParseException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateClass() throws ParseException {
        try {
            TypeHandler.createClass("java.lang.Object");
            assertTrue(true);
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException should not have been thrown");
        }
    }

    @Test
    public void testCreateClass_Exception() throws ParseException {
        try {
            TypeHandler.createClass("nonexistent.class");
            fail("ParseException should have been thrown");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: nonexistent.class", e.getMessage());
        }
    }

    @Test
    public void testCreateDate() {
        try {
            TypeHandler.createDate("2020-01-01");
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertTrue(result instanceof URL);
        assertEquals(new URL("https://www.example.com"), result);
    }

    @Test
    public void testCreateURL_Exception() throws MalformedURLException {
        try {
            TypeHandler.createURL("not a url");
            fail("ParseException should have been thrown");
        } catch (ParseException e) {
            assertEquals("Unable to parse the URL: not a url", e.getMessage());
        } catch (UnsupportedOperationException e) {
            fail("UnsupportedOperationException should not have been thrown");
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("path/to/file.txt");
        assertTrue(result instanceof File);
        assertEquals(new File("path/to/file.txt"), result);
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("path/to/files/*.txt");
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
}
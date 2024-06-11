import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() throws ParseException {
        // Testing createValue(String, Object)
        Object object = new Object();
        assertEquals(object, TypeHandler.createValue("test", object));

        // Testing createValue(String, Class<T>)
        assertEquals("test", TypeHandler.createValue("test", String.class));
        assertEquals(123, TypeHandler.createValue("123", Integer.class).intValue());
        assertEquals(new Date(), TypeHandler.createValue(String.valueOf(new Date()), Date.class));
        
        // Testing unsupported types
        assertNull(TypeHandler.createValue("test", Class.class));
        assertNull(TypeHandler.createValue("test", File.class));
    }

    @Test
    public void testCreateObject() throws ParseException {
        try {
            TypeHandler.createObject("org.apache.commons.cli.TestObject");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Unable to find the class"));
        }

        try {
            TypeHandler.createObject("org.apache.commons.cli.NotInstantiable");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Unable to create an instance of"));
        }
    }

    @Test
    public void testCreateNumber() throws ParseException {
        assertEquals(123, TypeHandler.createNumber("123").intValue());
        assertEquals(0.123, TypeHandler.createNumber("0.123").doubleValue(), 0.001);

        try {
            TypeHandler.createNumber("abc");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateClass() throws ParseException {
        try {
            TypeHandler.createClass("org.apache.commons.cli.TestClass");
        } catch (ParseException e) {
            assertTrue(e.getMessage().contains("Unable to find the class"));
        }
    }

    @Test
    public void testCreateDate() {
        try {
            TypeHandler.createDate("2022-01-01");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testCreateURL() throws ParseException {
        try {
            TypeHandler.createURL("http://www.example.com");
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        try {
            TypeHandler.createURL("invalid_url");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        try {
            TypeHandler.openFile("test.txt");
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }

        try {
            TypeHandler.openFile("invalid_file");
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("file1.txt,file2.txt");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}
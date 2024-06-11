import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    @After
    public void tearDown() {
        typeHandler = null;
    }

    @Test
    public void testCreateValue() {
        Object result = typeHandler.createValue("test", "String");
        assertEquals("test", result);

        result = typeHandler.createValue("1.5", "Double");
        assertTrue(result instanceof Double);
        assertEquals(1.5, result);

        result = typeHandler.createValue("123", "Integer");
        assertTrue(result instanceof Integer);
        assertEquals(123, result);

        result = typeHandler.createValue("2021/01/01", "Date");
        assertNull(result);

        result = typeHandler.createValue("java.lang.Object", "Class");
        assertTrue(result instanceof Class);
        assertEquals(Object.class, result);

        result = typeHandler.createValue("test-file.txt", "File");
        assertTrue(result instanceof File);
        assertEquals("test-file.txt", ((File) result).getPath());

        result = typeHandler.createValue("test-url.com", "URL");
        assertTrue(result instanceof URL);
        try {
            assertEquals(new URL("test-url.com"), result);
        } catch (MalformedURLException e) {
            fail("MalformedURLException");
        }

        result = typeHandler.createValue("test", "InvalidClass");
        assertNull(result);
    }

    @Test
    public void testCreateValueWithClass() {
        Object result = typeHandler.createValue("test", String.class);
        assertEquals("test", result);

        result = typeHandler.createValue("1.5", Double.class);
        assertTrue(result instanceof Double);
        assertEquals(1.5, result);

        result = typeHandler.createValue("123", Integer.class);
        assertTrue(result instanceof Integer);
        assertEquals(123, result);

        result = typeHandler.createValue("2021/01/01", Date.class);
        assertNull(result);

        result = typeHandler.createValue("java.lang.Object", Class.class);
        assertTrue(result instanceof Class);
        assertEquals(Object.class, result);

        result = typeHandler.createValue("test-file.txt", File.class);
        assertTrue(result instanceof File);
        assertEquals("test-file.txt", ((File) result).getPath());

        result = typeHandler.createValue("test-url.com", URL.class);
        assertTrue(result instanceof URL);
        try {
            assertEquals(new URL("test-url.com"), result);
        } catch (MalformedURLException e) {
            fail("MalformedURLException");
        }

        result = typeHandler.createValue("test", InvalidClass.class);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        Object result = typeHandler.createObject("java.lang.String");
        assertTrue(result instanceof String);

        result = typeHandler.createObject("java.lang.Object");
        assertTrue(result instanceof Object);

        result = typeHandler.createObject("InvalidClass");
        assertNull(result);
    }

    @Test
    public void testCreateNumber() {
        Number result = typeHandler.createNumber("1.5");
        assertTrue(result instanceof Double);
        assertEquals(1.5, result);

        result = typeHandler.createNumber("123");
        assertTrue(result instanceof Integer);
        assertEquals(123, result);

        result = typeHandler.createNumber("test");
        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        Class result = typeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);

        result = typeHandler.createClass("InvalidClass");
        assertNull(result);
    }

    @Test
    public void testCreateDate() {
        Date result = typeHandler.createDate("2021/01/01");
        assertNull(result);
    }

    @Test
    public void testCreateURL() {
        URL result = typeHandler.createURL("test-url.com");
        try {
            assertEquals(new URL("test-url.com"), result);
        } catch (MalformedURLException e) {
            fail("MalformedURLException");
        }

        result = typeHandler.createURL("InvalidURL");
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = typeHandler.createFile("test-file.txt");
        assertEquals("test-file.txt", result.getPath());
    }

    @Test
    public void testCreateFiles() {
        File[] result = typeHandler.createFiles("");
        assertNull(result);
    }
}

class InvalidClass {}
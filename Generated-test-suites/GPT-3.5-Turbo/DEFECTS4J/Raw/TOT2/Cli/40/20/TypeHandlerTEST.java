import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringAndClass() {
        try {
            Object obj = TypeHandler.createValue("testString", String.class);
            assertNotNull(obj);
            assertTrue(obj instanceof String);
            assertEquals("testString", obj);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertNotNull(obj);
            assertTrue(obj instanceof String);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateNumberWithLong() {
        try {
            Number num = TypeHandler.createNumber("100");
            assertNotNull(num);
            assertTrue(num instanceof Long);
            assertEquals(100L, num);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateNumberWithDouble() {
        try {
            Number num = TypeHandler.createNumber("100.25");
            assertNotNull(num);
            assertTrue(num instanceof Double);
            assertEquals(100.25, num);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertNotNull(clazz);
            assertEquals(String.class, clazz);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateURL() {
        try {
            String urlStr = "http://www.example.com";
            URL url = TypeHandler.createURL(urlStr);
            assertNotNull(url);
            assertEquals(urlStr, url.toString());
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testOpenFile() {
        try {
            FileInputStream inputStream = TypeHandler.openFile("test.txt");
            assertNotNull(inputStream);
        } catch (ParseException e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("path/to/files");
            fail("UnsupportedOperationException should have been thrown");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

}
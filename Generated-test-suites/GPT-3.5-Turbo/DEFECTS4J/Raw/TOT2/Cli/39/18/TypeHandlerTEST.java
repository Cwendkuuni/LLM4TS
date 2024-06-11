import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.ParseException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        try {
            Object obj = TypeHandler.createValue("testString", String.class);
            assertEquals("testString", obj);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateValueParseException() {
        try {
            TypeHandler.createValue("testString", Date.class);
            fail("Exception not thrown");
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateValueUnknownClass() {
        try {
            TypeHandler.createValue("TestClass", Class.class);
            fail("Exception not thrown");
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertEquals(String.class, obj.getClass());
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateObjectClassNotFoundException() {
        try {
            TypeHandler.createObject("InvalidClass");
            fail("Exception not thrown");
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateNumberDouble() {
        try {
            Number num = TypeHandler.createNumber("3.14");
            assertEquals(3.14, num);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateNumberLong() {
        try {
            Number num = TypeHandler.createNumber("100");
            assertEquals(100L, num);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateNumberNumberFormatException() {
        try {
            TypeHandler.createNumber("abc");
            fail("Exception not thrown");
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, clazz);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateClassClassNotFoundException() {
        try {
            TypeHandler.createClass("InvalidClass");
            fail("Exception not thrown");
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertEquals("http://www.example.com", url.toString());
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateURLMalformedURLException() {
        try {
            TypeHandler.createURL("invalidURL");
            fail("Exception not thrown");
        } catch (ParseException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] files = TypeHandler.createFiles("test.txt");
            assertEquals(1, files.length);
            assertEquals("test.txt", files[0].getName());
        } catch (UnsupportedOperationException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateDate() {
        try {
            TypeHandler.createDate("2021-10-01");
            fail("Exception not thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
}
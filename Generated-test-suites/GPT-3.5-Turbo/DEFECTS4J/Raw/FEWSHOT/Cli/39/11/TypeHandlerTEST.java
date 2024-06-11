import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        try {
            TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.NUMBER_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.DATE_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.CLASS_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.FILE_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.EXISTING_FILE_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.FILES_VALUE);
            TypeHandler.createValue("test", PatternOptionBuilder.URL_VALUE);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateValueInvalidPattern() {
        try {
            TypeHandler.createValue("test", "InvalidPattern");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected ParseException message", "Unable to find the class: InvalidPattern", e.getMessage());
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertEquals("Unexpected object type", String.class, obj.getClass());
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateObjectClassNotFoundException() {
        try {
            TypeHandler.createObject("InvalidClass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected ParseException message", "Unable to find the class: InvalidClass", e.getMessage());
        }
    }

    @Test
    public void testCreateObjectInstantiationException() {
        try {
            TypeHandler.createObject("java.lang.Integer");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected ParseException message", "java.lang.InstantiationException; Unable to create an instance of: java.lang.Integer", e.getMessage());
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number num1 = TypeHandler.createNumber("123");
            Number num2 = TypeHandler.createNumber("12.34");
            assertEquals("Unexpected number value", 123L, num1);
            assertEquals("Unexpected number value", 12.34, num2);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateNumberNumberFormatException() {
        try {
            TypeHandler.createNumber("test");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected ParseException message", "For input string: \"test\"", e.getMessage());
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertEquals("Unexpected class type", String.class, clazz);
        } catch (ParseException e) {
            fail("Unexpected ParseException");
        }
    }

    @Test
    public void testCreateClassClassNotFoundException() {
        try {
            TypeHandler.createClass("InvalidClass");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected ParseException message", "Unable to find the class: InvalidClass", e.getMessage());
        }
    }

    @Test
    public void testCreateDate() {
        try {
            Date date = TypeHandler.createDate("2022-01-01");
            assertNull("Unexpected date value", date);
        } catch (UnsupportedOperationException e) {
            assertEquals("Unexpected UnsupportedOperationException message", "Not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertEquals("Unexpected URL value", new URL("http://www.example.com"), url);
        } catch (ParseException | MalformedURLException e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    public void testCreateURLMalformedURLException() {
        try {
            TypeHandler.createURL("invalid-url");
            fail("Expected ParseException");
        } catch (ParseException e) {
            assertEquals("Unexpected ParseException message", "Unable to parse the URL: invalid-url", e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertEquals("Unexpected file value", new File("test.txt"), file);
    }

    @Test
    public void testCreateFiles() {
        try {
            TypeHandler.createFiles("file1,file2,file3");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Unexpected UnsupportedOperationException message", "Not yet implemented", e.getMessage());
        }
    }

}
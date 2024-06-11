import org.apache.commons.cli.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() throws ParseException {
        String str = "test";
        Object obj = new Object(); // any object
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringWrongClass() throws ParseException {
        String str = "test";
        Class<?> wrongClass = String[].class;
        TypeHandler.createValue(str, wrongClass);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringInvalidString() throws ParseException {
        String str = "123";
        Class<?> stringClass = String.class;
        try {
            TypeHandler.createValue(str, stringClass);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateValue_StringNumberInvalid() throws ParseException {
        String str = "test.123";
        Class<?> numberClass = Double.class;
        try {
            TypeHandler.createValue(str, numberClass);
            fail("Expected ParseException or NumberFormatException");
        } catch (ParseException e) {
            // expected
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateValue_StringUnknownClass() throws ParseException {
        String classname = "UnknownClass";
        Class<?> clazz = Class.forName(classname);
        try {
            TypeHandler.createValue("test", clazz);
            fail("Expected ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_NonEmptyConstructor() throws ParseException {
        String classname = "java.util.Date";
        try {
            TypeHandler.createObject(classname);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testCreateNumber_Long() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        String str = "3.14159265359";
        Number result = TypeHandler.createNumber(str);
        assertEquals(3.14159265359, result.doubleValue(), 0.0001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidLong() throws ParseException {
        String str = "test";
        try {
            TypeHandler.createNumber(str);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidDouble() throws ParseException {
        String str = "test.123";
        try {
            TypeHandler.createNumber(str);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURL() throws ParseException {
        String str = "test://localhost";
        try {
            TypeHandler.createURL(str);
            fail("Expected MalformedURLException");
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test
    public void testCreateFile_ExistingFile() throws ParseException {
        String path = "./testFile.txt";
        File file = TypeHandler.createFile(path);
        assertTrue(file.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_NonExistentFile() throws ParseException {
        String path = "./testFileThatDoesntExist.txt";
        try {
            TypeHandler.createFile(path);
            fail("Expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_NullString() throws ParseException {
        String str = null;
        try {
            TypeHandler.createValue(str, String.class);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testCreateObject_NullClassname() throws ParseException {
        String classname = null;
        try {
            TypeHandler.createObject(classname);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException {
        String str = "2023-03-15";
        Date result = TypeHandler.createDate(str);
        fail("Expected UnsupportedOperationException");
    }

    @Test
    public void testCreateDate_FormattedString() throws ParseException {
        String str = "2023-03-15";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expected = dateFormat.parse(str);
        Date result = TypeHandler.createDate(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateClass_KnownClass() throws ParseException {
        String classname = String.class.getName();
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_UnknownClass() throws ParseException {
        String classname = "UnknownClass";
        try {
            TypeHandler.createClass(classname);
            fail("Expected ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // expected
        }
    }

    @Test(expected = InstantiationException.class)
    public void testCreateFileInputStream_InvalidFile() throws ParseException, IOException {
        String path = "./testFileThatDoesntExist.txt";
        try {
            TypeHandler.createFileInputStream(path);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // expected
        }
    }

    @Test
    public void testCreateFileInputStream_ValidFile() throws ParseException, IOException {
        String path = "./testFile.txt";
        File file = new File(path);
        FileInputStream expected = new FileInputStream(file);
        FileInputStream result = TypeHandler.createFileInputStream(path);
        assertEquals(expected, result);
    }
}
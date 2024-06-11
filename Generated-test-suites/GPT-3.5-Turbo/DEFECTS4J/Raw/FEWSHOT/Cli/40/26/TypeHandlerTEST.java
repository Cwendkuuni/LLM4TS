import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_String_Object() {
        try {
            Object actual = TypeHandler.createValue("test", "String");
            assertEquals("test", actual);

            actual = TypeHandler.createValue("java.lang.Object", "Object");
            assertEquals(Object.class, actual);

            actual = TypeHandler.createValue("123", "Number");
            assertEquals(123, actual);

            actual = TypeHandler.createValue("01/01/2022", "Date");
            assertEquals(new Date(2022, 1, 1), actual);

            actual = TypeHandler.createValue("java.lang.String", "Class");
            assertEquals(String.class, actual);

            actual = TypeHandler.createValue("test.txt", "File");
            assertEquals(new File("test.txt"), actual);

            actual = TypeHandler.createValue("test.txt", "ExistingFile");
            try {
                FileInputStream fis = (FileInputStream) actual;
                File file = new File("test.txt");
                assertEquals(file.getAbsolutePath(), fis.getFD().toString());
            } catch (ClassCastException e) {
                fail("Returned object is not a FileInputStream");
            }

            actual = TypeHandler.createValue("test.txt,test2.txt", "Files");
            try {
                File[] files = (File[]) actual;
                assertEquals(new File("test.txt"), files[0]);
                assertEquals(new File("test2.txt"), files[1]);
            } catch (ClassCastException e) {
                fail("Returned object is not a File[]");
            }

            actual = TypeHandler.createValue("http://www.example.com", "URL");
            assertEquals(new URL("http://www.example.com"), actual);
        } catch (ParseException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValue_String_Class() {
        try {
            Object actual = TypeHandler.createValue("test", String.class);
            assertEquals("test", actual);

            actual = TypeHandler.createValue("java.lang.Object", Object.class);
            assertEquals(Object.class, actual);

            actual = TypeHandler.createValue("123", Number.class);
            assertEquals(123, actual);

            actual = TypeHandler.createValue("01/01/2022", Date.class);
            assertEquals(new Date(2022, 1, 1), actual);

            actual = TypeHandler.createValue("java.lang.String", Class.class);
            assertEquals(String.class, actual);

            actual = TypeHandler.createValue("test.txt", File.class);
            assertEquals(new File("test.txt"), actual);

            actual = TypeHandler.createValue("test.txt", FileInputStream.class);
            try {
                FileInputStream fis = (FileInputStream) actual;
                File file = new File("test.txt");
                assertEquals(file.getAbsolutePath(), fis.getFD().toString());
            } catch (ClassCastException e) {
                fail("Returned object is not a FileInputStream");
            }
            
            actual = TypeHandler.createValue("test.txt,test2.txt", File[].class);
            try {
                File[] files = (File[]) actual;
                assertEquals(new File("test.txt"), files[0]);
                assertEquals(new File("test2.txt"), files[1]);
            } catch (ClassCastException e) {
                fail("Returned object is not a File[]");
            }

            actual = TypeHandler.createValue("http://www.example.com", URL.class);
            assertEquals(new URL("http://www.example.com"), actual);
        } catch (ParseException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValue_Invalid() {
        Object actual = TypeHandler.createValue("invalid", Integer.class);
        assertNull(actual);
    }

    @Test
    public void testCreateObject() {
        try {
            Object actual = TypeHandler.createObject("java.lang.String");
            assertEquals(String.class, actual.getClass());

            actual = TypeHandler.createObject("java.lang.Object");
            assertEquals(Object.class, actual.getClass());

            actual = TypeHandler.createObject("test");
            fail("Expected ParseException to be thrown");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: test", e.getMessage());
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number actual = TypeHandler.createNumber("3.14");
            assertEquals(3.14, actual);

            actual = TypeHandler.createNumber("123");
            assertEquals(123, actual);

            actual = TypeHandler.createNumber("abc");
            fail("Expected ParseException to be thrown");
        } catch (ParseException e) {
            assertEquals("For input string: \"abc\"", e.getMessage());
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> actual = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, actual);

            actual = TypeHandler.createClass("java.lang.Object");
            assertEquals(Object.class, actual);

            actual = TypeHandler.createClass("test");
            fail("Expected ParseException to be thrown");
        } catch (ParseException e) {
            assertEquals("Unable to find the class: test", e.getMessage());
        }
    }

    @Test
    public void testCreateDate() {
        try {
            Date actual = TypeHandler.createDate("01/01/2022");
            assertEquals(new Date(2022, 1, 1), actual);
        } catch (UnsupportedOperationException e) {
            fail("Method not yet implemented");
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL actual = TypeHandler.createURL("http://www.example.com");
            assertEquals(new URL("http://www.example.com"), actual);
        } catch (ParseException e) {
            fail("Exception occurred: " + e.getMessage());
        } catch (MalformedURLException e) {
            fail("Malformed URL: " + e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        File actual = TypeHandler.createFile("test.txt");
        assertEquals(new File("test.txt"), actual);
    }

    @Test
    public void testOpenFile() {
        try {
            FileInputStream actual = TypeHandler.openFile("test.txt");
            File file = new File("test.txt");
            assertEquals(file.getAbsolutePath(), actual.getFD().toString());
        } catch (ParseException e) {
            fail("Exception occurred: " + e.getMessage());
        } catch (FileNotFoundException e) {
            fail("File not found: " + e.getMessage());
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] actual = TypeHandler.createFiles("test.txt,test2.txt");
            assertEquals(new File("test.txt"), actual[0]);
            assertEquals(new File("test2.txt"), actual[1]);
        } catch (UnsupportedOperationException e) {
            fail("Method not yet implemented");
        }
    }
}
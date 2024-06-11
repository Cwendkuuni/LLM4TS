import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObj1() {
        try {
            Object result = TypeHandler.createValue("test", "java.lang.String");
            assertEquals("test", result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_StringObj2() {
        try {
            Object result = TypeHandler.createValue("test", Object.class);
            assertEquals("test", result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_NumObj() {
        try {
            Object result = TypeHandler.createValue("3.14", Number.class);
            assertEquals(3.14, result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_DateObj() {
        try {
            Object result = TypeHandler.createValue("2021-01-01", Date.class);
            assertNull(result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_ClassObj() {
        try {
            Object result = TypeHandler.createValue("java.lang.String", Class.class);
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_FileObj() {
        try {
            Object result = TypeHandler.createValue("file.txt", File.class);
            assertEquals(new File("file.txt"), result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_ExistingFileObj() {
        try {
            Object result = TypeHandler.createValue("file.txt", FileInputStream.class);
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_FilesObj() {
        try {
            Object result = TypeHandler.createValue("file1.txt,file2.txt", File[].class);
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateValue_URLObj() {
        try {
            Object result = TypeHandler.createValue("http://www.example.com", URL.class);
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object result = TypeHandler.createObject("java.lang.String");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateNumber_Long() {
        try {
            Number result = TypeHandler.createNumber("123");
            assertEquals(123L, result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateNumber_Double() {
        try {
            Number result = TypeHandler.createNumber("3.14");
            assertEquals(3.14, result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> result = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-01-01");
    }

    @Test
    public void testCreateURL() {
        try {
            URL result = TypeHandler.createURL("http://www.example.com");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("file.txt");
        assertEquals(new File("file.txt"), result);
    }

    @Test
    public void testOpenFile() {
        try {
            FileInputStream result = TypeHandler.openFile("file.txt");
            assertNotNull(result);
        } catch (ParseException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] result = TypeHandler.createFiles("file1.txt,file2.txt");
            assertNotNull(result);
        } catch (UnsupportedOperationException e) {
            fail("Should not throw an exception");
        }
    }
}
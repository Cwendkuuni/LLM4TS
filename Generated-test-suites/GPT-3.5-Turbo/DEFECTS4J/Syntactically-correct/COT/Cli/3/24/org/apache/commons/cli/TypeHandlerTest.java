import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringOption() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithObjectOption() {
        Object result = TypeHandler.createValue("org.example.TestObject", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertEquals("org.example.TestObject", result.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumberOption() {
        Object result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(result);
        assertEquals(123, result);
    }

    @Test
    public void testCreateValueWithDateOption() {
        Object result = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(result); // Date parsing is not implemented
    }

    @Test
    public void testCreateValueWithClassOption() {
        Object result = TypeHandler.createValue("org.example.TestClass", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(result);
        assertEquals("org.example.TestClass", result.getClass().getName());
    }

    @Test
    public void testCreateValueWithFileOption() {
        Object result = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
        assertEquals("path/to/file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithExistingFileOption() {
        Object result = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File);
        assertEquals("path/to/file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithFilesOption() {
        Object result = TypeHandler.createValue("path/to/file1.txt,path/to/file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        assertEquals(2, files.length);
        assertEquals("path/to/file1.txt", files[0].getPath());
        assertEquals("path/to/file2.txt", files[1].getPath());
    }

    @Test
    public void testCreateValueWithURLOption() {
        Object result = TypeHandler.createValue("http://example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
        assertTrue(result instanceof URL);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testCreateValueWithUnknownOption() {
        Object result = TypeHandler.createValue("unknown", PatternOptionBuilder.UNKNOWN_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("org.example.TestObject");
        assertNotNull(result);
        assertEquals("org.example.TestObject", result.getClass().getName());
    }

    @Test
    public void testCreateNumber() {
        Number result = TypeHandler.createNumber("123");
        assertNotNull(result);
        assertEquals(123, result);
    }

    @Test
    public void testCreateNumberWithInvalidInput() {
        Number result = TypeHandler.createNumber("abc");
        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("org.example.TestClass");
        assertNotNull(result);
        assertEquals("org.example.TestClass", result.getName());
    }

    @Test
    public void testCreateClassWithInvalidInput() {
        Class result = TypeHandler.createClass("org.example.UnknownClass");
        assertNull(result);
    }

    @Test
    public void testCreateDate() {
        Date result = TypeHandler.createDate("2021-01-01");
        assertNull(result); // Date parsing is not implemented
    }

    @Test
    public void testCreateDateWithInvalidInput() {
        Date result = TypeHandler.createDate("abc");
        assertNull(result);
    }

    @Test
    public void testCreateURL() {
        URL result = TypeHandler.createURL("http://example.com");
        assertNotNull(result);
        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testCreateURLWithInvalidInput() {
        URL result = TypeHandler.createURL("invalid");
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("path/to/file.txt");
        assertNotNull(result);
        assertEquals("path/to/file.txt", result.getPath());
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("path/to/file1.txt,path/to/file2.txt");
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("path/to/file1.txt", result[0].getPath());
        assertEquals("path/to/file2.txt", result[1].getPath());
    }

    @Test
    public void testCreateFilesWithInvalidInput() {
        File[] result = TypeHandler.createFiles("invalid");
        assertNull(result);
    }
}


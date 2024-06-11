import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringArgument() {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithObjectArgument() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumberArgument() {
        Object result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(10, result);
    }

    @Test
    public void testCreateValueWithDateArgument() {
        Object result = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValueWithClassArgument() {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals("java.lang.String", result.getClass().getName());
    }

    @Test
    public void testCreateValueWithFileArgument() {
        Object result = TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals("path/to/file.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithExistingFileArgument() {
        Object result = TypeHandler.createValue("path/to/existingFile.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals("path/to/existingFile.txt", ((File) result).getPath());
    }

    @Test
    public void testCreateValueWithFilesArgument() {
        Object result = TypeHandler.createValue("path/to/file1.txt,path/to/file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValueWithURLArgument() {
        Object result = TypeHandler.createValue("https://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
    }

    @Test
    public void testCreateValueWithInvalidArgument() {
        Object result = TypeHandler.createValue("invalid", "InvalidArgument");
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }

    @Test
    public void testCreateNumberWithDouble() {
        Number result = TypeHandler.createNumber("10.5");
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateNumberWithLong() {
        Number result = TypeHandler.createNumber("10");
        assertEquals(10L, result);
    }

    @Test
    public void testCreateNumberWithInvalidNumber() {
        Number result = TypeHandler.createNumber("invalid");
        assertNull(result);
    }

    @Test
    public void testCreateClass() {
        Class result = TypeHandler.createClass("java.lang.String");
        assertEquals("java.lang.String", result.getName());
    }

    @Test
    public void testCreateClassWithInvalidClass() {
        Class result = TypeHandler.createClass("InvalidClass");
        assertNull(result);
    }

    @Test
    public void testCreateDateWithValidDate() {
        Date result = TypeHandler.createDate("2021-01-01");
        assertNotNull(result);
    }

    @Test
    public void testCreateDateWithInvalidDate() {
        Date result = TypeHandler.createDate("InvalidDate");
        assertNull(result);
    }

    @Test
    public void testCreateURLWithValidURL() {
        URL result = TypeHandler.createURL("https://www.example.com");
        assertNotNull(result);
    }

    @Test
    public void testCreateURLWithInvalidURL() {
        URL result = TypeHandler.createURL("InvalidURL");
        assertNull(result);
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("path/to/file.txt");
        assertEquals("path/to/file.txt", result.getPath());
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("path/to/file1.txt,path/to/file2.txt");
        assertNull(result);
    }
}
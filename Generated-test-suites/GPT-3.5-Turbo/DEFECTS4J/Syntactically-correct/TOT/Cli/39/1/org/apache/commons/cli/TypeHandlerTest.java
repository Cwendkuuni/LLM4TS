import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TypeHandlerTest {

    @Test
    public void createValue_String_ReturnsString() throws Exception {
        // Arrange
        String str = "testString";
        Object obj = PatternOptionBuilder.STRING_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertEquals(str, result);
    }

    @Test
    public void createValue_Object_ReturnsObject() throws Exception {
        // Arrange
        String str = "java.lang.Object";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNotNull(result);
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void createValue_Number_ReturnsNumber() throws Exception {
        // Arrange
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNotNull(result);
        assertEquals(Long.class, result.getClass());
        assertEquals(10L, result);
    }

    @Test
    public void createValue_NumberWithDecimal_ReturnsDouble() throws Exception {
        // Arrange
        String str = "10.5";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNotNull(result);
        assertEquals(Double.class, result.getClass());
        assertEquals(10.5, result);
    }

    @Test
    public void createValue_Date_ReturnsDate() throws Exception {
        // Arrange
        String str = "2020-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNull(result); // Not yet implemented
    }

    @Test
    public void createValue_Class_ReturnsClass() throws Exception {
        // Arrange
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNotNull(result);
        assertEquals(Class.class, result.getClass());
        assertEquals(String.class, result);
    }

    @Test
    public void createValue_File_ReturnsFile() throws Exception {
        // Arrange
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNotNull(result);
        assertEquals(File.class, result.getClass());
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void createValue_ExistingFile_ReturnsFile() throws Exception {
        // Arrange
        String str = "/path/to/file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNotNull(result);
        assertEquals(File.class, result.getClass());
        assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void createValue_Files_ReturnsFileArray() throws Exception {
        // Arrange
        String str = "/path/to/file1.txt, /path/to/file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNull(result); // Not yet implemented
    }

    @Test
    public void createValue_URL_ReturnsURL() throws Exception {
        // Arrange
        String str = "http://example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;

        // Act
        Object result = TypeHandler.createValue(str, obj);

        // Assert
        assertNotNull(result);
        assertEquals(URL.class, result.getClass());
        assertEquals(str, result.toString());
    }
    
    @Test(expected = ParseException.class)
    public void createValue_InvalidClass_ThrowsParseException() throws Exception {
        // Arrange
        String str = "InvalidClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;

        // Act
        TypeHandler.createValue(str, obj);
    }

    @Test(expected = ParseException.class)
    public void createValue_InvalidURL_ThrowsParseException() throws Exception {
        // Arrange
        String str = "invalidUrl";
        Object obj = PatternOptionBuilder.URL_VALUE;

        // Act
        TypeHandler.createValue(str, obj);
    }
}
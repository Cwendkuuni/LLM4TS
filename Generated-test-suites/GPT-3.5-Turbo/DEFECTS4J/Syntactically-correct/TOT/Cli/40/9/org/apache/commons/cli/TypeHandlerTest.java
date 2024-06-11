import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;

/**
 * JUnit test case for TypeHandler class.
 */
@RunWith(JUnit4.class)
public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringArgument_ReturnsInstanceOfString() throws ParseException {
        // Arrange
        String value = "test";

        // Act
        Object result = TypeHandler.createValue(value, String.class);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result);
    }

    @Test
    public void testCreateValue_ObjectArgument_ReturnsCreatedObject() throws ParseException {
        // Arrange
        String value = "java.lang.String";

        // Act
        Object result = TypeHandler.createValue(value, Object.class);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateValue_NumberArgument_ReturnsCreatedNumber() throws ParseException {
        // Arrange
        String value = "123";

        // Act
        Object result = TypeHandler.createValue(value, Number.class);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(Long.class, result.getClass());
        Assert.assertEquals(123L, result);
    }

    @Test
    public void testCreateValue_DateArgument_ReturnsNull() throws ParseException {
        // Arrange
        String value = "2021-01-01";

        // Act
        Object result = TypeHandler.createValue(value, Date.class);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValue_ClassArgument_ReturnsCreatedClass() throws ParseException {
        // Arrange
        String value = "java.lang.String";

        // Act
        Object result = TypeHandler.createValue(value, Class.class);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_FileArgument_ReturnsCreatedFile() throws ParseException {
        // Arrange
        String value = "test.txt";

        // Act
        Object result = TypeHandler.createValue(value, File.class);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(File.class, result.getClass());
        Assert.assertEquals("test.txt", ((File)result).getName());
    }

    @Test
    public void testCreateValue_ExistingFileArgument_ReturnsOpenedFileInputStream() throws ParseException {
        // Arrange
        String value = "test.txt";

        // Act
        Object result = TypeHandler.createValue(value, FileInputStream.class);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(FileInputStream.class, result.getClass());
    }

    @Test
    public void testCreateValue_FilesArgument_ReturnsFileArray() throws ParseException {
        // Arrange
        String value = "file1.txt, file2.txt";

        // Act
        Object result = TypeHandler.createValue(value, File[].class);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(File[].class, result.getClass());
        File[] files = (File[]) result;
        Assert.assertEquals(2, files.length);
        Assert.assertEquals("file1.txt", files[0].getName());
        Assert.assertEquals("file2.txt", files[1].getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_UnsupportedClass_ThrowsParseException() throws ParseException {
        // Arrange
        String value = "unsupported";

        // Act
        TypeHandler.createValue(value, UnsupportedClass.class);

        // Assert (expected exception)
    }
}
import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_withStringArgument_shouldReturnStringObject() {
        String str = "test";
        Object result = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_withObjectArgument_shouldReturnObjectInstance() {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, Object.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValue_withNumberArgument_shouldReturnNumberObject() {
        String str = "10";
        Object result = TypeHandler.createValue(str, Number.class);
        Assert.assertEquals(10, result);
    }

    @Test
    public void testCreateValue_withDateArgument_shouldReturnDateObject() {
        String str = "2021-01-01";
        Object result = TypeHandler.createValue(str, Date.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValue_withClassArgument_shouldReturnClassObject() {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, Class.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, ((Class) result).getName());
    }

    @Test
    public void testCreateValue_withFileArgument_shouldReturnFileObject() {
        String str = "/path/to/file.txt";
        Object result = TypeHandler.createValue(str, File.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_withExistingFileArgument_shouldReturnFileObject() {
        String str = "/path/to/existingFile.txt";
        Object result = TypeHandler.createValue(str, File.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, ((File) result).getPath());
    }

    @Test
    public void testCreateValue_withFilesArgument_shouldReturnFileArray() {
        String str = "/path/to/file1.txt;/path/to/file2.txt;/path/to/file3.txt";
        Object result = TypeHandler.createValue(str, File[].class);
        Assert.assertNotNull(result);
        File[] files = (File[]) result;
        Assert.assertEquals(3, files.length);
        Assert.assertEquals("/path/to/file1.txt", files[0].getPath());
        Assert.assertEquals("/path/to/file2.txt", files[1].getPath());
        Assert.assertEquals("/path/to/file3.txt", files[2].getPath());
    }

    @Test
    public void testCreateValue_withURLArgument_shouldReturnURLObject() {
        String str = "https://www.example.com";
        Object result = TypeHandler.createValue(str, URL.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, ((URL) result).toString());
    }

    @Test
    public void testCreateValue_withInvalidArgument_shouldReturnNull() {
        String str = "invalid";
        Object result = TypeHandler.createValue(str, UnknownClass.class);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject_withValidClassName_shouldReturnObjectInstance() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        Assert.assertNotNull(result);
        Assert.assertEquals(className, result.getClass().getName());
    }

    @Test
    public void testCreateObject_withInvalidClassName_shouldReturnNull() {
        String className = "unknown.Class";
        Object result = TypeHandler.createObject(className);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateNumber_withValidNumber_shouldReturnNumberObject() {
        String numberStr = "10";
        Number result = TypeHandler.createNumber(numberStr);
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result);
    }

    @Test
    public void testCreateNumber_withInvalidNumber_shouldReturnNull() {
        String invalidNumberStr = "invalid";
        Number result = TypeHandler.createNumber(invalidNumberStr);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateClass_withValidClassName_shouldReturnClassObject() {
        String className = "java.lang.String";
        Class result = TypeHandler.createClass(className);
        Assert.assertNotNull(result);
        Assert.assertEquals(className, result.getName());
    }

    @Test
    public void testCreateClass_withInvalidClassName_shouldReturnNull() {
        String className = "unknown.Class";
        Class result = TypeHandler.createClass(className);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateDate_withValidDateString_shouldReturnDateObject() {
        String dateString = "2021-01-01";
        Date result = TypeHandler.createDate(dateString);
        Assert.assertNotNull(result);
        Assert.assertEquals(dateString, result.toString());
    }

    @Test
    public void testCreateDate_withInvalidDateString_shouldReturnNull() {
        String invalidDateString = "invalid";
        Date result = TypeHandler.createDate(invalidDateString);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL_withValidURLString_shouldReturnURLObject() {
        String urlString = "https://www.example.com";
        URL result = TypeHandler.createURL(urlString);
        Assert.assertNotNull(result);
        Assert.assertEquals(urlString, result.toString());
    }

    @Test
    public void testCreateURL_withInvalidURLString_shouldReturnNull() {
        String invalidURLString = "invalid";
        URL result = TypeHandler.createURL(invalidURLString);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateFile_withValidFilePath_shouldReturnFileObject() {
        String filePath = "/path/to/file.txt";
        File result = TypeHandler.createFile(filePath);
        Assert.assertNotNull(result);
        Assert.assertEquals(filePath, result.getPath());
    }

    @Test
    public void testCreateFiles_withValidFilePaths_shouldReturnFileArray() {
        String filePaths = "/path/to/file1.txt;/path/to/file2.txt;/path/to/file3.txt";
        File[] result = TypeHandler.createFiles(filePaths);
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.length);
        Assert.assertEquals("/path/to/file1.txt", result[0].getPath());
        Assert.assertEquals("/path/to/file2.txt", result[1].getPath());
        Assert.assertEquals("/path/to/file3.txt", result[2].getPath());
    }
}
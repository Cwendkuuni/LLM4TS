import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertSame(obj, result);
    }

    @Test
    public void testCreateValue_StringString() {
        String str = "test";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_InvalidClass() {
        String str = "test";
        Class clazz = int.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject_ValidClassName() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertInstanceOf(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_InvalidClassName() {
        String className = "java.DoesNotExist";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber_ValidNumber() {
        String numberString = "123";
        Number result = TypeHandler.createNumber(numberString);
        assertEquals(123, result.intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidNumber() {
        String numberString = "abc";
        TypeHandler.createNumber(numberString);
    }

    @Test
    public void testCreateClass_ValidClassName() {
        String className = "java.lang.String";
        Class result = TypeHandler.createClass(className);
        assertEquals(String.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_InvalidClassName() {
        String className = "java.DoesNotExist";
        TypeHandler.createClass(className);
    }

    @Test
    public void testCreateDate_ValidDateString() {
        String dateString = "2023-03-23";
        Date result = TypeHandler.createDate(dateString);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidDateString() {
        String dateString = "2023-3-23";
        TypeHandler.createDate(dateString);
    }

    @Test
    public void testCreateURL_ValidURLString() {
        String urlString = "http://www.example.com";
        URL result = TypeHandler.createURL(urlString);
        assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURLString() {
        String urlString = "http://www.example.com/doesnotexist";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile_ValidFilePath() {
        String filePath = "/path/to/file.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_InvalidFilePath() {
        String filePath = "/path/to/non-existent-file.txt";
        TypeHandler.createFile(filePath);
    }

    @Test
    public void testCreateFiles_ValidFilePaths() {
        String filePaths = "/path/to/file1.txt, /path/to/file2.txt";
        File[] result = TypeHandler.createFiles(filePaths);
        assertNotNull(result);
        assertEquals(2, result.length);
    }
}
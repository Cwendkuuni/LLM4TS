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
        Object result = TypeHandler.createValue(str, String.class);
        assertEquals(str, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullString() {
        Object obj = new Object();
        TypeHandler.createValue(null, obj.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullClass() {
        String str = "test";
        TypeHandler.createValue(str, null);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateValue_IncorrectClass() {
        String str = "test";
        TypeHandler.createValue(str, Integer.class);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_NonExistentClass() {
        String className = "NonExistentClass";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateObject_ExistentClass() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber_ValidNumber() {
        String numberString = "123";
        Number numberResult = TypeHandler.createNumber(numberString);
        assertEquals(123, numberResult.intValue());
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        String numberString = "abc";
        Number numberResult = TypeHandler.createNumber(numberString);
        assertNull(numberResult);
    }

    @Test
    public void testCreateDate_ValidDate() {
        String dateString = "2022-12-31";
        Date dateResult = TypeHandler.createDate(dateString);
        assertNotNull(dateResult);
    }

    @Test
    public void testCreateDate_InvalidDate() {
        String dateString = "2022-12-32";
        Date dateResult = TypeHandler.createDate(dateString);
        assertNull(dateResult);
    }

    @Test
    public void testCreateURL_ValidURL() {
        String urlString = "http://example.com";
        URL urlResult = TypeHandler.createURL(urlString);
        assertNotNull(urlResult);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidURL() {
        String urlString = "http://example.com/invalid";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile_ValidFile() {
        String filePath = "test.txt";
        File fileResult = TypeHandler.createFile(filePath);
        assertNotNull(fileResult);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFile_NullFilePath() {
        TypeHandler.createFile(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_NonExistentFile() {
        String filePath = "non-existent-file.txt";
        TypeHandler.createFile(filePath);
    }
}
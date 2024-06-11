import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithStringString() {
        String str = "test";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithInvalidStringAndObject() {
        String str = "invalid";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertNull(result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectWithInvalidClassName() {
        String className = "InvalidClass";
        Object result = TypeHandler.createObject(className);
        assertNull(result);
    }

    @Test
    public void testCreateObjectWithValidClassName() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
    }

    @Test
    public void testCreateNumberWithValidNumber() {
        String numberString = "123";
        Number result = TypeHandler.createNumber(numberString);
        assertNotNull(result);
        assertEquals(123, result.intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberWithInvalidNumber() {
        String numberString = "invalid";
        Number result = TypeHandler.createNumber(numberString);
        assertNull(result);
    }

    @Test
    public void testCreateClassWithValidClassName() {
        String className = "java.lang.String";
        Class result = TypeHandler.createClass(className);
        assertNotNull(result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClassWithInvalidClassName() {
        String className = "InvalidClass";
        Class result = TypeHandler.createClass(className);
        assertNull(result);
    }

    @Test
    public void testCreateDateWithValidDateString() {
        String dateString = "2022-03-23";
        Date result = TypeHandler.createDate(dateString);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateWithInvalidDateString() {
        String dateString = "invalid";
        Date result = TypeHandler.createDate(dateString);
        assertNull(result);
    }

    @Test
    public void testCreateURLWithValidURLString() {
        String urlString = "http://example.com";
        URL result = TypeHandler.createURL(urlString);
        assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLWithInvalidURLString() {
        String urlString = "invalid";
        URL result = TypeHandler.createURL(urlString);
        assertNull(result);
    }

    @Test
    public void testCreateFileWithValidFileString() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileWithInvalidFileString() {
        String filePath = "invalid";
        File result = TypeHandler.createFile(filePath);
        assertNull(result);
    }

    @Test
    public void testCreateFilesWithValidFilePaths() {
        String filePaths = "test1.txt, test2.txt";
        File[] result = TypeHandler.createFiles(filePaths);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCreateFilesWithEmptyString() {
        String filePaths = "";
        File[] result = TypeHandler.createFiles(filePaths);
        assertNull(result);
    }
}
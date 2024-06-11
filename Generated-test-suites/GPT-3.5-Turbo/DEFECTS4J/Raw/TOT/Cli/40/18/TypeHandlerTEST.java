import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void createValue_String_ValidValue_ReturnsStringObject() throws ParseException {
        String value = "test";
        Object result = TypeHandler.createValue(value, String.class);
        assertEquals(value, result);
    }

    @Test
    public void createValue_String_Null_ReturnsNull() throws ParseException {
        Object result = TypeHandler.createValue(null, String.class);
        assertNull(result);
    }

    @Test
    public void createValue_Object_ValidValue_ReturnsObject() throws ParseException {
        String value = "java.lang.Object";
        Object result = TypeHandler.createValue(value, Object.class);
        assertEquals(Object.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void createValue_Object_InvalidValue_ThrowsParseException() throws ParseException {
        String value = "invalidClass";
        TypeHandler.createValue(value, Object.class);
    }

    @Test
    public void createValue_Number_IntegerValue_ReturnsLongObject() throws ParseException {
        String value = "100";
        Number result = TypeHandler.createValue(value, Number.class);
        assertEquals(Long.class, result.getClass());
    }

    @Test
    public void createValue_Number_DecimalValue_ReturnsDoubleObject() throws ParseException {
        String value = "10.5";
        Number result = TypeHandler.createValue(value, Number.class);
        assertEquals(Double.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void createValue_Number_InvalidValue_ThrowsParseException() throws ParseException {
        String value = "invalidNumber";
        TypeHandler.createValue(value, Number.class);
    }

    @Test
    public void createValue_Date_ValidValue_ReturnsDateObject() throws ParseException {
        String value = "2022-10-25";
        Date result = TypeHandler.createValue(value, Date.class);
        assertNotNull(result);
    }

    @Test
    public void createValue_Class_ValidValue_ReturnsClassObject() throws ParseException {
        String value = "java.lang.String";
        Class<?> result = TypeHandler.createValue(value, Class.class);
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void createValue_Class_InvalidValue_ThrowsParseException() throws ParseException {
        String value = "invalidClass";
        TypeHandler.createValue(value, Class.class);
    }

    @Test
    public void createValue_File_ValidValue_ReturnsFileObject() throws ParseException {
        String value = "test.txt";
        File result = TypeHandler.createValue(value, File.class);
        assertEquals(value, result.getName());
    }

    @Test
    public void createValue_URL_ValidValue_ReturnsURLObject() throws ParseException, MalformedURLException {
        String value = "https://www.example.com";
        URL result = TypeHandler.createValue(value, URL.class);
        assertEquals(value, result.toString());
    }

    @Test(expected = ParseException.class)
    public void createValue_URL_InvalidValue_ThrowsParseException() throws ParseException {
        String value = "invalidURL";
        TypeHandler.createValue(value, URL.class);
    }

    @Test
    public void createObject_ValidClassName_ReturnsObject() throws ParseException {
        String className = "java.lang.Object";
        Object result = TypeHandler.createObject(className);
        assertEquals(Object.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void createObject_InvalidClassName_ThrowsParseException() throws ParseException {
        String className = "invalidClass";
        TypeHandler.createObject(className);
    }

    @Test
    public void createNumber_IntegerValue_ReturnsLongObject() throws ParseException {
        String value = "100";
        Number result = TypeHandler.createNumber(value);
        assertEquals(Long.class, result.getClass());
    }

    @Test
    public void createNumber_DecimalValue_ReturnsDoubleObject() throws ParseException {
        String value = "10.5";
        Number result = TypeHandler.createNumber(value);
        assertEquals(Double.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void createNumber_InvalidValue_ThrowsParseException() throws ParseException {
        String value = "invalidNumber";
        TypeHandler.createNumber(value);
    }

    @Test
    public void createClass_ValidClassName_ReturnsClassObject() throws ParseException {
        String className = "java.lang.Object";
        Class<?> result = TypeHandler.createClass(className);
        assertEquals(Object.class, result);
    }

    @Test(expected = ParseException.class)
    public void createClass_InvalidClassName_ThrowsParseException() throws ParseException {
        String className = "invalidClass";
        TypeHandler.createClass(className);
    }

    @Test
    public void createDate_ValidDateString_ReturnsDateObject() {
        String dateString = "2022-10-25";
        Date result = TypeHandler.createDate(dateString);
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createDate_InvalidDateString_ThrowsUnsupportedOperationException() {
        String dateString = "invalidDate";
        TypeHandler.createDate(dateString);
    }

    @Test
    public void createURL_ValidURL_ReturnsURLObject() throws ParseException, MalformedURLException {
        String urlStr = "https://www.example.com";
        URL result = TypeHandler.createURL(urlStr);
        assertEquals(urlStr, result.toString());
    }

    @Test(expected = ParseException.class)
    public void createURL_InvalidURL_ThrowsParseException() throws ParseException {
        String urlStr = "invalidURL";
        TypeHandler.createURL(urlStr);
    }

    @Test
    public void createFile_ValidFilePath_ReturnsFileObject() {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        assertEquals(filePath, result.getName());
    }

    @Test
    public void openFile_ExistingFile_ReturnsFileInputStream() throws ParseException {
        String filePath = "test.txt";
        FileInputStream result = TypeHandler.openFile(filePath);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void openFile_NonExistingFile_ThrowsParseException() throws ParseException {
        String filePath = "nonExisting.txt";
        TypeHandler.openFile(filePath);
    }

    @Test
    public void createFiles_ValidPaths_ReturnsFileArray() {
        String paths = "test1.txt,test2.txt,test3.txt";
        File[] result = TypeHandler.createFiles(paths);
        assertEquals(3, result.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createFiles_InvalidPaths_ThrowsUnsupportedOperationException() {
        String paths = "invalidPaths";
        TypeHandler.createFiles(paths);
    }

    
}
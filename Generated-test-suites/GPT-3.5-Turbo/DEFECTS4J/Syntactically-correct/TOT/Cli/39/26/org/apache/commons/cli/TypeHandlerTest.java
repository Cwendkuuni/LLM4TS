import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValue_String_Object_Success() throws ParseException {
        String str = "test";
        Object obj = String.class;
        Object value = typeHandler.createValue(str, obj);
        assertEquals(str, value);
    }

    @Test
    public void testCreateValue_Class_Success() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        Object value = typeHandler.createValue(str, clazz);
        assertEquals(str, value);
    }

    @Test
    public void testCreateValue_Object() throws ParseException {
        String str = "test";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNull(value);
    }

    @Test
    public void testCreateValue_Number_Long_Success() throws ParseException {
        String str = "123";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123L, value);
    }

    @Test
    public void testCreateValue_Number_Double_Success() throws ParseException {
        String str = "123.45";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123.45, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_Number_Failure() throws ParseException {
        String str = "abc";
        typeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_Date_NotImplemented() throws ParseException {
        String str = "2020-01-01";
        typeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }

    @Test
    public void testCreateValue_Class_Success() throws ParseException {
        String str = "java.lang.String";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, value);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_Class_NotFound() throws ParseException {
        String str = "com.example.Test";
        typeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test
    public void testCreateValue_File_Success() throws ParseException {
        String str = "path/to/file.txt";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertEquals(new File(str), value);
    }

    @Test
    public void testCreateValue_ExistingFile_Success() throws ParseException {
        String str = "path/to/existingfile.txt";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(new File(str), value);
    }

    @Test
    public void testCreateValue_Files_NotImplemented() throws ParseException {
        String str = "path/to/files";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertNull(value);
    }

    @Test
    public void testCreateValue_URL_Success() throws ParseException, MalformedURLException {
        String str = "https://www.example.com";
        Object value = typeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertEquals(new URL(str), value);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_URL_Invalid() throws ParseException, MalformedURLException {
        String str = "invalidurl";
        typeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
    }

    @Test
    public void testCreateObject_Success() throws ParseException {
        String classname = "java.lang.String";
        Object value = typeHandler.createObject(classname);
        assertTrue(value instanceof String);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFound() throws ParseException {
        String classname = "com.example.Test";
        typeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_Long_Success() throws ParseException {
        String str = "123";
        Number number = typeHandler.createNumber(str);
        assertEquals(123L, number);
    }

    @Test
    public void testCreateNumber_Double_Success() throws ParseException {
        String str = "123.45";
        Number number = typeHandler.createNumber(str);
        assertEquals(123.45, number);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_Failure() throws ParseException {
        String str = "abc";
        typeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_Success() throws ParseException {
        String classname = "java.lang.String";
        Class<?> clazz = typeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_NotFound() throws ParseException {
        String classname = "com.example.Test";
        typeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_NotImplemented() {
        String str = "2020-01-01";
        typeHandler.createDate(str);
    }

    @Test
    public void testCreateURL_Success() throws ParseException, MalformedURLException {
        String str = "https://www.example.com";
        URL url = typeHandler.createURL(str);
        assertEquals(new URL(str), url);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_Invalid() throws ParseException, MalformedURLException {
        String str = "invalidurl";
        typeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_Success() {
        String str = "path/to/file.txt";
        File file = typeHandler.createFile(str);
        assertEquals(new File(str), file);
    }

    @Test
    public void testCreateFiles_NotImplemented() {
        String str = "path/to/files";
        File[] files = typeHandler.createFiles(str);
        assertNull(files);
    }

}
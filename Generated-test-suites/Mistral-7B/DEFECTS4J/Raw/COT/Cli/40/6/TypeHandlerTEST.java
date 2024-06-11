import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeHandlerTest
{

    @Test
    public void testCreateValue_String_String() throws ParseException {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals("test", obj);
    }

    @Test
    public void testCreateValue_String_Class_String() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals("test", obj);
    }

    @Test
    public void testCreateValue_String_Class_InvalidClass() throws ParseException {
        String str = "test";
        Class<?> clazz = Integer.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertNull(obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_InvalidClass_ParseException() throws ParseException {
        String str = "test";
        Class<?> clazz = File.class;
        Object obj = TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject_ValidClassname() throws ParseException {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createObject(classname);
        Assert.assertNotNull(obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassname() throws ParseException {
        String classname = "java.lang.InvalidClass";
        Object obj = TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_ValidNumberString() throws ParseException {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123, number.intValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumberString() throws ParseException {
        String str = "123.45";
        Number number = TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateDate_ValidDateString() throws ParseException {
        // Not yet implemented, uncomment when implemented
//        String str = "2022-12-31";
//        Date date = TypeHandler.createDate(str);
//        assertNotNull(date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_InvalidDateString() throws ParseException {
        String str = "test";
        Date date = TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL_ValidURLString() throws ParseException {
        String str = "https://www.google.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("https://www.google.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURLString() throws ParseException {
        String str = "http://www.google.com/test";
        URL url = TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_ValidFileString() {
        String str = "test.txt";
        File file = TypeHandler.createFile(str);
        assertTrue(file.exists());
        file.delete();
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidFileString() {
        String str = "/test.txt";
        File file = TypeHandler.createFile(str);
    }

    @Test
    public void testOpenFile_ValidFileString() throws IOException, ParseException {
        String str = "test.txt";
        File file = new File(str);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileInputStream inputStream = TypeHandler.openFile(str);
        inputStream.close();
        file.delete();
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_InvalidFileString() throws IOException, ParseException {
        String str = "/test.txt";
        FileInputStream inputStream = TypeHandler.openFile(str);
    }
}
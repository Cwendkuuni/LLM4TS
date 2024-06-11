import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObjectArgument() throws ParseException {
        Object obj = "test";
        assertEquals("test", TypeHandler.createValue("test", obj));
    }

    @Test
    public void testCreateValueWithClassArgument() throws ParseException {
        String str = "test";
        assertEquals("test", TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        assertNotNull(TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateNumberWithLongValue() throws ParseException {
        String value = "123";
        assertEquals(Long.valueOf(value), TypeHandler.createNumber(value));
    }

    @Test
    public void testCreateNumberWithDoubleValue() throws ParseException {
        String value = "123.45";
        assertEquals(Double.valueOf(value), TypeHandler.createNumber(value));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidValue() throws ParseException {
        String value = "abc";
        TypeHandler.createNumber(value);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String className = "java.lang.String";
        assertEquals(String.class, TypeHandler.createClass(className));
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassName() throws ParseException {
        String className = "InvalidClass";
        TypeHandler.createClass(className);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String dateStr = "2021-10-01";
        TypeHandler.createDate(dateStr);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String urlStr = "https://www.example.com";
        URL url = TypeHandler.createURL(urlStr);
        assertEquals(urlStr, url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String urlStr = "invalidurl";
        TypeHandler.createURL(urlStr);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File file = TypeHandler.createFile(filePath);
        assertEquals(filePath, file.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String filePath = "test.txt";
        assertNotNull(TypeHandler.openFile(filePath));
    }

    @Test(expected = ParseException.class)
    public void testOpenFileNotFound() throws ParseException {
        String filePath = "invalid_file.txt";
        TypeHandler.openFile(filePath);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String fileArray = "file1,file2,file3";
        TypeHandler.createFiles(fileArray);
    }
}
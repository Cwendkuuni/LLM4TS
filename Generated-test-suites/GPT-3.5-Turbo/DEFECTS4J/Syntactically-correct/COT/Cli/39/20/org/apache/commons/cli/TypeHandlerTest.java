import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    private TypeHandler typeHandler;

    @Before
    public void setUp() {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValue_StringClass() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = typeHandler.createValue(str, clazz);
        assert result instanceof String;
        assert result.equals(str);
    }

    @Test
    public void testCreateValue_StringObject() throws ParseException {
        String str = "test";
        Object obj = String.class;
        Object result = typeHandler.createValue(str, obj);
        assert result instanceof String;
        assert result.equals(str);
    }

    @Test
    public void testCreateValue_Object() throws ParseException {
        String str = "java.lang.String";
        Object result = typeHandler.createValue(str);
        assert result instanceof String;
        assert result.getClass().getName().equals(str);
    }

    @Test
    public void testCreateValue_Number() throws ParseException {
        String str = "1";
        Object result = typeHandler.createValue(str);
        assert result instanceof Number;
        assert result.equals(1);
    }

    @Test
    public void testCreateValue_Date() {
        String str = "2021-01-01";
        Object result = typeHandler.createValue(str);
        assert result instanceof Date;
        assert result.toString().equals("2021-01-01");
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_Date_Exception() throws ParseException {
        String str = "not_a_date";
        typeHandler.createValue(str);
    }

    @Test
    public void testCreateValue_Class() throws ParseException {
        String str = "java.lang.String";
        Object result = typeHandler.createValue(str);
        assert result instanceof Class<?>;
        assert result.equals(String.class);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_Class_Exception() throws ParseException {
        String str = "not_a_class";
        typeHandler.createValue(str);
    }

    @Test
    public void testCreateValue_File() {
        String str = "/path/to/file.txt";
        Object result = typeHandler.createValue(str);
        assert result instanceof File;
        assert result.toString().equals(str);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_File_Exception() throws ParseException {
        String str = "/path/to/non_existing_file.txt";
        typeHandler.createValue(str);
    }

    @Test
    public void testCreateValue_Files() {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        Object result = typeHandler.createValue(str);
        assert result instanceof File[];
        File[] files = (File[]) result;
        assert files.length == 2;
        assert files[0].toString().equals("/path/to/file1.txt");
        assert files[1].toString().equals("/path/to/file2.txt");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_Files_Exception() throws ParseException {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        typeHandler.createValue(str);
    }

    @Test
    public void testCreateValue_URL() throws ParseException, MalformedURLException {
        String str = "http://www.example.com";
        Object result = typeHandler.createValue(str);
        assert result instanceof URL;
        assert result.toString().equals(str);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_URL_Exception() throws ParseException {
        String str = "not_a_url";
        typeHandler.createValue(str);
    }
}
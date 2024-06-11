
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithString() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObject() throws ParseException {
        String str = "org.apache.commons.cli.TypeHandler";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(TypeHandler.class.getName(), result.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateValueWithDate() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithClass() throws ParseException {
        String str = "java.util.Date";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(Date.class, result);
    }

    @Test
    public void testCreateValueWithFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(File.class, result.getClass());
    }

    @Test
    public void testCreateValueWithExistingFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(FileInputStream.class, result.getClass());
    }

    @Test
    public void testCreateValueWithFiles() throws ParseException {
        String str = "file1.txt,file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithUrl() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(URL.class, result.getClass());
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "org.apache.commons.cli.TypeHandler";
        Object result = TypeHandler.createObject(classname);
        Assert.assertEquals(TypeHandler.class.getName(), result.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateNumberWithDouble() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Double.valueOf(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumber() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.util.Date";
        Class<?> result = TypeHandler.createClass(classname);
        Assert.assertEquals(Date.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        Date result = TypeHandler.createDate(str);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals(URL.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidUrl() throws ParseException {
        String str = "invalid_url";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertEquals(File.class, result.getClass());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        FileInputStream result = TypeHandler.openFile(str);
        Assert.assertEquals(FileInputStream.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testOpenFileWithInvalidFile() throws ParseException {
        String str = "invalid_file.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "file1.txt,file2.txt";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.FILE_VALUE;
import static org.apache.commons.cli.PatternOptionBuilder.FILES_VALUE;
import static org.apache.commons.cli.PatternOptionBuilder.NUMBER_VALUE;
import static org.apache.commons.cli.PatternOptionBuilder.STRING_VALUE;
import static org.apache.commons.cli.PatternOptionBuilder.URL_VALUE;

/**
 * TypeHandlerTest.java
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithStringType() throws ParseException {
        String str = "TestString";
        Object obj = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(str, obj);
    }

    @Test
    public void testCreateValueWithObjectType_ValidClass() throws ParseException, ClassNotFoundException, InstantiationException {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue(classname, String.class);
        Assert.assertNotNull(obj);
        Assert.assertTrue(obj.getClass().getName().equals(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithObjectType_InvalidClass() throws ParseException, ClassNotFoundException {
        String classname = "java.lang.Integer";
        TypeHandler.createValue(classname, String.class);
    }

    @Test
    public void testCreateValueWithNumberType_Long() throws ParseException {
        String str = "123";
        Number number = TypeHandler.createValue(str, Long.class);
        Assert.assertEquals(Long.parseLong(str), number.longValue());
    }

    @Test
    public void testCreateValueWithNumberType_Double() throws ParseException {
        String str = "123.45";
        Number number = TypeHandler.createValue(str, Double.class);
        Assert.assertEquals(Double.parseDouble(str), number.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithNumberType_InvalidNumber() throws ParseException {
        String str = "abc";
        TypeHandler.createValue(str, Long.class);
    }

    @Test
    public void testCreateValueWithDateType_UnsupportedOperationException() throws ParseException {
        String str = "2023-01-31";
        Date date = TypeHandler.createValue(str, Date.class);
        Assert.assertNull(date);
    }

    @Test
    public void testCreateValueWithClassType() throws ParseException, ClassNotFoundException, InstantiationException {
        String className = "java.lang.String";
        Class<?> clazz = TypeHandler.createValue(className, Class.class);
        Assert.assertNotNull(clazz);
        Assert.assertTrue(clazz.getName().equals(className));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithClassType_InvalidClass() throws ParseException, ClassNotFoundException {
        String className = "java.lang.Integer";
        TypeHandler.createValue(className, Class.class);
    }

    @Test
    public void testCreateValueWithURLType() throws ParseException, MalformedURLException {
        String urlString = "http://example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        Assert.assertNotNull(url);
        Assert.assertEquals(urlString, url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithURLType_InvalidURL() throws ParseException, MalformedURLException {
        String urlString = "invalid_url";
        TypeHandler.createValue(urlString, URL.class);
    }

    @Test
    public void testCreateFile() {
        String filePath = "test.txt";
        File file = TypeHandler.createFile(filePath);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.exists());
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_NonExistingFile() throws IOException, ParseException {
        String filePath = "non_existing_file.txt";
        TypeHandler.openFile(filePath);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_UnreadableFile() throws IOException, FileNotFoundException, ParseException {
        String filePath = "./TypeHandlerTest.java";
        File file = new File(filePath);
        TypeHandler.openFile(filePath);
    }

    @Test
    public void testCreateFiles() {
        String filePath = "test_files/";
        File directory = new File(filePath);
        directory.mkdir();
        String[] files = {"file1.txt", "file2.txt"};
        File[] fileArray = TypeHandler.createFiles(filePath + File.separator + "*");
        Assert.assertArrayEquals(files, fileArray);
        for (File file : fileArray) {
            Assert.assertTrue(file.exists());
        }
    }
}
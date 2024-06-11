import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * TypeHandlerTest class to test all methods in TypeHandler class
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringAndString() {
        String str = "testString";
        Object obj = new String("testString");
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(obj, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateValueWithNullStringAndString() {
        String str = null;
        Object obj = new String("testString");
        TypeHandler.createValue(str, obj.getClass());
    }

    @Test
    public void testCreateValueWithStringAndClass() {
        String str = "java.lang.String";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertEquals(clazz, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueWithInvalidStringAndClass() {
        String str = "java.lang.Integer";
        Class clazz = String.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        Assert.assertNotNull(result);
        Assert.assertEquals(String.class, result.getClass());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectWithInvalidClassName() {
        String className = "InvalidClass";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";
        Number number = 123;
        Number result = TypeHandler.createNumber(numberString);
        Assert.assertEquals(number, result);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberWithInvalidString() {
        String numberString = "abc";
        TypeHandler.createNumber(numberString);
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";
        Class clazz = String.class;
        Class result = TypeHandler.createClass(className);
        Assert.assertEquals(clazz, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClassWithInvalidClassName() {
        String className = "InvalidClass";
        TypeHandler.createClass(className);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-01-01";
        Date date = new Date(2022, 0, 1);
        Date result = TypeHandler.createDate(dateString);
        Assert.assertEquals(date, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateWithInvalidString() {
        String dateString = "2022-01-33";
        TypeHandler.createDate(dateString);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL url = new URL("http://www.example.com");
        URL result = TypeHandler.createURL(urlString);
        Assert.assertEquals(url, result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLWithInvalidString() {
        String urlString = "http://www.example.com/invalid";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile() {
        String filePath = "testFile.txt";
        File expectedFile = new File(filePath);
        File result = TypeHandler.createFile(filePath);
        Assert.assertEquals(expectedFile, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileWithInvalidFilePath() {
        String filePath = "invalidFilePath";
        TypeHandler.createFile(filePath);
    }

    @Test
    public void testCreateFiles() {
        String filesPath = "testFile1.txt,testFile2.txt";
        File[] expectedFiles = new File[] {new File("testFile1.txt"), new File("testFile2.txt")};
        File[] result = TypeHandler.createFiles(filesPath);
        Assert.assertArrayEquals(expectedFiles, result);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCreateFilesWithInvalidFilePaths() {
        String filesPath = "testFile1.txt,testFile2.txt,testFile3.txt";
        TypeHandler.createFiles(filesPath);
    }
}
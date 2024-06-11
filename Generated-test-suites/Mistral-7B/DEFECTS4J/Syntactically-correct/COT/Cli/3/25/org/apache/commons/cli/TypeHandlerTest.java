import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * TypeHandlerTest class
 */
public class TypeHandlerTest {

    /**
     * Test createValue(String, Object) method with String type
     */
    @Test
    public void testCreateValueStringObjectString() {
        String str = "testString";
        String expected = "testString";
        Object obj = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(expected, obj);
    }

    /**
     * Test createValue(String, Object) method with Object type
     */
    @Test
    public void testCreateValueStringObjectObject() {
        String str = "testObject";
        Object expected = "testObject";
        Object obj = TypeHandler.createValue(str, Object.class);
        Assert.assertEquals(expected, obj);
    }

    /**
     * Test createValue(String, Class) method with String_VALUE type
     */
    @Test
    public void testCreateValueStringClassStringValue() {
        String str = "testString";
        Object expected = str;
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with OBJECT_VALUE type
     */
    @Test
    public void testCreateValueStringClassObjectValue() {
        String str = "testObject";
        Class clazz = Class.forName("java.lang.String");
        Object expected = "testObject";
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with NUMBER_VALUE type
     */
    @Test
    public void testCreateValueStringClassNumberValue() {
        String str = "123";
        Number expected = 123;
        Number result = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with NUMBER_VALUE type, invalid number
     */
    @Test(expected = NumberFormatException.class)
    public void testCreateValueStringClassNumberValueInvalid() {
        String str = "invalidNumber";
        TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }

    /**
     * Test createValue(String, Class) method with DATE_VALUE type
     */
    @Test
    public void testCreateValueStringClassDateValue() {
        String str = "2023-03-31";
        Date expected = new Date(2023 - 1900, 2, 31);
        Date result = (Date) TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with DATE_VALUE type, invalid date
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringClassDateValueInvalid() {
        String str = "invalidDate";
        TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }

    /**
     * Test createValue(String, Class) method with CLASS_VALUE type
     */
    @Test
    public void testCreateValueStringClassClassValue() {
        String str = "java.lang.String";
        Class expected = String.class;
        Class result = (Class) TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with CLASS_VALUE type, invalid class name
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateValueStringClassClassValueInvalid() {
        String str = "InvalidClassName";
        TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }

    /**
     * Test createValue(String, Class) method with FILE_VALUE type
     */
    @Test
    public void testCreateValueStringClassFileValue() {
        String str = "testFile.txt";
        File expected = new File("testFile.txt");
        File result = (File) TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        Assert.assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    /**
     * Test createValue(String, Class) method with EXISTING_FILE_VALUE type
     */
    @Test
    public void testCreateValueStringClassExistingFileValue() {
        String str = "testFile.txt";
        File expected = new File("testFile.txt");
        File result = (File) TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    /**
     * Test createValue(String, Class) method with EXISTING_FILE_VALUE type, file not found
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringClassExistingFileValueNotFound() {
        String str = "nonExistentFile.txt";
        TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
    }

    /**
     * Test createValue(String, Class) method with URL_VALUE type
     */
    @Test
    public void testCreateValueStringClassUrlValue() {
        String str = "http://example.com";
        URL expected = new URL("http://example.com");
        URL result = (URL) TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    /**
     * Test createValue(String, Class) method with URL_VALUE type, invalid URL
     */
    @Test(expected = MalformedURLException.class)
    public void testCreateValueStringClassUrlValueInvalid() {
        String str = "invalidURL";
        TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
    }

    /**
     * Test createObject(String) method
     */
    @Test
    public void testCreateObjectString() {
        String str = "java.lang.String";
        Class expected = String.class;
        Class result = (Class) TypeHandler.createObject(str);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createObject(String) method, invalid class name
     */
    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectStringInvalid() {
        String str = "InvalidClassName";
        TypeHandler.createObject(str);
    }

    /**
     * Test createNumber(String) method
     */
    @Test
    public void testCreateNumberString() {
        String str = "123";
        Number expected = 123;
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createNumber(String) method, invalid number
     */
    @Test(expected = NumberFormatException.class)
    public void testCreateNumberStringInvalid() {
        String str = "invalidNumber";
        TypeHandler.createNumber(str);
    }

    /**
     * Test createDate(String) method
     */
    @Test
    public void testCreateDateString() {
        String str = "2023-03-31";
        Date expected = new Date(2023 - 1900, 2, 31);
        Date result = TypeHandler.createDate(str);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test createDate(String) method, invalid date
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateStringInvalid() {
        String str = "invalidDate";
        TypeHandler.createDate(str);
    }

    /**
     * Test createURL(String) method
     */
    @Test
    public void testCreateURLString() {
        String str = "http://example.com";
        URL expected = new URL("http://example.com");
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    /**
     * Test createURL(String) method, invalid URL
     */
    @Test(expected = MalformedURLException.class)
    public void testCreateURLStringInvalid() {
        String str = "invalidURL";
        TypeHandler.createURL(str);
    }

    /**
     * Test createFile(String) method
     */
    @Test
    public void testCreateFileString() {
        String str = "testFile.txt";
        File expected = new File("testFile.txt");
        File result = TypeHandler.createFile(str);
        Assert.assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    /**
     * Test createFiles(String) method
     */
    @Test
    public void testCreateFilesString() {
        String str = "testFile1.txt,testFile2.txt";
        File[] expected = new File[]{"testFile1.txt", "testFile2.txt"};
        File[] result = TypeHandler.createFiles(str);
        Assert.assertArrayEquals(expected, result);
    }
}
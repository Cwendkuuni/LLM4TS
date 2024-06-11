import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    private static final String TEST_STRING = "test";
    private static final String TEST_CLASSNAME = "TestClass";
    private static final String TEST_NUMBER = "123";
    private static final String TEST_DOUBLE = "123.456";
    private static final String TEST_DATE = "2020-01-01";
    private static final String TEST_URL = "https://www.example.com";
    private static final String TEST_FILE = "test.txt";
    private static final String TEST_FILES = "file1.txt,file2.txt";

    @Test
    public void testCreateValueWithStringValue() throws ParseException {
        Object result = TypeHandler.createValue(TEST_STRING, String.class);
        Assert.assertEquals(TEST_STRING, result);
    }

    @Test
    public void testCreateValueWithObjectValue() throws ParseException {
        Object result = TypeHandler.createValue(TEST_STRING, Object.class);
        Assert.assertEquals(TEST_STRING, result);
    }

    @Test
    public void testCreateValueWithNumberValue() throws ParseException {
        Object result = TypeHandler.createValue(TEST_NUMBER, Number.class);
        Assert.assertEquals(Long.parseLong(TEST_NUMBER), result);
    }

    @Test
    public void testCreateValueWithDoubleValue() throws ParseException {
        Object result = TypeHandler.createValue(TEST_DOUBLE, Number.class);
        Assert.assertEquals(Double.parseDouble(TEST_DOUBLE), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidNumberValue() throws ParseException {
        TypeHandler.createValue("abc", Number.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithDateValue() {
        TypeHandler.createValue(TEST_DATE, Date.class);
    }

    @Test
    public void testCreateValueWithClassValue() throws ParseException {
        Object result = TypeHandler.createValue(TEST_CLASSNAME, Class.class);
        Assert.assertEquals(Class.forName(TEST_CLASSNAME), result);
    }

    @Test
    public void testCreateValueWithFileValue() {
        Object result = TypeHandler.createValue(TEST_FILE, File.class);
        Assert.assertEquals(new File(TEST_FILE), result);
    }

    @Test
    public void testCreateValueWithExistingFileValue() {
        Object result = TypeHandler.createValue(TEST_FILE, File.class);
        Assert.assertEquals(new File(TEST_FILE), result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithFilesValue() {
        TypeHandler.createValue(TEST_FILES, File[].class);
    }

    @Test
    public void testCreateValueWithURLValue() throws ParseException, MalformedURLException {
        Object result = TypeHandler.createValue(TEST_URL, URL.class);
        Assert.assertEquals(new URL(TEST_URL), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidURLValue() throws ParseException {
        TypeHandler.createValue("invalid-url", URL.class);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassName() throws ParseException {
        TypeHandler.createObject("invalid-class");
    }

    @Test
    public void testCreateObjectWithValidClassName() throws ParseException {
        Object result = TypeHandler.createObject(TEST_CLASSNAME);
        Assert.assertEquals(TEST_CLASSNAME, result.getClass().getSimpleName());
    }

    @Test
    public void testCreateNumberWithIntegerValue() throws ParseException {
        Number result = TypeHandler.createNumber(TEST_NUMBER);
        Assert.assertEquals(Long.parseLong(TEST_NUMBER), result);
    }

    @Test
    public void testCreateNumberWithDoubleValue() throws ParseException {
        Number result = TypeHandler.createNumber(TEST_DOUBLE);
        Assert.assertEquals(Double.parseDouble(TEST_DOUBLE), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidValue() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassName() throws ParseException {
        TypeHandler.createClass("invalid-class");
    }

    @Test
    public void testCreateClassWithValidClassName() throws ParseException, ClassNotFoundException {
        Class<?> result = TypeHandler.createClass(TEST_CLASSNAME);
        Assert.assertEquals(Class.forName(TEST_CLASSNAME), result);
    }

    // Skipped createDate and createURL tests as they are not implemented yet
    
    @Test
    public void testCreateFileWithValidFilePath() {
        File result = TypeHandler.createFile(TEST_FILE);
        Assert.assertEquals(new File(TEST_FILE), result);
    }

    // Skipped createFiles test as not implemented yet

}
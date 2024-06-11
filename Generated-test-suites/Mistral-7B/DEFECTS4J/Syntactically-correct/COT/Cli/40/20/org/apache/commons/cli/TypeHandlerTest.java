package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.utils.PatternOptionBuilder;
import org.junit.Test;

/**
 * Test class for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with String type.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "testString";
        Object expected = "testString";
        Object actual = TypeHandler.createValue(str, String.class);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} with Object type.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringObjectInstanceOfObject() throws ParseException
    {
        String str = "testString";
        Object actual = TypeHandler.createValue(str, Object.class);
        assertInstanceOf(String.class, actual);
        assertEquals(str, actual.toString());
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with StringType.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringStringType() throws ParseException
    {
        String str = "testString";
        String expected = "testString";
        String actual = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with ObjectType.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringObjectType() throws ParseException
    {
        String str = "testString";
        Object actual = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertInstanceOf(String.class, actual.getClass());
        assertEquals(str, actual.toString());
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with NumberType.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringNumberType() throws ParseException
    {
        String str = "123";
        Long expected = 123L;
        Long actual = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with NumberType and Double.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringNumberTypeDouble() throws ParseException
    {
        String str = "123.45";
        Double expected = 123.45;
        Double actual = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(expected, actual, 0.0);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with DateType.
     * @throws ParseException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringDateType() throws ParseException
    {
        String str = "2022-12-31";
        TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with ClassType.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringClassType() throws ParseException
    {
        String classname = "java.lang.String";
        String expected = "testString";
        String actual = TypeHandler.createValue(classname, String.class).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with ClassType and new instance.
     * @throws ParseException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void testCreateValueStringClassTypeNewInstance() throws ParseException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        String expected = "testString";
        String actual = TypeHandler.createValue(classname, String.class).toString();
        assertEquals(expected, actual);

        String newInstance = (String) TypeHandler.createValue(classname, String.class).newInstance();
        assertEquals(expected, newInstance);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with ClassType and Class.forName().
     * @throws ParseException
     * @throws ClassNotFoundException
     */
    @Test
    public void testCreateValueStringClassTypeClassForName() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.String";
        String expected = "testString";
        String actual = TypeHandler.createValue(classname, String.class).toString();
        assertEquals(expected, actual);

        Class<?> clazz = Class.forName(classname);
        String actualFromClassForName = (String) TypeHandler.createValue(classname, clazz);
        assertEquals(expected, actualFromClassForName);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with FileType.
     * @throws ParseException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringFileType() throws ParseException
    {
        String str = "/path/to/testFile.txt";
        TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with ExistingFileType.
     * @throws ParseException
     * @throws FileNotFoundException
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringExistingFileType() throws ParseException, FileNotFoundException
    {
        String str = "/path/to/testFile.txt";
        TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with FilesType.
     * @throws ParseException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringFilesType() throws ParseException
    {
        String str = "/path/to/testFiles/*.txt";
        TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with URLType.
     * @throws ParseException
     */
    @Test
    public void testCreateValueStringURLType() throws ParseException
    {
        String str = "http://example.com";
        URL expected = new URL(str);
        URL actual = (URL) TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with valid classname.
     * @throws ParseException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void testCreateObjectValidClassName() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        String expected = "testString";
        String actual = ((String) TypeHandler.createObject(classname)).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)} with invalid classname.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException
    {
        String classname = "java.lang.InvalidClassName";
        TypeHandler.createObject(classname);
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with valid number string.
     * @throws ParseException
     */
    @Test
    public void testCreateNumberValidNumberString() throws ParseException
    {
        String str = "123";
        Long expected = 123L;
        Long actual = (Long) TypeHandler.createNumber(str);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)} with invalid number string.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumberString() throws ParseException
    {
        String str = "123a";
        TypeHandler.createNumber(str);
    }

    /**
     * Test method for {@link TypeHandler#createClass(String)} with valid classname.
     * @throws ParseException
     * @throws ClassNotFoundException
     */
    @Test
    public void testCreateClassValidClassName() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> actual = TypeHandler.createClass(classname);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createClass(String)} with invalid classname.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClassName() throws ParseException
    {
        String classname = "java.lang.InvalidClassName";
        TypeHandler.createClass(classname);
    }

    /**
     * Test method for {@link TypeHandler#createDate(String)} with valid date string.
     * @throws ParseException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateValidDateString() throws ParseException
    {
        String str = "2022-12-31";
        TypeHandler.createDate(str);
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)} with valid url string.
     * @throws ParseException
     */
    @Test
    public void testCreateURLValidURLString() throws ParseException
    {
        String str = "http://example.com";
        URL expected = new URL(str);
        URL actual = (URL) TypeHandler.createURL(str);
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)} with invalid url string.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testCreateURLInvalidURLString() throws ParseException
    {
        String str = "http://example.com/invalidURL";
        TypeHandler.createURL(str);
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with valid file path.
     */
    @Test
    public void testCreateFileValidFilePath()
    {
        String str = "/path/to/testFile.txt";
        File expected = new File(str);
        File actual = TypeHandler.createFile(str);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)} with invalid file path.
     * @throws ParseException
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidFilePath() throws ParseException
    {
        String str = "/path/to/invalidFile.txt";
        TypeHandler.createFile(str);
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with valid file path.
     * @throws ParseException
     * @throws IOException
     */
    @Test
    public void testOpenFileValidFilePath() throws ParseException, IOException
    {
        String str = "/path/to/testFile.txt";
        File file =new File(str);
        FileInputStream expected = new FileInputStream(file);
        FileInputStream actual = TypeHandler.openFile(str);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link TypeHandler#openFile(String)} with invalid file path.
     * @throws ParseException
     * @throws FileNotFoundException
     */
    @Test(expected = ParseException.class)
    public void testOpenFileInvalidFilePath() throws ParseException, FileNotFoundException
    {
        String str = "/path/to/invalidFile.txt";
        TypeHandler.openFile(str);
    }
}
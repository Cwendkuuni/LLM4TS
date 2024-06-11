import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object actual = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringInvalidObjectType() {
        Object actual = TypeHandler.createValue("test", int.class);
        Assert.fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testCreateValueStringPatternStringValue() {
        Object actual = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals("test", actual);
    }

    @Test
    public void testCreateValueStringPatternObjectValue() {
        Object actual = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNull(actual);
    }

    @Test
    public void testCreateValueStringPatternNumberValue() {
        Number actual = (Number) TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(123, actual.intValue());

        actual = (Number) TypeHandler.createValue("456.78", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(456.78, actual.doubleValue(), 0.001);
    }

    @Test
    public void testCreateValueStringPatternDateValue() {
        Date actual = TypeHandler.createValue("2022-12-31", PatternOptionBuilder.DATE_VALUE);
        Assert.assertNotNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringPatternClassValue() {
        Object actual = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        Assert.fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testCreateValueStringPatternFileValue() {
        File actual = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.FILE_VALUE);
        Assert.assertNotNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringPatternExistingFileValue() {
        File actual = TypeHandler.createValue("/path/to/nonexistentfile", PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.fail("Should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringPatternFilesValue() {
        File[] actual = TypeHandler.createFiles("path1/file1, path2/file2");
        Assert.fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testCreateValueStringPatternURLValue() {
        URL actual = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        Assert.assertNotNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringPatternInvalidClass() {
        Object actual = TypeHandler.createValue("java.lang.Object", 123.0);
        Assert.fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testCreateObject() {
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createObject("java.lang.String");
        Assert.assertInstanceOf(clazz, actual);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectInvalidClassName() {
        Object actual = TypeHandler.createObject("java.lang.InvalidClassName");
        Assert.fail("Should have thrown ClassNotFoundException");
    }

    @Test
    public void testCreateNumber() {
        Number actual = TypeHandler.createNumber("123");
        Assert.assertEquals(123, actual.intValue());

        actual = TypeHandler.createNumber("456.78");
        Assert.assertEquals(456.78, actual.doubleValue(), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalidNumber() {
        Number actual = TypeHandler.createNumber("test");
        Assert.fail("Should have thrown NumberFormatException");
    }

    @Test
    public void testCreateClass() {
        Class<?> actual = TypeHandler.createClass("java.lang.String");
        Assert.assertEquals(String.class, actual);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClassInvalidClassName() {
        Class<?> actual = TypeHandler.createClass("java.lang.InvalidClassName");
        Assert.fail("Should have thrown ClassNotFoundException");
    }

    @Test
    public void testCreateDate() {
        Date actual = TypeHandler.createDate("2022-12-31");
        Assert.assertNotNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateInvalidDateFormat() {
        Date actual = TypeHandler.createDate("InvalidDateFormat");
        Assert.fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testCreateURL() {
        URL actual = TypeHandler.createURL("http://www.example.com");
        Assert.assertNotNull(actual);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLInvalidURLFormat() {
        URL actual = TypeHandler.createURL("InvalidURLFormat");
        Assert.fail("Should have thrown MalformedURLException");
    }

    @Test
    public void testCreateFile() {
        File actual = TypeHandler.createFile("/path/to/file");
        Assert.assertNotNull(actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileNonexistentPath() {
        File actual = TypeHandler.createFile("/path/to/nonexistentfile");
        Assert.fail("Should have thrown FileNotFoundException");
    }

    @Test
    public void testCreateFiles() {
        File[] actual = TypeHandler.createFiles("path1/file1, path2/file2");
        Assert.assertNotNull(actual);
        Assert.assertEquals(2, actual.length);
    }
}
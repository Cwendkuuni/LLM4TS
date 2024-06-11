import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;


public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObj() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object expected = "test";
        Object actual = TypeHandler.createValue(str, obj);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithClassObj() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object expected = String.class;
        Object actual = TypeHandler.createValue(str, obj);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateValueWithInvalidValue() {
        String str = "test";
        Object obj = new Object();
        try {
            TypeHandler.createValue(str, obj);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("java.lang.NullPointerException; Unable to create an instance of: java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testCreateObjectWithValidClassname() throws ParseException {
        String classname = "java.lang.String";
        Object expected = String.class;
        Object actual = TypeHandler.createObject(classname);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateObjectWithInvalidClassname() {
        String classname = "invalid.Classname";
        try {
            TypeHandler.createObject(classname);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unable to find the class: invalid.Classname", e.getMessage());
        }
    }

    @Test
    public void testCreateNumberWithValidInteger() throws ParseException {
        String str = "10";
        Number expected = 10L;
        Number actual = TypeHandler.createNumber(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberWithValidDouble() throws ParseException {
        String str = "10.5";
        Number expected = 10.5;
        Number actual = TypeHandler.createNumber(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateNumberWithInvalidValue() {
        String str = "abc";
        try {
            TypeHandler.createNumber(str);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("For input string: \"abc\"", e.getMessage());
        }
    }

    @Test
    public void testCreateClassWithValidClassname() throws ParseException {
        String classname = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> actual = TypeHandler.createClass(classname);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateClassWithInvalidClassname() {
        String classname = "invalid.Classname";
        try {
            TypeHandler.createClass(classname);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unable to find the class: invalid.Classname", e.getMessage());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2020-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURLWithValidURL() throws ParseException {
        String str = "https://www.example.com";
        URL expected = new URL(str);
        URL actual = TypeHandler.createURL(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateURLWithInvalidURL() {
        String str = "invalid.url";
        try {
            TypeHandler.createURL(str);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unable to parse the URL: invalid.url", e.getMessage());
        }
    }

    @Test
    public void testCreateFile() {
        String str = "path/to/file";
        File expected = new File(str);
        File actual = TypeHandler.createFile(str);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateFiles() {
        String str = "path/to/file1,path/to/file2";
        try {
            File[] actual = TypeHandler.createFiles(str);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Not yet implemented", e.getMessage());
        }
    }

}
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringAndObject() throws ParseException {
        Object result = TypeHandler.createValue("testString", String.class);
        Assert.assertEquals("testString", result);
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        try {
            TypeHandler.createValue("testValue", Integer.class);
        } catch (ParseException e) {
            Assert.assertTrue("Exception message should contain 'Unable to find'", e.getMessage().contains("Unable to find"));
        }
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        Assert.assertTrue(result instanceof String);
    }

    @Test
    public void testCreateNumberWithDoubleValue() throws ParseException {
        Number result = TypeHandler.createNumber("10.5");
        Assert.assertEquals(10.5, result);
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("https://www.example.com");
        Assert.assertEquals("https://www.example.com", result.toString());
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("testFile.txt");
        Assert.assertEquals("testFile.txt", result.getName());
    }

    @Test
    public void testOpenFileWithInvalidPath() {
        try {
            TypeHandler.openFile("invalid_path");
        } catch (ParseException e) {
            Assert.assertTrue("Exception message should contain 'Unable to find file'", e.getMessage().contains("Unable to find file"));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        TypeHandler.createFiles("testFiles");
    }
}
import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringType() throws ParseException {
        Object value = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals("test", value);
    }

    @Test
    public void testCreateValueWithObjectType() throws ParseException {
        Object value = TypeHandler.createValue("TestClass", PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertNotNull(value);
        Assert.assertEquals("TestClass", value.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumberType() throws ParseException {
        Object value1 = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertNotNull(value1);
        Assert.assertEquals(Long.class, value1.getClass());

        Object value2 = TypeHandler.createValue("123.45", PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertNotNull(value2);
        Assert.assertEquals(Double.class, value2.getClass());
    }

    @Test
    public void testCreateValueWithDateType() {
        try {
            Object value = TypeHandler.createValue("2020-01-01", PatternOptionBuilder.DATE_VALUE);
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testCreateValueWithClassType() throws ParseException {
        Object value = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        Assert.assertNotNull(value);
        Assert.assertEquals(Class.forName("java.lang.String"), value);
    }

    @Test
    public void testCreateValueWithFileType() {
        Object value = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        Assert.assertNotNull(value);
        Assert.assertEquals(File.class, value.getClass());
        Assert.assertEquals("test.txt", ((File)value).getPath());
    }

    @Test
    public void testCreateValueWithExistingFileType() {
        Object value = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertNotNull(value);
        Assert.assertEquals(File.class, value.getClass());
        Assert.assertEquals("test.txt", ((File)value).getPath());
    }

    @Test
    public void testCreateValueWithFilesType() {
        try {
            Object value = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Not yet implemented", e.getMessage());
        }
    }

    @Test
    public void testCreateValueWithURLType() throws ParseException, MalformedURLException {
        Object value = TypeHandler.createValue("https://www.google.com", PatternOptionBuilder.URL_VALUE);
        Assert.assertNotNull(value);
        Assert.assertEquals(URL.class, value.getClass());
        Assert.assertEquals("https://www.google.com", ((URL)value).toString());
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object value = TypeHandler.createObject("TestClass");
        Assert.assertNotNull(value);
        Assert.assertEquals("TestClass", value.getClass().getName());
    }

    @Test
    public void testCreateNumber() throws ParseException {
        Number value1 = TypeHandler.createNumber("123");
        Assert.assertEquals(Long.class, value1.getClass());
        Assert.assertEquals(123L, value1);

        Number value2 = TypeHandler.createNumber("123.45");
        Assert.assertEquals(Double.class, value2.getClass());
        Assert.assertEquals(123.45, value2);
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException {
        Class<?> value = TypeHandler.createClass("java.lang.String");
        Assert.assertNotNull(value);
        Assert.assertEquals(Class.forName("java.lang.String"), value);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException {
        URL value = TypeHandler.createURL("https://www.google.com");
        Assert.assertNotNull(value);
        Assert.assertEquals(URL.class, value.getClass());
        Assert.assertEquals("https://www.google.com", value.toString());
    }

    @Test
    public void testCreateFile() {
        File value = TypeHandler.createFile("test.txt");
        Assert.assertNotNull(value);
        Assert.assertEquals(File.class, value.getClass());
        Assert.assertEquals("test.txt", value.getPath());
    }

    @Test
    public void testCreateFiles() {
        try {
            File[] value = TypeHandler.createFiles("file1.txt,file2.txt");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Not yet implemented", e.getMessage());
        }
    }
}
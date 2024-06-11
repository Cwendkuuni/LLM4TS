import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringClass() {
        String str = "Test";
        Class<String> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObjectClass() {
        String str = "org.apache.commons.cli.TypeHandler";
        Class<Class> clazz = Class.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(Class.forName(str), result);
    }

    @Test
    public void testCreateValueWithNumberClass() {
        String str = "3.14";
        Class<Number> clazz = Number.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(Double.valueOf(str), result);
    }

    @Test
    public void testCreateValueWithDateClass() {
        String str = "2021-01-01";
        Class<Date> clazz = Date.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithClassClass() {
        String str = "java.lang.String";
        Class<Class> clazz = Class.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(Class.forName(str), result);
    }

    @Test
    public void testCreateValueWithFileClass() {
        String str = "/path/to/file.txt";
        Class<File> clazz = File.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(new File(str), result);
    }

    @Test
    public void testCreateValueWithExistingFileClass() {
        String str = "/path/to/existingFile.txt";
        Class<File> clazz = File.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(new File(str), result);
    }

    @Test
    public void testCreateValueWithFilesClass() {
        String str = "/path/to/files";
        Class<File[]> clazz = File[].class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateValueWithURLClass() {
        String str = "http://www.example.com";
        Class<URL> clazz = URL.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(new URL(str), result);
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        String str = "InvalidClass";
        Class<Object> clazz = Object.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "org.apache.commons.cli.Option";
        Object result = TypeHandler.createObject(classname);
        Assert.assertEquals(Option.class, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberWithIntegerValue() throws ParseException {
        String str = "1";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateNumberWithDecimalValue() throws ParseException {
        String str = "3.14";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(Double.valueOf(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidValue() throws ParseException {
        String str = "InvalidNumber";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        Assert.assertEquals(new URL(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String str = "InvalidURL";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file.txt";
        File result = TypeHandler.createFile(str);
        Assert.assertEquals(new File(str), result);
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/files";
        File[] result = TypeHandler.createFiles(str);
        Assert.assertNull(result);
    }
}
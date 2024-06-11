import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        Object value = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", value);
    }

    @Test
    public void testCreateValue_Class_Object() throws ParseException {
        Object value = TypeHandler.createValue("java.lang.Object", Class.class);
        Assert.assertEquals(Object.class, value);
    }

    @Test
    public void testCreateValue_Number_Object() throws ParseException {
        Object value = TypeHandler.createValue("10", Number.class);
        Assert.assertEquals(10, value);
    }

    @Test
    public void testCreateValue_Date_Object() throws ParseException {
        Object value = TypeHandler.createValue("2021-01-01", Date.class);
        Assert.assertNull(value);
    }

    @Test
    public void testCreateValue_Class_Object_Null() throws ParseException {
        Object value = TypeHandler.createValue("invalid_class", Class.class);
        Assert.assertNull(value);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object object = TypeHandler.createObject("java.lang.String");
        Assert.assertEquals(String.class, object.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFound() throws ParseException {
        TypeHandler.createObject("com.example.InvalidClass");
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_Exception() throws ParseException {
        TypeHandler.createObject("java.util.Date");
    }

    @Test
    public void testCreateNumber_Integer() throws ParseException {
        Number number = TypeHandler.createNumber("10");
        Assert.assertEquals(Integer.valueOf(10), number);
    }

    @Test
    public void testCreateNumber_Double() throws ParseException {
        Number number = TypeHandler.createNumber("10.5");
        Assert.assertEquals(Double.valueOf(10.5), number);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_Exception() throws ParseException {
        TypeHandler.createNumber("invalid_number");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> clazz = TypeHandler.createClass("java.lang.String");
        Assert.assertEquals(String.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_NotFound() throws ParseException {
        TypeHandler.createClass("com.example.InvalidClass");
    }

    @Test
    public void testCreateDate() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createDate("2021-01-01");
        });
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL url = TypeHandler.createURL("https://www.example.com");
        Assert.assertEquals("https://www.example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_Malformed() throws ParseException {
        TypeHandler.createURL("invalid_url");
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        Assert.assertEquals("test.txt", file.getName());
    }

    @Test
    public void testCreateFiles() {
        Assert.assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createFiles("path/to/files");
        });
    }
}
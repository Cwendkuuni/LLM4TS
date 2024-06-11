import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {

    private static TypeHandler typeHandler;

    @BeforeClass
    public static void setUp() {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValue_String_Object_Success() throws ParseException {
        String str = "test";
        Object obj = String.class;

        Object result = typeHandler.createValue(str, obj);

        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Class_Success() throws ParseException {
        String str = "1";
        Class<?> clazz = Integer.class;

        Object result = typeHandler.createValue(str, clazz);

        Assert.assertEquals(1, result);
    }

    @Test
    public void testCreateValue_InvalidClass_ReturnNull() throws ParseException {
        String str = "test";
        Class<?> clazz = Object.class;

        Object result = typeHandler.createValue(str, clazz);

        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject_ValidClass_Success() throws ParseException {
        String classname = "java.lang.String";

        Object result = typeHandler.createObject(classname);

        Assert.assertTrue(result instanceof String);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClass_ThrowsParseException() throws ParseException {
        String classname = "InvalidClass";

        typeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_IntegerString_ReturnsInteger() throws ParseException {
        String str = "123";

        Number result = typeHandler.createNumber(str);

        Assert.assertTrue(result instanceof Integer);
        Assert.assertEquals(123, result);
    }

    @Test
    public void testCreateNumber_DoubleString_ReturnsDouble() throws ParseException {
        String str = "0.5";

        Number result = typeHandler.createNumber(str);

        Assert.assertTrue(result instanceof Double);
        Assert.assertEquals(0.5, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidNumber_ThrowsParseException() throws ParseException {
        String str = "abc";

        typeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_ValidClass_Success() throws ParseException {
        String classname = "java.lang.String";

        Class<?> result = typeHandler.createClass(classname);

        Assert.assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClass_ThrowsParseException() throws ParseException {
        String classname = "InvalidClass";

        typeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_NotImplemented_ThrowsUnsupportedOperationException() {
        String str = "2020-01-01";

        typeHandler.createDate(str);
    }

    @Test
    public void testCreateURL_ValidURL_Success() throws ParseException, MalformedURLException {
        String str = "http://www.example.com";

        URL result = typeHandler.createURL(str);

        Assert.assertEquals(new URL(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURL_ThrowsParseException() throws ParseException, MalformedURLException {
        String str = "invalidURL";

        typeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_ValidFile_ReturnsFile() {
        String str = "file.txt";

        File result = typeHandler.createFile(str);

        Assert.assertEquals(new File(str), result);
    }

    @Test
    public void testCreateFiles_NotImplemented_ThrowsUnsupportedOperationException() {
        String str = "file.txt";

        Exception exception = Assert.assertThrows(UnsupportedOperationException.class, () -> {
            typeHandler.createFiles(str);
        });

        String expectedMessage = "Not yet implemented";
        String actualMessage = exception.getMessage();

        Assert.assertTrue(actualMessage.contains(expectedMessage));
    }
}
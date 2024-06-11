import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringAndString_ReturnsString() {
        String str = "test";
        String objType = PatternOptionBuilder.STRING_VALUE.getName();
        Object result = TypeHandler.createValue(str, objType);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValue_StringAndClass_ThrowsExceptionWhenUnknownType() {
        String str = "test";
        Class<?> objType = String[].class;
        try {
            TypeHandler.createValue(str, objType);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringAndClass_ReturnsObjectForClass() {
        String className = "java.lang.String";
        String str = "test";
        Class<?> objType = Class.forName(className);
        Object result = TypeHandler.createValue(str, objType);

        Assert.assertEquals(className, result.getClass().getName());
    }

    @Test
    public void testCreateValue_StringAndClass_ReturnsNumberWhenNumber() {
        String str = "123.45";
        Class<?> objType = Double.class;
        Number result = (Number) TypeHandler.createValue(str, objType);

        Assert.assertEquals(123.45, result.doubleValue(), 0.01);
    }

    @Test
    public void testCreateValue_StringAndClass_ReturnsDateWhenDate() {
        String dateString = "2022-12-31";
        Class<?> objType = Date.class;
        Date result = (Date) TypeHandler.createValue(dateString, objType);

        Assert.assertNotNull(result);
        Assert.assertEquals("Fri Dec 31 00:00:00 CET 2022", result.toString());
    }

    @Test
    public void testCreateValue_StringAndClass_ReturnsFileWhenFile() {
        String filePath = "src/test/resources/test.txt";
        Class<?> objType = File.class;
        File result = (File) TypeHandler.createValue(filePath, objType);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isFile());
    }

    @Test
    public void testCreateValue_StringAndClass_ReturnsURLWhenURL() {
        String urlString = "http://example.com";
        Class<?> objType = URL.class;
        URL result = (URL) TypeHandler.createValue(urlString, objType);

        Assert.assertEquals(new URL("http://example.com"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_StringAndClass_ThrowsExceptionForNull() {
        String str = null;
        Class<?> objType = String.class;
        TypeHandler.createValue(str, objType);
    }
}
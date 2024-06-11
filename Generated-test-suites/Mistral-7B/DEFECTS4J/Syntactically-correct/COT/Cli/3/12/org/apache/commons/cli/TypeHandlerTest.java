import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;

/**
 * TypeHandlerTest
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        Object result = TypeHandler.createValue("someString", String.class);
        Assert.assertEquals("someString", result);

        try {
            result = TypeHandler.createValue("123", Integer.class);
            Assert.assertEquals(123, result);
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            result = TypeHandler.createValue("123.45", Double.class);
            Assert.assertEquals(123.45, result, 0.001);
        } catch (NumberFormatException e) {
            // expected
        }

        try {
            result = TypeHandler.createValue("2022-01-01", Date.class);
            Assert.assertEquals(new Date(2022, 0, 1), result);
        } catch (IllegalArgumentException e) {
            // expected for wrong date format
        }

        try {
            result = TypeHandler.createValue("com.example.MyClass", MyClass.class);
            Assert.assertNotNull(result);
        } catch (ClassNotFoundException e) {
            // expected
        }

        try {
            result = TypeHandler.createValue("file:/path/to/file.txt", File.class);
            Assert.assertNotNull(result);
        } catch (MalformedURLException e) {
            // expected
        }

        try {
            result = TypeHandler.createValue("http://example.com", URL.class);
            Assert.assertNotNull(result);
        } catch (MalformedURLException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_WrongType() {
        TypeHandler.createValue("someValue", int.class);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_NonExistentClass() {
        TypeHandler.createClass("non.existent.Class");
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_MalformedURL() {
        TypeHandler.createURL("invalid://example.com");
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_NonExistentFile() {
        File file = new File("non-existent-file.txt");
        TypeHandler.createFile("non-existent-file.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDate_InvalidFormat() {
        TypeHandler.createDate("2022-13-01");
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_NonInstanciableClass() {
        Class clazz = ClassUtils.forName("java.lang.reflect.MalformedMethodNameException");
        TypeHandler.createObject(clazz.getName());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidNumber() {
        TypeHandler.createNumber("invalid-number");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullObject() {
        TypeHandler.createValue(null, Object.class);
    }

    @Test
    public void testCreateValue_ArrayTypes() {
        // not fully covered, as the test case for PatternOptionBuilder.FILES_VALUE
        // would require handling multiple files

        String[] arrayStr = {"file1.txt", "file2.txt"};
        File[] files = TypeHandler.createFiles(String.join(",", arrayStr));
        Assert.assertArrayEquals(new File[]{new File("file1.txt"), new File("file2.txt")}, files);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_MalformedURL_NoProtocol() {
        TypeHandler.createURL("/file.txt");
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_MalformedURL_NoFile() {
        TypeHandler.createURL("http:/");
    }
}
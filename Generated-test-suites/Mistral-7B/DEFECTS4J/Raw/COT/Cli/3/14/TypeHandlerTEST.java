import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateStringValue() {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        Assert.assertEquals(str, obj);
    }

    @Test
    public void testCreateObjectValueNullClass() {
        String str = "java.lang.String";
        Object obj = TypeHandler.createValue(str, Object.class);
        Assert.assertNull(obj);
    }

    @Test
    public void testCreateObjectValueInvalidClass() {
        String str = "java.lang.String";
        Object obj = TypeHandler.createValue(str, Integer.class);
        Assert.assertNull(obj);
    }

    @Test
    public void testCreateObjectValue() {
        String str = "org.apache.commons.cli.TypeHandlerTest";
        Object obj = TypeHandler.createValue(str, TypeHandlerTest.class);
        Assert.assertEquals(TypeHandlerTest.class, obj);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectValueInvalidClassWithException() {
        String str = "java.abc.ABC";
        Object obj = TypeHandler.createValue(str, TypeHandlerTest.class);
    }

    @Test
    public void testCreateNumberValueLong() {
        String str = "123";
        Number number = (Number) TypeHandler.createValue(str, Long.class);
        Assert.assertEquals(Long.valueOf(123), number);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberValueInvalidLong() {
        String str = "abc";
        Number number = (Number) TypeHandler.createValue(str, Long.class);
        Assert.assertNull(number);
    }

    @Test
    public void testCreateNumberValueDouble() {
        String str = "123.4";
        Number number = (Number) TypeHandler.createValue(str, Double.class);
        Assert.assertEquals(Double.valueOf(123.4), number);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberValueInvalidDouble() {
        String str = "abc";
        Number number = (Number) TypeHandler.createValue(str, Double.class);
        Assert.assertNull(number);
    }

    @Test
    public void testCreateDateValueValid() {
        String str = "2022-01-01";
        Date date = (Date) TypeHandler.createValue(str, Date.class);
        Assert.assertNotNull(date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateValueInvalidFormat() {
        String str = "2022-01-33";
        Date date = (Date) TypeHandler.createValue(str, Date.class);
        Assert.assertNull(date);
    }

    @Test
    public void testCreateURLValueValid() {
        String str = "http://www.example.com";
        URL url = (URL) TypeHandler.createValue(str, URL.class);
        Assert.assertNotNull(url);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLValueInvalid() {
        String str = "http://www.example.com/abc";
        URL url = (URL) TypeHandler.createValue(str, URL.class);
        Assert.assertNull(url);
    }

    @Test
    public void testCreateFileValueValid() {
        String str = "/path/to/file.txt";
        File file = (File) TypeHandler.createValue(str, File.class);
        Assert.assertNotNull(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileValueInvalid() {
        String str = "/path/to/nonexistentfile.txt";
        File file = (File) TypeHandler.createValue(str, File.class);
        Assert.assertNull(file);
    }

    @Test
    public void testCreateFilesValueValid() {
        String str = "/path/to/file1.txt, /path/to/file2.txt";
        File[] files = (File[]) TypeHandler.createValue(str, File[].class);
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFilesValueInvalid() {
        String str = "/path/to/file1.txt, /path/to/nonexistentfile.txt, invalid";
        File[] files = (File[]) TypeHandler.createValue(str, File[].class);
        Assert.assertNull(files);
    }
}
import org.junit.Test;
import junit.framework.Assert;
import java.text.ParseException;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() throws ParseException {
        String str = "string";
        Object obj = "string";
        Assert.assertEquals(TypeHandler.createValue(str, obj), "string");
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Assert.assertEquals(TypeHandler.createObject(classname), "java.lang.String");
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "10";
        Assert.assertEquals(TypeHandler.createNumber(str), 10);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Assert.assertEquals(TypeHandler.createClass(classname), String.class);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        Assert.assertEquals(TypeHandler.createURL(str), new URL("http://www.example.com"));
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        Assert.assertEquals(TypeHandler.createFile(str), new File("test.txt"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInputStream() throws ParseException {
        String str = "test.txt";
        TypeHandler.createFileInputStream(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() {
        String str = "dir/";
        TypeHandler.createFiles(str);
    }
}
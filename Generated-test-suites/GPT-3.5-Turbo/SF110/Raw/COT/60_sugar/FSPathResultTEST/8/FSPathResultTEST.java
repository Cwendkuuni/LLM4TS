
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.getString());
    }

    @Test
    public void testGetDouble() {
        Double value = 10.5;
        FSPathResult result = new FSPathResult(value);
        Assert.assertEquals(value, result.getDouble());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date, result.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        Assert.assertEquals(bool, result.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.toString());
    }

    @Test
    public void testToStringDouble() {
        Double value = 10.5;
        FSPathResult result = new FSPathResult(value);
        Assert.assertEquals(value.toString(), result.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        Assert.assertEquals(bool.toString(), result.toString());
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), result.toString());
    }
}


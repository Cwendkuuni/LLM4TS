import org.junit.*;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.getString());
    }

    @Test
    public void testGetDouble() {
        Double doub = 1.23;
        FSPathResult result = new FSPathResult(doub);
        Assert.assertEquals(doub, result.getDouble());
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
    public void testToString() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), result.toString());

        String string = "testString";
        result = new FSPathResult(string);
        Assert.assertEquals(string, result.toString());

        Double doub = 1.23;
        result = new FSPathResult(doub);
        Assert.assertEquals(doub.toString(), result.toString());

        Boolean bool = true;
        result = new FSPathResult(bool);
        Assert.assertEquals(bool.toString(), result.toString());

        Date date = new Date();
        result = new FSPathResult(date);
        Assert.assertEquals(date.toString(), result.toString());

        URI uri = URI.create("http://www.example.com");
        result = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), result.toString());

        result = new FSPathResult(null);
        Assert.assertEquals("FSPathResult : empty", result.toString());
    }
}
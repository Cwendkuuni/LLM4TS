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
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.getString());
     }

     @Test
     public void testGetDouble() {
        Double num = 3.14;
        FSPathResult result = new FSPathResult(num);
        Assert.assertEquals(num, result.getDouble());
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
     public void testToString_File() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), result.toString());
     }

     @Test
     public void testToString_String() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.toString());
     }

     @Test
     public void testToString_Double() {
        Double num = 3.14;
        FSPathResult result = new FSPathResult(num);
        Assert.assertEquals(num.toString(), result.toString());
     }

     @Test
     public void testToString_Boolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        Assert.assertEquals(bool.toString(), result.toString());
     }

     @Test
     public void testToString_Date() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date.toString(), result.toString());
     }

     @Test
     public void testToString_URI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), result.toString());
     }
}
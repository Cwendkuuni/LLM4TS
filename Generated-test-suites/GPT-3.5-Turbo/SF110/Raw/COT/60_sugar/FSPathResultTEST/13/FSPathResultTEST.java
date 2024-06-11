
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        FSPathResult fsPathResult = new FSPathResult(new File("testFile.txt"));
        File result = fsPathResult.getFile();
        assert result.equals(new File("testFile.txt"));
    }

    @Test
    public void testGetString() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        String result = fsPathResult.getString();
        assert result.equals("testString");
    }

    @Test
    public void testGetDouble() {
        FSPathResult fsPathResult = new FSPathResult(1.5);
        Double result = fsPathResult.getDouble();
        assert result.equals(1.5);
    }

    @Test
    public void testGetDate() {
        Date currentDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(currentDate);
        Date result = fsPathResult.getDate();
        assert result.equals(currentDate);
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        URI result = fsPathResult.getURI();
        assert result.equals(uri);
    }

    @Test
    public void testGetBoolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        Boolean result = fsPathResult.getBoolean();
        assert result.equals(true);
    }

    @Test
    public void testToString_File() {
        FSPathResult fsPathResult = new FSPathResult(new File("testFile.txt"));
        String result = fsPathResult.toString();
        assert result.equals(new File("testFile.txt").getAbsolutePath());
    }

    @Test
    public void testToString_String() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        String result = fsPathResult.toString();
        assert result.equals("testString");
    }

    @Test
    public void testToString_Double() {
        FSPathResult fsPathResult = new FSPathResult(1.5);
        String result = fsPathResult.toString();
        assert result.equals("1.5");
    }

    @Test
    public void testToString_Boolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        String result = fsPathResult.toString();
        assert result.equals("true");
    }

    @Test
    public void testToString_Date() {
        Date currentDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(currentDate);
        String result = fsPathResult.toString();
        assert result.equals(currentDate.toString());
    }

    @Test
    public void testToString_URI() {
        URI uri = URI.create("http://www.test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String result = fsPathResult.toString();
        assert result.equals(uri.toString());
    }
}


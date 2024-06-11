import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        String str = "test string";
        FSPathResult fsPathResult = new FSPathResult(str);
        
        assertEquals(str, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        Double num = 3.14;
        FSPathResult fsPathResult = new FSPathResult(num);
        
        assertEquals(num, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        Boolean bool = true;
        FSPathResult fsPathResult = new FSPathResult(bool);
        
        assertEquals(bool, fsPathResult.getBoolean());
    }

    @Test
    public void testToString() {
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }
}
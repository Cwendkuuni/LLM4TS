import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        
        assert result.getFile().equals(file);
    }
    
    @Test
    public void testGetString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        
        assert result.getString().equals(string);
    }
    
    @Test
    public void testGetDouble() {
        Double dbl = 10.5;
        FSPathResult result = new FSPathResult(dbl);
        
        assert result.getDouble().equals(dbl);
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assert result.getDate().equals(date);
    }
    
    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assert result.getURI().equals(uri);
    }
    
    @Test
    public void testGetBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        
        assert result.getBoolean().equals(bool);
    }
    
    @Test
    public void testToString_File() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        
        assert result.toString().equals(file.getAbsolutePath());
    }
    
    @Test
    public void testToString_String() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        
        assert result.toString().equals(string);
    }
    
    @Test
    public void testToString_Double() {
        Double dbl = 10.5;
        FSPathResult result = new FSPathResult(dbl);
        
        assert result.toString().equals(dbl.toString());
    }
    
    @Test
    public void testToString_Boolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        
        assert result.toString().equals(bool.toString());
    }
    
    @Test
    public void testToString_Date() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assert result.toString().equals(date.toString());
    }
    
    @Test
    public void testToString_URI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assert result.toString().equals(uri.toString());
    }
}
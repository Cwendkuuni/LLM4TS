import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    private FSPathResult fsPathResult;
    private File file;
    private String string;
    private Double resultDouble;
    private Date date;
    private URI uri;
    private Boolean resultBoolean;

    @Before
    public void setUp() {
        file = new File("test.txt");
        string = "test";
        resultDouble = 3.14;
        date = new Date();
        uri = URI.create("https://www.example.com");
        resultBoolean = true;
        fsPathResult = new FSPathResult(file);
    }

    @Test
    public void testGetFile() {
        assertEquals(file, fsPathResult.getFile());
    }
    
    @Test
    public void testGetString() {
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.getString());
    }
    
    @Test
    public void testGetDouble() {
        fsPathResult = new FSPathResult(resultDouble);
        assertEquals(resultDouble, fsPathResult.getDouble());
    }
    
    @Test
    public void testGetDate() {
        fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }
    
    @Test
    public void testGetURI() {
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }
    
    @Test
    public void testGetBoolean() {
        fsPathResult = new FSPathResult(resultBoolean);
        assertEquals(resultBoolean, fsPathResult.getBoolean());
    }
    
    @Test
    public void testToStringWithFile() {
        String expected = file.getAbsolutePath();
        assertEquals(expected, fsPathResult.toString());
    }
    
    @Test
    public void testToStringWithString() {
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.toString());
    }
    
    @Test
    public void testToStringWithDouble() {
        fsPathResult = new FSPathResult(resultDouble);
        assertEquals(resultDouble.toString(), fsPathResult.toString());
    }
    
    @Test
    public void testToStringWithBoolean() {
        fsPathResult = new FSPathResult(resultBoolean);
        assertEquals(resultBoolean.toString(), fsPathResult.toString());
    }
    
    @Test
    public void testToStringWithDate() {
        fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }
    
    @Test
    public void testToStringWithURI() {
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }
    
    @Test
    public void testToStringEmpty() {
        fsPathResult = new FSPathResult(null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}
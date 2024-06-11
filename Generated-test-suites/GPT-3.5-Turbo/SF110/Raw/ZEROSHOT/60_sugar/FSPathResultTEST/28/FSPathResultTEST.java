
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {
    
    private FSPathResult fsPathResultFile;
    private FSPathResult fsPathResultString;
    private FSPathResult fsPathResultDouble;
    private FSPathResult fsPathResultDate;
    private FSPathResult fsPathResultURI;
    private FSPathResult fsPathResultBoolean;
    
    @Before
    public void setUp() {
        File file = new File("testFile.txt");
        fsPathResultFile = new FSPathResult(file);
        
        fsPathResultString = new FSPathResult("testString");
        
        Double resultDouble = 3.14;
        fsPathResultDouble = new FSPathResult(resultDouble);
        
        Date date = new Date();
        fsPathResultDate = new FSPathResult(date);
        
        URI uri = URI.create("http://example.com");
        fsPathResultURI = new FSPathResult(uri);
        
        fsPathResultBoolean = new FSPathResult(true);
    }
    
    @Test
    public void testGetFile() {
        assertEquals("testFile.txt", fsPathResultFile.getFile().getName());
    }
    
    @Test
    public void testGetString() {
        assertEquals("testString", fsPathResultString.getString());
    }
    
    @Test
    public void testGetDouble() {
        assertEquals(3.14, fsPathResultDouble.getDouble(), 0.01);
    }
    
    @Test
    public void testGetDate() {
        assertNotNull(fsPathResultDate.getDate());
    }
    
    @Test
    public void testGetURI() {
        assertEquals("http://example.com", fsPathResultURI.getURI().toString());
    }
    
    @Test
    public void testGetBoolean() {
        assertTrue(fsPathResultBoolean.getBoolean());
    }
    
    @Test
    public void testToString() {
        assertEquals("testFile.txt", fsPathResultFile.toString());
        assertEquals("testString", fsPathResultString.toString());
        assertEquals("3.14", fsPathResultDouble.toString());
        assertEquals("true", fsPathResultBoolean.toString());
        assertNotNull(fsPathResultDate.toString());
        assertEquals("http://example.com", fsPathResultURI.toString());
        assertEquals("FSPathResult : empty", new FSPathResult(null).toString());
    }
}

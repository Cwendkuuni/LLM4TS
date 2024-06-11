import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class FSPathResultTest {

    private File file;
    private String string;
	private Double doubleValue;
	private Date date;
	private URI uri;
	private Boolean bool;
	private FSPathResult fsPathResult;

    @Before
    public void setUp() {
        file = new File("test.txt");
		string = "testString";
		doubleValue = 3.1415;
		date = new Date();
		uri = URI.create("http://www.example.com");
		bool = true;
    }
    
    @Test
    public void testGetFile() {
        fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());
    }
    
    @Test
    public void testGetString() {
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.getString());
    }
    
    @Test
    public void testGetDouble() {
        fsPathResult = new FSPathResult(doubleValue);
        assertEquals(doubleValue, fsPathResult.getDouble());
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
        fsPathResult = new FSPathResult(bool);
        assertEquals(bool, fsPathResult.getBoolean());
    }
    
    @Test
    public void testToString() {
        fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
        
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.toString());
        
        fsPathResult = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), fsPathResult.toString());
        
        fsPathResult = new FSPathResult(bool);
        assertEquals(bool.toString(), fsPathResult.toString());
        
        fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
        
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
        
        fsPathResult = new FSPathResult(null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}
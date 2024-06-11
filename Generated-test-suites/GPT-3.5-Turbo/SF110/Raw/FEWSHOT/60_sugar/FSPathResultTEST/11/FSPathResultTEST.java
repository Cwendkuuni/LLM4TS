
package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {
    private File file = new File("test.txt");
    private String string = "test";
    private Double dbl = 3.14;
    private Date date = new Date();
    private URI uri = URI.create("http://www.example.com");
    private Boolean bool = true;
    
    @Test
    public void testGetFile() {
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }
    
    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }
    
    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(dbl);
        assertEquals(dbl, result.getDouble());
    }
    
    @Test
    public void testGetDate() {
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }
    
    @Test
    public void testGetURI() {
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }
    
    @Test
    public void testGetBoolean() {
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
    }
    
    @Test
    public void testToString() {
        FSPathResult fileResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fileResult.toString());
        
        FSPathResult stringResult = new FSPathResult(string);
        assertEquals(string, stringResult.toString());
        
        FSPathResult dblResult = new FSPathResult(dbl);
        assertEquals(dbl.toString(), dblResult.toString());
        
        FSPathResult boolResult = new FSPathResult(bool);
        assertEquals(bool.toString(), boolResult.toString());
        
        FSPathResult dateResult = new FSPathResult(date);
        assertEquals(date.toString(), dateResult.toString());
        
        FSPathResult uriResult = new FSPathResult(uri);
        assertEquals(uri.toString(), uriResult.toString());
        
        assertNull(new FSPathResult(null).toString());
    }
}

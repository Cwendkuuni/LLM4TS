import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {
    
    private Newzgrabber newzgrabber;
    
    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }
    
    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String expected = System.getProperty("user.dir");
        String actual = newzgrabber.findDirectory(classfile);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testUpdateIni() {
        String inifile = "test.ini";
        String prop = "Messageids";
        String key = "123456";
        String value = "789012";
        newzgrabber.updateIni(inifile, prop, key, value);
        
        // Assert that value is updated correctly in the ini file
        
    }
    
    @Test
    public void testWindowClosing() {
        // Test the windowClosing() method
        
    }
    
    // Add more test cases for other methods in the Newzgrabber class
    

}
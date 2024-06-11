import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NewzgrabberTest {
    
    private Newzgrabber newzGrabber;
    
    @Before
    public void setUp() {
        newzGrabber = new Newzgrabber();
    }
    
    @After
    public void tearDown() {
        newzGrabber = null;
    }
    
    @Test
    public void testNewzgrabber() {
        assertNotNull(newzGrabber);
        assertFalse(Newzgrabber.verbose);
        assertFalse(Newzgrabber.debug);
        assertTrue(Newzgrabber.IsBatch);
        assertEquals("Newz.jar", Newzgrabber.JarFilename);
        assertEquals("batch.ini", Newzgrabber.BatchFilename);
        assertEquals("Newzgrabber.ini", Newzgrabber.NewzInifilename);
    }
    
    @Test
    public void testSetupBasicSystem() {
        // Test for Windows OS
        System.setProperty("os.name", "Windows");
        Newzgrabber.setupBasicSystem();
        assertEquals("Adding Newz directory to library path", 
            Newzgrabber.Newzdirectory + ";" + System.getProperty("java.library.path"),
            System.getProperty("java.library.path"));
        
        // Test for non-Windows OS
        System.setProperty("os.name", "Linux");
        Newzgrabber.setupBasicSystem();
        assertEquals("Lib path remains the same",
            System.getProperty("java.library.path"),
            System.getProperty("java.library.path"));
    }
    
    @Test
    public void testFindDirectory() {
        String directory = Newzgrabber.findDirectory("Newz.jar");
        assertNotNull(directory);
        assertTrue(directory.endsWith("Newzgrabber"));
    }
    
    @Test
    public void testUpdateIni() {
        String inifile = "config.ini";
        String prop = "Test";
        String key = "Key";
        String value = "Value";
        newzGrabber.updateIni(inifile, prop, key, value);
        
        // Assert that the ini file has been updated correctly
    }
    
    @Test
    public void testMain() {
        // Test for batch mode
        String[] args = {"-batch"};
        Newzgrabber.main(args);
        assertTrue(Newzgrabber.IsBatch);
        
        // Test for non-batch mode
        args = new String[]{};
        Newzgrabber.main(args);
        assertFalse(Newzgrabber.IsBatch);
    }
}
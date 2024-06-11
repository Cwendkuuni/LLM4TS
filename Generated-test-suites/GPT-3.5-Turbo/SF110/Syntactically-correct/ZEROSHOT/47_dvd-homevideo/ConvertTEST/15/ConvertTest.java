import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {
    GUI dvdGUI = new GUI();
    Convert convertObj;
    
    @Before
    public void setUp() {
        convertObj = new Convert("dvd-ntsc", "16:9", dvdGUI);
    }
    
    @Test
    public void testInit() {
        boolean result = convertObj.init();
        assertFalse(result);
    }
    
    @Test
    public void testRun() {
        convertObj.run();
    }
    
    @Test
    public void testTranscode() {
        convertObj.Transcode();
    }
    
    @Test
    public void testMatrix() {
        convertObj.matrix();
    }
    
    @After
    public void tearDown() {
        convertObj = null;
    }
}
import static org.junit.Assert.*;
import org.junit.*;

public class ConvertTest {
    
    private Convert convert;
    private GUI gui;
    
    @Before
    public void setUp() {
        gui = new GUI();
        convert = new Convert("dvd-ntsc", "16:9", gui);
    }
    
    @Test
    public void testInit() {
        boolean result = convert.init();
        assertFalse(result);
    }
    
    @Test
    public void testRun() {
        convert.run();
        boolean result = convert.m_Error;
        assertFalse(result);
    }
    
    @Test
    public void testTranscode() {
        convert.Transcode();
        String[] videoFiles = convert.video_files;
        assertNotNull(videoFiles);
        String transcode = convert.m_transcode;
        assertNotNull(transcode);
        String mplex = convert.mplex;
        assertNotNull(mplex);
    }
    
    @Test
    public void testMatrix() {
        convert.matrix();
        String strOutputDir = gui.strOutputDir;
        String matrixFilePath = strOutputDir + "/matrix.txt";
        File file = new File(matrixFilePath);
        assertTrue(file.exists());
    }
    
    @After
    public void tearDown() {
        gui = null;
        convert = null;
    }
}
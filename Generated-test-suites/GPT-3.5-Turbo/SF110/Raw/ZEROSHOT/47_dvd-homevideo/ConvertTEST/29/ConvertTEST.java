import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    // test if init method returns false when there is no error
    @Test
    public void testInitNoError() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        assertFalse(convert.init());
    }
    
    // test if init method returns true when there is an error in menuChkThread
    @Test
    public void testInitErrorInMenuChkThread() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(false);
        Convert convert = new Convert("format", "aspectRatio", gui);
        assertTrue(convert.init());
    }
    
    // test if init method returns true when there is an error in ErrorCheck for transcode.log
    @Test
    public void testInitErrorInTranscodeLog() {
        GUI gui = new GUI();
        gui.strOutputDir = "invalidDir";
        Convert convert = new Convert("format", "aspectRatio", gui);
        assertTrue(convert.init());
    }

    // test if init method returns true when there is an error in ErrorCheck for mplex.log
    @Test
    public void testInitErrorInMplexLog() {
        GUI gui = new GUI();
        gui.strOutputDir = "invalidDir";
        Convert convert = new Convert(0, "format", "aspectRatio", gui);
        assertTrue(convert.init());
    }
    
    // test if run method executes without throwing any exception
    @Test
    public void testRun() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        convert.run();
    }
    
    // test if Transcode method executes without throwing any exception
    @Test
    public void testTranscode() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        convert.Transcode();
    }
    
    // test if matrix method executes without throwing any exception
    @Test
    public void testMatrix() {
        Convert convert = new Convert("format", "aspectRatio", new GUI());
        convert.matrix();
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        boolean result = convert.init();
        assertFalse(result);
    }
    
    @Test
    public void testRun() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "4:3", gui);
        convert.run();
        // add assertion here
    }
    
    @Test
    public void testTranscode() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        convert.transcode();
        // add assertion here
    }
    
    @Test
    public void testMatrix() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "4:3", gui);
        convert.matrix();
        // add assertion here
    }
}
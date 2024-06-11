
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    
    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertTrue(convert.init());
        
        convert = new Convert("dvd-pal", "4:3", new GUI());
        assertTrue(convert.init());
    }
    
    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        
        convert = new Convert("dvd-pal", "4:3", new GUI());
        convert.Transcode();
    }
    
    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
    }
}

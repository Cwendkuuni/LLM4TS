import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertTrue(convert.init());
        
        Convert convert2 = new Convert(0, "dvd-pal", "4:3", new GUI());
        assertFalse(convert2.init());
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        
        Convert convert2 = new Convert(0, "dvd-pal", "4:3", new GUI());
        convert2.Transcode();
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
    }

}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    private Convert convert;
    private GUI dvdGui;
    
    @Before
    public void setUp() {
        dvdGui = new GUI();
        convert = new Convert("dvd-ntsc", "16:9", dvdGui);
    }
    
    @Test
    public void testInit() {
        assertTrue(convert.init());
    }
    
    @Test
    public void testTranscode() {
        convert.Transcode();
        // Add assertions here
    }
    
    @Test
    public void testMatrix() {
        convert.matrix();
        // Add assertions here
    }
}
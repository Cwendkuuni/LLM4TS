import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ConvertTest {
    
    private Convert convert;
    
    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "4:3", new GUI());
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
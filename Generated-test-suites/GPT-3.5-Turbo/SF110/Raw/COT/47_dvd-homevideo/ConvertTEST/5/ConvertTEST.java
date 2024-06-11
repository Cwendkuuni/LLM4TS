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
        assertFalse(convert.init());
    }
    
    @Test
    public void testRun() {
        convert.run();
    }
    
    @Test
    public void testTranscode() {
        convert.Transcode();
    }
    
    @Test
    public void testMatrix() {
        convert.matrix();
    }
}
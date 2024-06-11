import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {
    Convert convert;
    
    @Before
    public void setUp() {
        GUI DVD_GUI = new GUI();
        convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
    }
    
    @Test
    public void testInit() {
        assertTrue(convert.init());
    }
    
    @Test
    public void testRun() {
        convert.run();
        assertNull(convert.m_Thread);
    }
    
    @Test
    public void testTranscode() {
        convert.Transcode();
        assertFalse(convert.m_Error);
    }
    
    @Test
    public void testMatrix() {
        convert.matrix();
        // verify if the matrix.txt file is created
        // verify if the content of matrix.txt is correct
        // verify if the file is closed properly after writing
    }
}
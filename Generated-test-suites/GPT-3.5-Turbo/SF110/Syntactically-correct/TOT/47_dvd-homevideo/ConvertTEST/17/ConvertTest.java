import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConvertTest {
    private Convert convert;
    private GUI dvdGUI;
    
    @Before
    public void setUp() {
        dvdGUI = new GUI();
        convert = new Convert("dvd-ntsc", "16:9", dvdGUI);
    }
    
    @Test
    public void testInit_ThreadSelected() {
        dvdGUI.menuChkThread.setSelected(true);
        boolean result = convert.init();
        Assert.assertTrue(result);
    }
    
    @Test
    public void testInit_ThreadNotSelected() {
        dvdGUI.menuChkThread.setSelected(false);
        boolean result = convert.init();
        Assert.assertTrue(result);
    }
    
    @Test
    public void testRun() {
        convert.run();
        // Assert any necessary behavior
    }
    
    @Test
    public void testTranscode() {
        convert.Transcode();
        // Assert any necessary behavior
    }
    
    @Test
    public void testMatrix() {
        convert.matrix();
        // Assert any necessary behavior
    }
}
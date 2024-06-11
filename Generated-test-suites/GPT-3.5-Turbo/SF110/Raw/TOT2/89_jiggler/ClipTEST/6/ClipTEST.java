import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {
    
    // Test for int parameter constructor
    @Test
    public void testIntConstructor() {
        Clip clip = new Clip(10, 20);
        assertNotNull(clip);
    }
    
    // Test for float parameter constructor
    @Test
    public void testFloatConstructor() {
        Clip clip = new Clip(0.5f, 0.9f);
        assertNotNull(clip);
    }
    
    // Test apply method with GrayImage
    @Test
    public void testApplyGrayImage() {
        GrayImage image = new GrayImage();
        Clip clip = new Clip(10, 20);
        assertNotNull(clip.apply(image));
    }
    
    // Test apply method with RealGrayImage
    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new RealGrayImage();
        Clip clip = new Clip(0.5f, 0.9f);
        assertNotNull(clip.apply(image));
    }
    
    // Test apply method with ColorImage
    @Test
    public void testApplyColorImage() {
        ColorImage image = new ColorImage();
        Clip clip = new Clip(10, 20);
        assertNotNull(clip.apply(image));
    }
    
    // Test apply method with RealColorImage
    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new RealColorImage();
        Clip clip = new Clip(0.5f, 0.9f);
        assertNotNull(clip.apply(image));
    }
    
    // Test main method with valid input
    @Test
    public void testMainValidInput() {
        String[] argv = {"0.5", "0.9", "input.jpg", "output.jpg"};
        Clip.main(argv);
    }
    
    // Test main method with invalid input
    @Test
    public void testMainInvalidInput() {
        String[] argv = {"abc", "def", "input.jpg", "output.jpg"};
        Clip.main(argv);
    }
}
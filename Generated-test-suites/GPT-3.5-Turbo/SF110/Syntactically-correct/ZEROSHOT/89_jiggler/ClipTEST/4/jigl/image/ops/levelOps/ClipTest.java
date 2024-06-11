import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ROI;

public class ClipTest {
    
    private Clip clip;
    private Image image;
    private String inputfile;
    private Image image2;
    private String outputfile;
    
    @Before
    public void setUp() {
        clip = new Clip(0, 0);
        inputfile = "test.jpg";
        outputfile = "output.jpg";
        image = new GrayImage();
        image2 = new GrayImage();
    }
    
    @Test
    public void testApply_GrayImage() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        Image result = clip.apply((GrayImage) image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testApply_GrayImage_ROI() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        ROI roi = new ROI(0, 0, 2, 2);
        
        Image result = clip.apply((GrayImage) image, roi);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        Image result = clip.apply((RealGrayImage) image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealGrayImage_ROI() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        ROI roi = new ROI(0, 0, 2, 2);
        
        Image result = clip.apply((RealGrayImage) image, roi);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testApply_ColorImage() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        Image result = clip.apply((ColorImage) image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testApply_ColorImage_ROI() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        ROI roi = new ROI(0, 0, 2, 2);
        
        Image result = clip.apply((ColorImage) image, roi);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealColorImage() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        Image result = clip.apply((RealColorImage) image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testApply_RealColorImage_ROI() {
        // Test case 1
        image.set(0, 0, 100);
        image.set(1, 1, 200);
        image.set(2, 2, 300);
        
        ROI roi = new ROI(0, 0, 2, 2);
        
        Image result = clip.apply((RealColorImage) image, roi);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(200, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
    
    @Test
    public void testMain() {
        // Test case 1
        String[] argv = {"0.0", "1.0", inputfile, outputfile};
        
        Clip.main(argv);
        
        ImageInputStream is = new ImageInputStream(outputfile);
        Image result = is.read();
        is.close();
        
        assertEquals(0, result.get(0, 0));
        assertEquals(255, result.get(1, 1));
        assertEquals(255, result.get(2, 2));
    }
}
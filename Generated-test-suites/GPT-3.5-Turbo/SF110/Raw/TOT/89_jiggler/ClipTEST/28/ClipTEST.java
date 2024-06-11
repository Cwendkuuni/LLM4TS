import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.types.Image;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ROI;

public class ClipTest {
    
    @Test
    public void testApply_GrayImage() {
        //Arrange
        int min = 10;
        int max = 100;
        Clip clip = new Clip(min, max);
        GrayImage image = new GrayImage(100, 100);
        
        //Act
        Image result = clip.apply(image);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }
    
    @Test
    public void testApply_GrayImage_ROI() {
        //Arrange
        int min = 10;
        int max = 100;
        Clip clip = new Clip(min, max);
        GrayImage image = new GrayImage(100, 100);
        ROI roi = new ROI(0, 0, 50, 50);
        
        //Act
        Image result = clip.apply(image, roi);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof GrayImage);
    }
    
    @Test
    public void testApply_RealGrayImage() {
        //Arrange
        float min = 0.5f;
        float max = 1.0f;
        Clip clip = new Clip(min, max);
        RealGrayImage image = new RealGrayImage(100, 100);
        
        //Act
        Image result = clip.apply(image);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }
    
    @Test
    public void testApply_RealGrayImage_ROI() {
        //Arrange
        float min = 0.5f;
        float max = 1.0f;
        Clip clip = new Clip(min, max);
        RealGrayImage image = new RealGrayImage(100, 100);
        ROI roi = new ROI(0, 0, 50, 50);
        
        //Act
        Image result = clip.apply(image, roi);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof RealGrayImage);
    }
    
    @Test
    public void testApply_ColorImage() {
        //Arrange
        int min = 10;
        int max = 100;
        Clip clip = new Clip(min, max);
        ColorImage image = new ColorImage(100, 100);
        
        //Act
        Image result = clip.apply(image);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }
    
    @Test
    public void testApply_ColorImage_ROI() {
        //Arrange
        int min = 10;
        int max = 100;
        Clip clip = new Clip(min, max);
        ColorImage image = new ColorImage(100, 100);
        ROI roi = new ROI(0, 0, 50, 50);
        
        //Act
        Image result = clip.apply(image, roi);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof ColorImage);
    }
    
    @Test
    public void testApply_RealColorImage() {
        //Arrange
        float min = 0.5f;
        float max = 1.0f;
        Clip clip = new Clip(min, max);
        RealColorImage image = new RealColorImage(100, 100);
        
        //Act
        Image result = clip.apply(image);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }
    
    @Test
    public void testApply_RealColorImage_ROI() {
        //Arrange
        float min = 0.5f;
        float max = 1.0f;
        Clip clip = new Clip(min, max);
        RealColorImage image = new RealColorImage(100, 100);
        ROI roi = new ROI(0, 0, 50, 50);
        
        //Act
        Image result = clip.apply(image, roi);
        
        //Assert
        assertNotNull(result);
        assertTrue(result instanceof RealColorImage);
    }
}
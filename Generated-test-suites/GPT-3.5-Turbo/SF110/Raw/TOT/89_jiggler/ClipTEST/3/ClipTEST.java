
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {

    @Test
    public void apply_GrayImage_ReturnsGrayImageWithCorrectClipping() {
        // Arrange
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 5);
        image.set(2, 2, 15);
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void apply_GrayImageWithROI_ReturnsGrayImageWithCorrectClippingInROI() {
        // Arrange
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 5);
        image.set(2, 2, 15);
        ROI roi = new ROI(0, 0, 1, 1);
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image, roi);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(15, result.get(2, 2));
    }

    @Test
    public void apply_RealGrayImage_ReturnsRealGrayImageWithCorrectClipping() {
        // Arrange
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 5);
        image.set(2, 2, 15);
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void apply_RealGrayImageWithROI_ReturnsRealGrayImageWithCorrectClippingInROI() {
        // Arrange
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 10);
        image.set(1, 1, 5);
        image.set(2, 2, 15);
        ROI roi = new ROI(0, 0, 1, 1);
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image, roi);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(15, result.get(2, 2));
    }

    @Test
    public void apply_ColorImage_ReturnsColorImageWithCorrectClipping() {
        // Arrange
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, new GrayPixel(10));
        image.set(1, 1, new GrayPixel(5));
        image.set(2, 2, new GrayPixel(15));
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void apply_ColorImageWithROI_ReturnsColorImageWithCorrectClippingInROI() {
        // Arrange
        ColorImage image = new ColorImage(3, 3);
        image.set(0, 0, new GrayPixel(10));
        image.set(1, 1, new GrayPixel(5));
        image.set(2, 2, new GrayPixel(15));
        ROI roi = new ROI(0, 0, 1, 1);
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image, roi);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(15, result.get(2, 2));
    }

    @Test
    public void apply_RealColorImage_ReturnsRealColorImageWithCorrectClipping() {
        // Arrange
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, new RealGrayPixel(10));
        image.set(1, 1, new RealGrayPixel(5));
        image.set(2, 2, new RealGrayPixel(15));
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(10, result.get(2, 2));
    }

    @Test
    public void apply_RealColorImageWithROI_ReturnsRealColorImageWithCorrectClippingInROI() {
        // Arrange
        RealColorImage image = new RealColorImage(3, 3);
        image.set(0, 0, new RealGrayPixel(10));
        image.set(1, 1, new RealGrayPixel(5));
        image.set(2, 2, new RealGrayPixel(15));
        ROI roi = new ROI(0, 0, 1, 1);
        Clip clip = new Clip(0, 10);
        
        // Act
        Image result = clip.apply(image, roi);
        
        // Assert
        assertEquals(0, result.get(0, 0));
        assertEquals(5, result.get(1, 1));
        assertEquals(15, result.get(2, 2));
    }
}


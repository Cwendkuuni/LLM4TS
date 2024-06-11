import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClipTest {

    @Test
    void testApply_withGrayImage_shouldApplyClipToImage() {
        // Arrange
        int int_min = 10;
        int int_max = 20;
        GrayImage grayImage = new GrayImage(10, 10);
        
        // Act
        Clip clip = new Clip(int_min, int_max);
        Image result = clip.apply(grayImage);
        
        // Assert
        // Validate that the result image has been modified within the clipping range
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= int_min && value <= int_max);
            }
        }  
    }
    
    @Test
    void testApply_withGrayImageAndROI_shouldApplyClipToImageWithinROI() {
        // Arrange
        int int_min = 10;
        int int_max = 20;
        GrayImage grayImage = new GrayImage(10, 10);
        ROI roi = new ROI(3, 3, 6, 6);
        
        // Act
        Clip clip = new Clip(int_min, int_max);
        Image result = clip.apply(grayImage, roi);
        
        // Assert
        // Validate that the result image within the ROI has been modified within the clipping range
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                int value = result.get(x, y);
                assertTrue(value >= int_min && value <= int_max);
            }
        }  
    }
    
    @Test
    void testApply_withRealGrayImage_shouldApplyClipToImage() {
        // Arrange
        float float_min = 0.5f;
        float float_max = 0.8f;
        RealGrayImage realGrayImage = new RealGrayImage(10, 10);
        
        // Act
        Clip clip = new Clip(float_min, float_max);
        Image result = clip.apply(realGrayImage);
        
        // Assert
        // Validate that the result image has been modified within the clipping range
        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= float_min && value <= float_max);
            }
        }  
    }
    
    @Test
    void testApply_withRealGrayImageAndROI_shouldApplyClipToImageWithinROI() {
        // Arrange
        float float_min = 0.5f;
        float float_max = 0.8f;
        RealGrayImage realGrayImage = new RealGrayImage(10, 10);
        ROI roi = new ROI(3, 3, 6, 6);
        
        // Act
        Clip clip = new Clip(float_min, float_max);
        Image result = clip.apply(realGrayImage, roi);
        
        // Assert
        // Validate that the result image within the ROI has been modified within the clipping range
        for (int y = roi.uy(); y <= roi.ly(); y++) {
            for (int x = roi.ux(); x <= roi.lx(); x++) {
                float value = result.get(x, y);
                assertTrue(value >= float_min && value <= float_max);
            }
        }  
    }
    
    @Test
    void testApply_withColorImage_shouldApplyClipToImage() {
        // Arrange
        int int_min = 10;
        int int_max = 20;
        ColorImage colorImage = new ColorImage(10, 10);
        
        // Act
        Clip clip = new Clip(int_min, int_max);
        Image result = clip.apply(colorImage);
        
        // Assert
        // Validate that all planes of the result image have been modified within the clipping range
        for (int plane = 0; plane < result.planes(); plane++) {
            GrayImage planeImage = result.plane(plane);
            for (int y = 0; y < planeImage.Y(); y++) {
                for (int x = 0; x < planeImage.X(); x++) {
                    int value = planeImage.get(x, y);
                    assertTrue(value >= int_min && value <= int_max);
                }
            }
        }  
    }
    
    @Test
    void testApply_withColorImageAndROI_shouldApplyClipToImageWithinROI() {
        // Arrange
        int int_min = 10;
        int int_max = 20;
        ColorImage colorImage = new ColorImage(10, 10);
        ROI roi = new ROI(3, 3, 6, 6);
        
        // Act
        Clip clip = new Clip(int_min, int_max);
        Image result = clip.apply(colorImage, roi);
        
        // Assert
        // Validate that all planes of the result image within the ROI have been modified within the clipping range
        for (int plane = 0; plane < result.planes(); plane++) {
            GrayImage planeImage = result.plane(plane);
            for (int y = roi.uy(); y <= roi.ly(); y++) {
                for (int x = roi.ux(); x <= roi.lx(); x++) {
                    int value = planeImage.get(x, y);
                    assertTrue(value >= int_min && value <= int_max);
                }
            }
        }  
    }
    
    @Test
    void testApply_withRealColorImage_shouldApplyClipToImage() {
        // Arrange
        float float_min = 0.5f;
        float float_max = 0.8f;
        RealColorImage realColorImage = new RealColorImage(10, 10);
        
        // Act
        Clip clip = new Clip(float_min, float_max);
        Image result = clip.apply(realColorImage);
        
        // Assert
        // Validate that all planes of the result image have been modified within the clipping range
        for (int plane = 0; plane < result.planes(); plane++) {
            RealGrayImage planeImage = result.plane(plane);
            for (int y = 0; y < planeImage.Y(); y++) {
                for (int x = 0; x < planeImage.X(); x++) {
                    float value = planeImage.get(x, y);
                    assertTrue(value >= float_min && value <= float_max);
                }
            }
        }  
    }
    
    @Test
    void testApply_withRealColorImageAndROI_shouldApplyClipToImageWithinROI() {
        // Arrange
        float float_min = 0.5f;
        float float_max = 0.8f;
        RealColorImage realColorImage = new RealColorImage(10, 10);
        ROI roi = new ROI(3, 3, 6, 6);
        
        // Act
        Clip clip = new Clip(float_min, float_max);
        Image result = clip.apply(realColorImage, roi);
        
        // Assert
        // Validate that all planes of the result image within the ROI have been modified within the clipping range
        for (int plane = 0; plane < result.planes(); plane++) {
            RealGrayImage planeImage = result.plane(plane);
            for (int y = roi.uy(); y <= roi.ly(); y++) {
                for (int x = roi.ux(); x <= roi.lx(); x++) {
                    float value = planeImage.get(x, y);
                    assertTrue(value >= float_min && value <= float_max);
                }
            }
        }  
    }
}
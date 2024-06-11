import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;

public class ClipTest {

    private Clip clip;
    
    @Before
    public void setUp() {
        clip = new Clip(0, 255);
    }
    
    @Test
    public void testApply_GrayImage_Typical() {
        // Typical use case: all values within the range
        GrayImage image = new GrayImage(3, 3);
        image.set(0, 0, 100);
        image.set(0, 1, 150);
        image.set(0, 2, 200);
        image.set(1, 0, 50);
        image.set(1, 1, 100);
        image.set(1, 2, 150);
        image.set(2, 0, 200);
        image.set(2, 1, 250);
        image.set(2, 2, 300);
        
        GrayImage expectedImage = new GrayImage(3, 3);
        expectedImage.set(0, 0, 100);
        expectedImage.set(0, 1, 150);
        expectedImage.set(0, 2, 200);
        expectedImage.set(1, 0, 50);
        expectedImage.set(1, 1, 100);
        expectedImage.set(1, 2, 150);
        expectedImage.set(2, 0, 200);
        expectedImage.set(2, 1, 255);
        expectedImage.set(2, 2, 255);
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_GrayImage_Edge() {
        // Edge case: minimum integer value
        GrayImage image = new GrayImage(1, 1);
        image.set(0, 0, Integer.MIN_VALUE);
        
        GrayImage expectedImage = new GrayImage(1, 1);
        expectedImage.set(0, 0, 0);
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_GrayImage_Error() {
        // Error scenario: empty image
        GrayImage image = new GrayImage(0, 0);
        
        GrayImage expectedImage = new GrayImage(0, 0);
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_RealGrayImage_Typical() {
        // Typical use case: all values within the range
        RealGrayImage image = new RealGrayImage(3, 3);
        image.set(0, 0, 0.2f);
        image.set(0, 1, 0.5f);
        image.set(0, 2, 0.8f);
        image.set(1, 0, 0.1f);
        image.set(1, 1, 0.3f);
        image.set(1, 2, 0.6f);
        image.set(2, 0, 0.9f);
        image.set(2, 1, 1.2f);
        image.set(2, 2, 1.5f);
        
        RealGrayImage expectedImage = new RealGrayImage(3, 3);
        expectedImage.set(0, 0, 0.2f);
        expectedImage.set(0, 1, 0.5f);
        expectedImage.set(0, 2, 0.8f);
        expectedImage.set(1, 0, 0.1f);
        expectedImage.set(1, 1, 0.3f);
        expectedImage.set(1, 2, 0.6f);
        expectedImage.set(2, 0, 0.9f);
        expectedImage.set(2, 1, 1.2f);
        expectedImage.set(2, 2, 1.2f);
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_RealGrayImage_Edge() {
        // Edge case: minimum float value
        RealGrayImage image = new RealGrayImage(1, 1);
        image.set(0, 0, Float.MIN_VALUE);
        
        RealGrayImage expectedImage = new RealGrayImage(1, 1);
        expectedImage.set(0, 0, 0.0f);
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_RealGrayImage_Error() {
        // Error scenario: null image
        RealGrayImage image = null;
        
        RealGrayImage expectedImage = null;
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_ColorImage_Typical() {
        // Typical use case: all values within the range
        ColorImage image = new ColorImage(3, 3);
        GrayImage plane0 = new GrayImage(3, 3);
        plane0.set(0, 0, 100);
        plane0.set(0, 1, 150);
        plane0.set(0, 2, 200);
        plane0.set(1, 0, 50);
        plane0.set(1, 1, 100);
        plane0.set(1, 2, 150);
        plane0.set(2, 0, 200);
        plane0.set(2, 1, 250);
        plane0.set(2, 2, 300);
        GrayImage plane1 = new GrayImage(3, 3);
        plane1.set(0, 0, 200);
        plane1.set(0, 1, 250);
        plane1.set(0, 2, 300);
        plane1.set(1, 0, 100);
        plane1.set(1, 1, 150);
        plane1.set(1, 2, 200);
        plane1.set(2, 0, 150);
        plane1.set(2, 1, 200);
        plane1.set(2, 2, 250);
        GrayImage plane2 = new GrayImage(3, 3);
        plane2.set(0, 0, 150);
        plane2.set(0, 1, 200);
        plane2.set(0, 2, 250);
        plane2.set(1, 0, 300);
        plane2.set(1, 1, 255);
        plane2.set(1, 2, 200);
        plane2.set(2, 0, 100);
        plane2.set(2, 1, 50);
        plane2.set(2, 2, 0);
        image.setPlane(0, plane0);
        image.setPlane(1, plane1);
        image.setPlane(2, plane2);
        
        ColorImage expectedImage = new ColorImage(3, 3);
        GrayImage expectedPlane0 = new GrayImage(3, 3);
        expectedPlane0.set(0, 0, 100);
        expectedPlane0.set(0, 1, 150);
        expectedPlane0.set(0, 2, 200);
        expectedPlane0.set(1, 0, 50);
        expectedPlane0.set(1, 1, 100);
        expectedPlane0.set(1, 2, 150);
        expectedPlane0.set(2, 0, 200);
        expectedPlane0.set(2, 1, 255);
        expectedPlane0.set(2, 2, 255);
        GrayImage expectedPlane1 = new GrayImage(3, 3);
        expectedPlane1.set(0, 0, 200);
        expectedPlane1.set(0, 1, 255);
        expectedPlane1.set(0, 2, 255);
        expectedPlane1.set(1, 0, 100);
        expectedPlane1.set(1, 1, 150);
        expectedPlane1.set(1, 2, 200);
        expectedPlane1.set(2, 0, 150);
        expectedPlane1.set(2, 1, 200);
        expectedPlane1.set(2, 2, 250);
        GrayImage expectedPlane2 = new GrayImage(3, 3);
        expectedPlane2.set(0, 0, 150);
        expectedPlane2.set(0, 1, 200);
        expectedPlane2.set(0, 2, 250);
        expectedPlane2.set(1, 0, 255);
        expectedPlane2.set(1, 1, 255);
        expectedPlane2.set(1, 2, 200);
        expectedPlane2.set(2, 0, 100);
        expectedPlane2.set(2, 1, 50);
        expectedPlane2.set(2, 2, 0);
        expectedImage.setPlane(0, expectedPlane0);
        expectedImage.setPlane(1, expectedPlane1);
        expectedImage.setPlane(2, expectedPlane2);
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_ColorImage_Edge() {
        // Edge case: maximum integer value in planes
        ColorImage image = new ColorImage(1, 1);
        GrayImage plane0 = new GrayImage(1, 1);
        plane0.set(0, 0, Integer.MAX_VALUE);
        GrayImage plane1 = new GrayImage(1, 1);
        plane1.set(0, 0, Integer.MAX_VALUE);
        GrayImage plane2 = new GrayImage(1, 1);
        plane2.set(0, 0, Integer.MAX_VALUE);
        image.setPlane(0, plane0);
        image.setPlane(1, plane1);
        image.setPlane(2, plane2);
        
        ColorImage expectedImage = new ColorImage(1, 1);
        GrayImage expectedPlane0 = new GrayImage(1, 1);
        expectedPlane0.set(0, 0, 255);
        GrayImage expectedPlane1 = new GrayImage(1, 1);
        expectedPlane1.set(0, 0, 255);
        GrayImage expectedPlane2 = new GrayImage(1, 1);
        expectedPlane2.set(0, 0, 255);
        expectedImage.setPlane(0, expectedPlane0);
        expectedImage.setPlane(1, expectedPlane1);
        expectedImage.setPlane(2, expectedPlane2);
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
    
    @Test
    public void testApply_ColorImage_Error() {
        // Error scenario: null image
        ColorImage image = null;
        
        ColorImage expectedImage = null;
        
        clip.apply(image);
        
        Assert.assertEquals(expectedImage, image);
    }
}
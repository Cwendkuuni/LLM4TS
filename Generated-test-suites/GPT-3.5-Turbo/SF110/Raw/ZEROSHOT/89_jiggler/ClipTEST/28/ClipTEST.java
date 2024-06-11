package jigl.image.ops.levelOps;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClipTest {
    private Clip clip;
    private Clip clipInt;
    private Clip clipFloat;
    private Image inputImage;
    private ROI inputROI;
    
    @Before
    public void setUp() {
        clipInt = new Clip(0, 255);
        clipFloat = new Clip(0.0f, 1.0f);
        
        GrayImage grayImage = new GrayImage(3, 3);
        grayImage.set(0, 0, 0);
        grayImage.set(1, 0, 128);
        grayImage.set(2, 0, 255);
        grayImage.set(0, 1, 0);
        grayImage.set(1, 1, 128);
        grayImage.set(2, 1, 255);
        grayImage.set(0, 2, 0);
        grayImage.set(1, 2, 128);
        grayImage.set(2, 2, 255);
        
        inputImage = grayImage;
        
        inputROI = new ROI(0, 0, inputImage.X() - 1, inputImage.Y() - 1);
    }
    
    @Test
    public void testApply_grayImage_withoutROI() {
        Image resultImage = clipInt.apply((GrayImage) inputImage);
        assertSame("should return same input image", inputImage, resultImage);
        assertEquals("should clip values less than int_min to int_min", 0, ((GrayImage) resultImage).get(0, 0));
        assertEquals("should clip values greater than int_max to int_max", 255, ((GrayImage) resultImage).get(2, 2));
    }
    
    @Test
    public void testApply_grayImage_withROI() {
        Image resultImage = clipInt.apply((GrayImage) inputImage, inputROI);
        assertSame("should return same input image", inputImage, resultImage);
        assertEquals("should clip values less than int_min to int_min", 0, ((GrayImage) resultImage).get(0, 0));
        assertEquals("should clip values greater than int_max to int_max", 255, ((GrayImage) resultImage).get(2, 2));
    }
    
    @Test
    public void testApply_realGrayImage_withoutROI() {
        RealGrayImage realGrayImage = inputImage.realGray();
        
        Image resultImage = clipFloat.apply(realGrayImage);
        assertSame("should return same input image", realGrayImage, resultImage);
        assertEquals("should clip values less than float_min to float_min", 0.0f, ((RealGrayImage) resultImage).get(0, 0), 0.0f);
        assertEquals("should clip values greater than float_max to float_max", 1.0f, ((RealGrayImage) resultImage).get(2, 2), 0.0f);
    }
    
    @Test
    public void testApply_realGrayImage_withROI() {
        RealGrayImage realGrayImage = inputImage.realGray();
        
        Image resultImage = clipFloat.apply(realGrayImage, inputROI);
        assertSame("should return same input image", realGrayImage, resultImage);
        assertEquals("should clip values less than float_min to float_min", 0.0f, ((RealGrayImage) resultImage).get(0, 0), 0.0f);
        assertEquals("should clip values greater than float_max to float_max", 1.0f, ((RealGrayImage) resultImage).get(2, 2), 0.0f);
    }
    
    @Test
    public void testApply_colorImage_withoutROI() {
        ColorImage colorImage = new ColorImage(3, 3);
        
        colorImage.setPlane(0, (GrayImage) inputImage);
        colorImage.setPlane(1, (GrayImage) inputImage);
        colorImage.setPlane(2, (GrayImage) inputImage);
        
        Image resultImage = clipInt.apply(colorImage);
        assertSame("should return same input image", colorImage, resultImage);
        assertEquals("should clip values less than int_min to int_min", 0, ((ColorImage) resultImage).plane(0).get(0, 0));
        assertEquals("should clip values greater than int_max to int_max", 255, ((ColorImage) resultImage).plane(2).get(2, 2));
    }
    
    @Test
    public void testApply_colorImage_withROI() {
        ColorImage colorImage = new ColorImage(3, 3);
        
        colorImage.setPlane(0, (GrayImage) inputImage);
        colorImage.setPlane(1, (GrayImage) inputImage);
        colorImage.setPlane(2, (GrayImage) inputImage);
        
        Image resultImage = clipInt.apply(colorImage, inputROI);
        assertSame("should return same input image", colorImage, resultImage);
        assertEquals("should clip values less than int_min to int_min", 0, ((ColorImage) resultImage).plane(0).get(0, 0));
        assertEquals("should clip values greater than int_max to int_max", 255, ((ColorImage) resultImage).plane(2).get(2, 2));
    }
    
    @Test
    public void testApply_realColorImage_withoutROI() {
        RealColorImage realColorImage = new RealColorImage(3, 3);
        
        realColorImage.setPlane(0, (RealGrayImage) inputImage);
        realColorImage.setPlane(1, (RealGrayImage) inputImage);
        realColorImage.setPlane(2, (RealGrayImage) inputImage);
        
        Image resultImage = clipFloat.apply(realColorImage);
        assertSame("should return same input image", realColorImage, resultImage);
        assertEquals("should clip values less than float_min to float_min", 0.0f, ((RealColorImage) resultImage).plane(0).get(0, 0), 0.0f);
        assertEquals("should clip values greater than float_max to float_max", 1.0f, ((RealColorImage) resultImage).plane(2).get(2, 2), 0.0f);
    }
    
    @Test
    public void testApply_realColorImage_withROI() {
        RealColorImage realColorImage = new RealColorImage(3, 3);
        
        realColorImage.setPlane(0, (RealGrayImage) inputImage);
        realColorImage.setPlane(1, (RealGrayImage) inputImage);
        realColorImage.setPlane(2, (RealGrayImage) inputImage);
        
        Image resultImage = clipFloat.apply(realColorImage, inputROI);
        assertSame("should return same input image", realColorImage, resultImage);
        assertEquals("should clip values less than float_min to float_min", 0.0f, ((RealColorImage) resultImage).plane(0).get(0, 0), 0.0f);
        assertEquals("should clip values greater than float_max to float_max", 1.0f, ((RealColorImage) resultImage).plane(2).get(2, 2), 0.0f);
    }
}
package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import org.junit.Test;

public class ShiftTest {
    @Test
    public void testApplyWithWrap_GrayImage() {
        GrayImage inputImage = new GrayImage(10, 10);
        inputImage.set(5, 5, 50);
        
        Shift shift = new Shift(2, 2, 1);
        GrayImage outputImage = shift.apply(inputImage);
        
        assertEquals(50, outputImage.get(7, 7));
    }
    
    @Test
    public void testApplyWithoutWrap_GrayImage() {
        GrayImage inputImage = new GrayImage(10, 10);
        inputImage.set(15, 15, 50);
        
        Shift shift = new Shift(2, 2, 0);
        GrayImage outputImage = shift.apply(inputImage);
        
        assertEquals(0, outputImage.get(15, 15));
    }
    
    @Test
    public void testApplyWithWrap_RealGrayImage() {
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        inputImage.set(5, 5, 0.5f);
        
        Shift shift = new Shift(2, 2, 1);
        RealGrayImage outputImage = shift.apply(inputImage);
        
        assertEquals(0.5f, outputImage.get(7, 7), 0.01f);
    }
    
    @Test
    public void testApplyWithoutWrap_RealGrayImage() {
        RealGrayImage inputImage = new RealGrayImage(10, 10);
        inputImage.set(15, 15, 0.5f);
        
        Shift shift = new Shift(2, 2, 0);
        RealGrayImage outputImage = shift.apply(inputImage);
        
        assertEquals(0.0f, outputImage.get(15, 15), 0.01f);
    }
    
    @Test
    public void testApplyWithWrap_ColorImage() {
        ColorImage inputImage = new ColorImage(10, 10);
        inputImage.set(5, 5, 1, 0, 0);
        
        Shift shift = new Shift(2, 2, 1);
        ColorImage outputImage = shift.apply(inputImage);
        
        assertEquals(1, outputImage.get(7, 7, 0));
    }
    
    @Test
    public void testApplyWithoutWrap_ColorImage() {
        ColorImage inputImage = new ColorImage(10, 10);
        inputImage.set(15, 15, 1, 0, 0);
        
        Shift shift = new Shift(2, 2, 0);
        ColorImage outputImage = shift.apply(inputImage);
        
        assertEquals(0, outputImage.get(15, 15, 0));
    }
    
    @Test
    public void testApplyWithWrap_RealColorImage() {
        RealColorImage inputImage = new RealColorImage(10, 10);
        inputImage.set(5, 5, 0.5f, 0.0f, 0.0f);
        
        Shift shift = new Shift(2, 2, 1);
        RealColorImage outputImage = shift.apply(inputImage);
        
        assertEquals(0.5f, outputImage.get(7, 7, 0), 0.01f);
    }
    
    @Test
    public void testApplyWithoutWrap_RealColorImage() {
        RealColorImage inputImage = new RealColorImage(10, 10);
        inputImage.set(15, 15, 0.5f, 0.0f, 0.0f);
        
        Shift shift = new Shift(2, 2, 0);
        RealColorImage outputImage = shift.apply(inputImage);
        
        assertEquals(0.0f, outputImage.get(15, 15, 0), 0.01f);
    }
}
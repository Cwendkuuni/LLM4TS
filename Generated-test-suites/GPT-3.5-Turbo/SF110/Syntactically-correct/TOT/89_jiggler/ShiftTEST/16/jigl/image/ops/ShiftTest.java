import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.ops.Shift;
import jigl.image.types.*;

public class ShiftTest {

    @Test
    public void testApply_GrayImageWrap() {
        GrayImage inputImage = new GrayImage(5, 5);
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                inputImage.set(x, y, 10);
            }
        }
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        GrayImage expectedImage = new GrayImage(5, 5);
        
        expectedImage.set(2, 2, 10);
        expectedImage.set(3, 2, 10);
        expectedImage.set(4, 2, 10);
        expectedImage.set(0, 3, 10);
        expectedImage.set(1, 3, 10);
        expectedImage.set(2, 3, 10);
        
        GrayImage resultImage = shift.apply(inputImage);
        
        assertEquals(expectedImage, resultImage);
    }
    
    @Test
    public void testApply_GrayImageNoWrap() {
        GrayImage inputImage = new GrayImage(5, 5);
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                inputImage.set(x, y, 10);
            }
        }
        
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        GrayImage expectedImage = new GrayImage(5, 5);
        
        expectedImage.set(2, 2, 10);
        expectedImage.set(3, 3, 10);
        expectedImage.set(4, 4, 10);
        
        GrayImage resultImage = shift.apply(inputImage);
        
        assertEquals(expectedImage, resultImage);
    }
    
    @Test
    public void testApply_RealGrayImageWrap() {
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                inputImage.set(x, y, 10.0f);
            }
        }
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealGrayImage expectedImage = new RealGrayImage(5, 5);
        
        expectedImage.set(2, 2, 10.0f);
        expectedImage.set(3, 2, 10.0f);
        expectedImage.set(4, 2, 10.0f);
        expectedImage.set(0, 3, 10.0f);
        expectedImage.set(1, 3, 10.0f);
        expectedImage.set(2, 3, 10.0f);
        
        RealGrayImage resultImage = shift.apply(inputImage);
        
        assertEquals(expectedImage, resultImage);
    }
    
    @Test
    public void testApply_RealGrayImageNoWrap() {
        RealGrayImage inputImage = new RealGrayImage(5, 5);
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                inputImage.set(x, y, 10.0f);
            }
        }
        
        Shift shift = new Shift(2, 2, Shift.NOWRAP);
        RealGrayImage expectedImage = new RealGrayImage(5, 5);
        
        expectedImage.set(2, 2, 10.0f);
        expectedImage.set(3, 3, 10.0f);
        expectedImage.set(4, 4, 10.0f);
        
        RealGrayImage resultImage = shift.apply(inputImage);
        
        assertEquals(expectedImage, resultImage);
    }
    
    @Test
    public void testApply_ColorImageWrap() {
        ColorImage inputImage = new ColorImage(5, 5);
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                inputImage.set(x, y, 10, 20, 30);
            }
        }
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        ColorImage expectedImage = new ColorImage(5, 5);
        
        expectedImage.set(2, 2, 10, 20, 30);
        expectedImage.set(3, 2, 10, 20, 30);
        expectedImage.set(4, 2, 10, 20, 30);
        expectedImage.set(0, 3, 10, 20, 30);
        expectedImage.set(1, 3, 10, 20, 30);
        expectedImage.set(2, 3, 10, 20, 30);
        
        ColorImage resultImage = shift.apply(inputImage);
        
        assertEquals(expectedImage, resultImage);
    }
    
    @Test
    public void testApply_RealColorImageWrap() {
        RealColorImage inputImage = new RealColorImage(5, 5);
        for (int y = 0; y < inputImage.Y(); y++) {
            for (int x = 0; x < inputImage.X(); x++) {
                inputImage.set(x, y, 10.0f, 20.0f, 30.0f);
            }
        }
        
        Shift shift = new Shift(2, 2, Shift.WRAP);
        RealColorImage expectedImage = new RealColorImage(5, 5);
        
        expectedImage.set(2, 2, 10.0f, 20.0f, 30.0f);
        expectedImage.set(3, 2, 10.0f, 20.0f, 30.0f);
        expectedImage.set(4, 2, 10.0f, 20.0f, 30.0f);
        expectedImage.set(0, 3, 10.0f, 20.0f, 30.0f);
        expectedImage.set(1, 3, 10.0f, 20.0f, 30.0f);
        expectedImage.set(2, 3, 10.0f, 20.0f, 30.0f);
        
        RealColorImage resultImage = shift.apply(inputImage);
        
        assertEquals(expectedImage, resultImage);
    }
    
}
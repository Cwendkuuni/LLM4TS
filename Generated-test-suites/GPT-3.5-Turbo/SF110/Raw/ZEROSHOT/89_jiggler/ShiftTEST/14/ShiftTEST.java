import static org.junit.Assert.*;
import org.junit.Test;

public class ShiftTest {
  
  @Test
  public void testApply_GrayImage_wrap() {
    // Create input image
    GrayImage inputImage = new GrayImage(3, 3);
    inputImage.set(0, 0, 1);
    inputImage.set(1, 1, 2);
    inputImage.set(2, 2, 3);
    
    // Create expected output image
    GrayImage expectedOutput = new GrayImage(3, 3);
    expectedOutput.set(2, 2, 1);
    expectedOutput.set(0, 0, 2);
    expectedOutput.set(1, 1, 3);
    
    // Apply shift
    Shift shift = new Shift(1, 1, Shift.WRAP);
    GrayImage outputImage = shift.apply(inputImage);
    
    // Compare output with expected output
    assertEquals(expectedOutput, outputImage);
  }
  
  @Test
  public void testApply_GrayImage_nowrap() {
    // Create input image
    GrayImage inputImage = new GrayImage(3, 3);
    inputImage.set(0, 0, 1);
    inputImage.set(1, 1, 2);
    inputImage.set(2, 2, 3);
    
    // Create expected output image
    GrayImage expectedOutput = new GrayImage(3, 3);
    expectedOutput.set(1, 1, 1);
    expectedOutput.set(2, 2, 2);
    
    // Apply shift
    Shift shift = new Shift(2, 2, Shift.NOWRAP);
    GrayImage outputImage = shift.apply(inputImage);
    
    // Compare output with expected output
    assertEquals(expectedOutput, outputImage);
  }
  
  @Test
  public void testApply_RealGrayImage_wrap() {
    // Create input image
    RealGrayImage inputImage = new RealGrayImage(3, 3);
    inputImage.set(0, 0, 1.0f);
    inputImage.set(1, 1, 2.0f);
    inputImage.set(2, 2, 3.0f);
    
    // Create expected output image
    RealGrayImage expectedOutput = new RealGrayImage(3, 3);
    expectedOutput.set(2, 2, 1.0f);
    expectedOutput.set(0, 0, 2.0f);
    expectedOutput.set(1, 1, 3.0f);
    
    // Apply shift
    Shift shift = new Shift(1, 1, Shift.WRAP);
    RealGrayImage outputImage = shift.apply(inputImage);
    
    // Compare output with expected output
    assertEquals(expectedOutput, outputImage);
  }
  
  @Test
  public void testApply_RealGrayImage_nowrap() {
    // Create input image
    RealGrayImage inputImage = new RealGrayImage(3, 3);
    inputImage.set(0, 0, 1.0f);
    inputImage.set(1, 1, 2.0f);
    inputImage.set(2, 2, 3.0f);
    
    // Create expected output image
    RealGrayImage expectedOutput = new RealGrayImage(3, 3);
    expectedOutput.set(1, 1, 1.0f);
    expectedOutput.set(2, 2, 2.0f);
    
    // Apply shift
    Shift shift = new Shift(2, 2, Shift.NOWRAP);
    RealGrayImage outputImage = shift.apply(inputImage);
    
    // Compare output with expected output
    assertEquals(expectedOutput, outputImage);
  }
  
  @Test
  public void testApply_ColorImage() {
    // Create input image
    ColorImage inputImage = new ColorImage(3, 3);
    inputImage.setPlane(0, new GrayImage(3, 3));
    inputImage.setPlane(1, new GrayImage(3, 3));
    inputImage.setPlane(2, new GrayImage(3, 3));
    inputImage.getPlane(0).set(0, 0, 1);
    inputImage.getPlane(1).set(1, 1, 2);
    inputImage.getPlane(2).set(2, 2, 3);
    
    // Create expected output image
    ColorImage expectedOutput = new ColorImage(3, 3);
    expectedOutput.setPlane(0, new GrayImage(3, 3));
    expectedOutput.setPlane(1, new GrayImage(3, 3));
    expectedOutput.setPlane(2, new GrayImage(3, 3));
    expectedOutput.getPlane(0).set(2, 2, 1);
    expectedOutput.getPlane(1).set(0, 0, 2);
    expectedOutput.getPlane(2).set(1, 1, 3);
    
    // Apply shift
    Shift shift = new Shift(1, 1, Shift.WRAP);
    ColorImage outputImage = shift.apply(inputImage);
    
    // Compare output with expected output
    assertEquals(expectedOutput, outputImage);
  }
  
  @Test
  public void testApply_RealColorImage() {
    // Create input image
    RealColorImage inputImage = new RealColorImage(3, 3);
    inputImage.setPlane(0, new RealGrayImage(3, 3));
    inputImage.setPlane(1, new RealGrayImage(3, 3));
    inputImage.setPlane(2, new RealGrayImage(3, 3));
    inputImage.getPlane(0).set(0, 0, 1.0f);
    inputImage.getPlane(1).set(1, 1, 2.0f);
    inputImage.getPlane(2).set(2, 2, 3.0f);
    
    // Create expected output image
    RealColorImage expectedOutput = new RealColorImage(3, 3);
    expectedOutput.setPlane(0, new RealGrayImage(3, 3));
    expectedOutput.setPlane(1, new RealGrayImage(3, 3));
    expectedOutput.setPlane(2, new RealGrayImage(3, 3));
    expectedOutput.getPlane(0).set(2, 2, 1.0f);
    expectedOutput.getPlane(1).set(0, 0, 2.0f);
    expectedOutput.getPlane(2).set(1, 1, 3.0f);
    
    // Apply shift
    Shift shift = new Shift(1, 1, Shift.WRAP);
    RealColorImage outputImage = shift.apply(inputImage);
    
    // Compare output with expected output
    assertEquals(expectedOutput, outputImage);
  }
}
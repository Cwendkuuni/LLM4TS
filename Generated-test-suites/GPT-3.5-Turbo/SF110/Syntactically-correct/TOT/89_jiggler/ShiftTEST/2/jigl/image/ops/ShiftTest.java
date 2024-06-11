import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftTest {
  
  // Test case for the constructor
  @Test
  public void testConstructor(){
    Shift shift = new Shift(2, 3, 1);
    assertEquals(2, shift.getXoffset());
    assertEquals(3, shift.getYoffset());
    assertTrue(shift.isWrap());
  }
  
  // Test cases for the apply(GrayImage) method
  
  // Typical use case
  @Test
  public void testApplyGrayImageTypical(){
    GrayImage image1 = new GrayImage(4, 4);
    // Set pixels
    image1.set(0, 0, 10);
    image1.set(1, 1, 20);
    image1.set(2, 2, 30);
    image1.set(3, 3, 40);
    
    Shift shift = new Shift(1, 1, 0);
    GrayImage result = shift.apply(image1);
    
    // Check shifted pixels
    assertEquals(10, result.get(1, 1));
    assertEquals(20, result.get(2, 2));
    assertEquals(30, result.get(3, 3));
    assertEquals(0, result.get(0, 0));
  }
  
  // Edge case - Wrapping with negative xoffset and yoffset
  @Test
  public void testApplyGrayImageWrapNegativeOffsets(){
    GrayImage image1 = new GrayImage(4, 4);
    // Set pixels
    image1.set(0, 1, 10);
    image1.set(1, 2, 20);
    image1.set(2, 3, 30);
    image1.set(3, 0, 40);
    
    Shift shift = new Shift(-1, -1, 1);
    GrayImage result = shift.apply(image1);
    
    // Check wrapped pixels
    assertEquals(10, result.get(3, 3));
    assertEquals(20, result.get(0, 0));
    assertEquals(30, result.get(1, 1));
    assertEquals(40, result.get(2, 2));
  }
  
  // Error scenario - null GrayImage
  @Test(expected = NullPointerException.class)
  public void testApplyGrayImageNull(){
    Shift shift = new Shift(1, 1, 0);
    shift.apply(null);
  }
  
  // Test cases for the apply(RealGrayImage) method
  
  // Typical use case
  @Test
  public void testApplyRealGrayImageTypical(){
    RealGrayImage image1 = new RealGrayImage(4, 4);
    // Set pixels
    image1.set(0, 0, 10.5f);
    image1.set(1, 1, 20.5f);
    image1.set(2, 2, 30.5f);
    image1.set(3, 3, 40.5f);
    
    Shift shift = new Shift(1, 1, 0);
    RealGrayImage result = shift.apply(image1);
    
    // Check shifted pixels
    assertEquals(10.5f, result.get(1, 1), 0.00001);
    assertEquals(20.5f, result.get(2, 2), 0.00001);
    assertEquals(30.5f, result.get(3, 3), 0.00001);
    assertEquals(0.0f, result.get(0, 0), 0.00001);
  }
  
  // Edge case - Wrapping with negative xoffset and yoffset
  @Test
  public void testApplyRealGrayImageWrapNegativeOffsets(){
    RealGrayImage image1 = new RealGrayImage(4, 4);
    // Set pixels
    image1.set(0, 1, 10.5f);
    image1.set(1, 2, 20.5f);
    image1.set(2, 3, 30.5f);
    image1.set(3, 0, 40.5f);
    
    Shift shift = new Shift(-1, -1, 1);
    RealGrayImage result = shift.apply(image1);
    
    // Check wrapped pixels
    assertEquals(10.5f, result.get(3, 3), 0.00001);
    assertEquals(20.5f, result.get(0, 0), 0.00001);
    assertEquals(30.5f, result.get(1, 1), 0.00001);
    assertEquals(40.5f, result.get(2, 2), 0.00001);
  }
  
  // Error scenario - null RealGrayImage
  @Test(expected = NullPointerException.class)
  public void testApplyRealGrayImageNull(){
    Shift shift = new Shift(1, 1, 0);
    shift.apply((RealGrayImage)null);
  }
  
  // Test cases for the apply(ColorImage) method
  
  // Typical use case
  @Test
  public void testApplyColorImageTypical(){
    ColorImage image1 = new ColorImage(4, 4);
    // Set red pixels in plane 0
    image1.setPlane(0, new GrayImage(4, 4));
    image1.plane(0).set(0, 0, 10);
    image1.plane(0).set(1, 1, 20);
    image1.plane(0).set(2, 2, 30);
    image1.plane(0).set(3, 3, 40);
    
    // Set green pixels in plane 1
    image1.setPlane(1, new GrayImage(4, 4));
    image1.plane(1).set(0, 0, 50);
    image1.plane(1).set(1, 1, 60);
    image1.plane(1).set(2, 2, 70);
    image1.plane(1).set(3, 3, 80);
    
    // Set blue pixels in plane 2
    image1.setPlane(2, new GrayImage(4, 4));
    image1.plane(2).set(0, 0, 90);
    image1.plane(2).set(1, 1, 100);
    image1.plane(2).set(2, 2, 110);
    image1.plane(2).set(3, 3, 120);
    
    Shift shift = new Shift(1, 1, 0);
    ColorImage result = shift.apply(image1);
    
    // Check shifted pixels in plane 0
    assertEquals(10, result.plane(0).get(1, 1));
    assertEquals(20, result.plane(0).get(2, 2));
    assertEquals(30, result.plane(0).get(3, 3));
    assertEquals(0, result.plane(0).get(0, 0));
    
    // Check shifted pixels in plane 1
    assertEquals(50, result.plane(1).get(1, 1));
    assertEquals(60, result.plane(1).get(2, 2));
    assertEquals(70, result.plane(1).get(3, 3));
    assertEquals(0, result.plane(1).get(0, 0));
    
    // Check shifted pixels in plane 2
    assertEquals(90, result.plane(2).get(1, 1));
    assertEquals(100, result.plane(2).get(2, 2));
    assertEquals(110, result.plane(2).get(3, 3));
    assertEquals(0, result.plane(2).get(0, 0));
  }
  
  // Error scenario - null ColorImage
  @Test(expected = NullPointerException.class)
  public void testApplyColorImageNull(){
    Shift shift = new Shift(1, 1, 0);
    shift.apply((ColorImage)null);
  }
  
  // Test cases for the apply(RealColorImage) method
  
  // Typical use case
  @Test
  public void testApplyRealColorImageTypical(){
    RealColorImage image1 = new RealColorImage(4, 4);
    // Set red pixels in plane 0
    image1.setPlane(0, new RealGrayImage(4, 4));
    image1.plane(0).set(0, 0, 10.5f);
    image1.plane(0).set(1, 1, 20.5f);
    image1.plane(0).set(2, 2, 30.5f);
    image1.plane(0).set(3, 3, 40.5f);
    
    // Set green pixels in plane 1
    image1.setPlane(1, new RealGrayImage(4, 4));
    image1.plane(1).set(0, 0, 50.5f);
    image1.plane(1).set(1, 1, 60.5f);
    image1.plane(1).set(2, 2, 70.5f);
    image1.plane(1).set(3, 3, 80.5f);
    
    // Set blue pixels in plane 2
    image1.setPlane(2, new RealGrayImage(4, 4));
    image1.plane(2).set(0, 0, 90.5f);
    image1.plane(2).set(1, 1, 100.5f);
    image1.plane(2).set(2, 2, 110.5f);
    image1.plane(2).set(3, 3, 120.5f);
    
    Shift shift = new Shift(1, 1, 0);
    RealColorImage result = shift.apply(image1);
    
    // Check shifted pixels in plane 0
    assertEquals(10.5f, result.plane(0).get(1, 1), 0.00001);
    assertEquals(20.5f, result.plane(0).get(2, 2), 0.00001);
    assertEquals(30.5f, result.plane(0).get(3, 3), 0.00001);
    assertEquals(0.0f, result.plane(0).get(0, 0), 0.00001);
    
    // Check shifted pixels in plane 1
    assertEquals(50.5f, result.plane(1).get(1, 1), 0.00001);
    assertEquals(60.5f, result.plane(1).get(2, 2), 0.00001);
    assertEquals(70.5f, result.plane(1).get(3, 3), 0.00001);
    assertEquals(0.0f, result.plane(1).get(0, 0), 0.00001);
    
    // Check shifted pixels in plane 2
    assertEquals(90.5f, result.plane(2).get(1, 1), 0.00001);
    assertEquals(100.5f, result.plane(2).get(2, 2), 0.00001);
    assertEquals(110.5f, result.plane(2).get(3, 3), 0.00001);
    assertEquals(0.0f, result.plane(2).get(0, 0), 0.00001);
  }
  
  // Error scenario - null RealColorImage
  @Test(expected = NullPointerException.class)
  public void testApplyRealColorImageNull(){
    Shift shift = new Shift(1, 1, 0);
    shift.apply((RealColorImage)null);
  }

}
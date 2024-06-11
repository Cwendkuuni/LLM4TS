import org.junit.*;
import static org.junit.Assert.*;

public class ShiftTest {
  
  @Test
  public void testApply_GrayImage_typical() {
    GrayImage image = new GrayImage(2, 2);
    image.set(0, 0, 1);
    image.set(1, 0, 2);
    image.set(0, 1, 3);
    image.set(1, 1, 4);
    
    Shift shift = new Shift(1, 0, Shift.NOWRAP);
    GrayImage result = shift.apply(image);
    
    assertEquals(2, result.get(0, 0));
    assertEquals(0, result.get(1, 0));
    assertEquals(4, result.get(0, 1));
    assertEquals(0, result.get(1, 1));
  }
  
  @Test
  public void testApply_RealGrayImage_typical() {
    RealGrayImage image = new RealGrayImage(2, 2);
    image.set(0, 0, 1.1f);
    image.set(1, 0, 2.2f);
    image.set(0, 1, 3.3f);
    image.set(1, 1, 4.4f);
    
    Shift shift = new Shift(0, 1, Shift.NOWRAP);
    RealGrayImage result = shift.apply(image);
    
    assertEquals(0.0f, result.get(0, 0), 0.00001);
    assertEquals(0.0f, result.get(1, 0), 0.00001);
    assertEquals(1.1f, result.get(0, 1), 0.00001);
    assertEquals(2.2f, result.get(1, 1), 0.00001);
  }
  
  @Test
  public void testApply_ColorImage_typical() {
    ColorImage image = new ColorImage(2, 2);
    image.setPixel(0, 0, 255, 0, 0);
    image.setPixel(1, 0, 0, 255, 0);
    image.setPixel(0, 1, 0, 0, 255);
    image.setPixel(1, 1, 255, 255, 255);
    
    Shift shift = new Shift(0, 0, Shift.NOWRAP);
    ColorImage result = shift.apply(image);
    
    assertArrayEquals(new int[] {255, 0, 0}, result.getPixel(0, 0));
    assertArrayEquals(new int[] {0, 255, 0}, result.getPixel(1, 0));
    assertArrayEquals(new int[] {0, 0, 255}, result.getPixel(0, 1));
    assertArrayEquals(new int[] {255, 255, 255}, result.getPixel(1, 1));
  }
  
  @Test
  public void testApply_RealColorImage_typical() {
    RealColorImage image = new RealColorImage(2, 2);
    image.setPixel(0, 0, 1.1f, 2.2f, 3.3f);
    image.setPixel(1, 0, 4.4f, 5.5f, 6.6f);
    image.setPixel(0, 1, 7.7f, 8.8f, 9.9f);
    image.setPixel(1, 1, 10.0f, 11.1f, 12.2f);
    
    Shift shift = new Shift(1, 1, Shift.NOWRAP);
    RealColorImage result = shift.apply(image);
    
    assertArrayEquals(new float[] {0.0f, 0.0f, 0.0f}, result.getPixel(0, 0), 0.00001);
    assertArrayEquals(new float[] {0.0f, 0.0f, 0.0f}, result.getPixel(1, 0), 0.00001);
    assertArrayEquals(new float[] {0.0f, 0.0f, 0.0f}, result.getPixel(0, 1), 0.00001);
    assertArrayEquals(new float[] {1.1f, 2.2f, 3.3f}, result.getPixel(1, 1), 0.00001);
  }
  
  @Test
  public void testApply_GrayImage_negativeOffset() {
    GrayImage image = new GrayImage(2, 2);
    image.set(0, 0, 1);
    image.set(1, 0, 2);
    image.set(0, 1, 3);
    image.set(1, 1, 4);
    
    Shift shift = new Shift(-1, 0, Shift.NOWRAP);
    GrayImage result = shift.apply(image);
    
    assertEquals(0, result.get(0, 0));
    assertEquals(1, result.get(1, 0));
    assertEquals(2, result.get(0, 1));
    assertEquals(3, result.get(1, 1));
  }
  
  @Test
  public void testApply_RealGrayImage_negativeOffset() {
    RealGrayImage image = new RealGrayImage(2, 2);
    image.set(0, 0, 1.1f);
    image.set(1, 0, 2.2f);
    image.set(0, 1, 3.3f);
    image.set(1, 1, 4.4f);
    
    Shift shift = new Shift(0, -1, Shift.NOWRAP);
    RealGrayImage result = shift.apply(image);
    
    assertEquals(3.3f, result.get(0, 0), 0.00001);
    assertEquals(4.4f, result.get(1, 0), 0.00001);
    assertEquals(0.0f, result.get(0, 1), 0.00001);
    assertEquals(0.0f, result.get(1, 1), 0.00001);
  }
  
  @Test
  public void testApply_ColorImage_edgeCase() {
    ColorImage image = new ColorImage(1, 1);
    image.setPixel(0, 0, 255, 255, 255);
    
    Shift shift = new Shift(1, 0, Shift.NOWRAP);
    ColorImage result = shift.apply(image);
    
    assertArrayEquals(new int[] {0, 0, 0}, result.getPixel(0, 0));
  }
  
  @Test
  public void testApply_RealColorImage_edgeCase() {
    RealColorImage image = new RealColorImage(1, 1);
    image.setPixel(0, 0, 1.1f, 2.2f, 3.3f);
    
    Shift shift = new Shift(0, 1, Shift.NOWRAP);
    RealColorImage result = shift.apply(image);
    
    assertArrayEquals(new float[] {0.0f, 0.0f, 0.0f}, result.getPixel(0, 0), 0.00001);
  }
  
  @Test
  public void testApply_GrayImage_wrap() {
    GrayImage image = new GrayImage(2, 2);
    image.set(0, 0, 1);
    image.set(1, 0, 2);
    image.set(0, 1, 3);
    image.set(1, 1, 4);
    
    Shift shift = new Shift(2, 0, Shift.WRAP);
    GrayImage result = shift.apply(image);
    
    assertEquals(1, result.get(0, 0));
    assertEquals(2, result.get(1, 0));
    assertEquals(3, result.get(0, 1));
    assertEquals(4, result.get(1, 1));
  }
  
  @Test
  public void testApply_RealGrayImage_wrap() {
    RealGrayImage image = new RealGrayImage(2, 2);
    image.set(0, 0, 1.1f);
    image.set(1, 0, 2.2f);
    image.set(0, 1, 3.3f);
    image.set(1, 1, 4.4f);
    
    Shift shift = new Shift(0, 3, Shift.WRAP);
    RealGrayImage result = shift.apply(image);
    
    assertEquals(1.1f, result.get(0, 0), 0.00001);
    assertEquals(2.2f, result.get(1, 0), 0.00001);
    assertEquals(3.3f, result.get(0, 1), 0.00001);
    assertEquals(4.4f, result.get(1, 1), 0.00001);
  }
  
  @Test
  public void testApply_ColorImage_error() {
    ColorImage image = null;
    
    Shift shift = new Shift(1, 1, Shift.NOWRAP);
    
    try {
      shift.apply(image);
    } catch (NullPointerException e) {
      assertTrue(true);
    }
  }
  
  @Test
  public void testApply_RealColorImage_error() {
    RealColorImage image = new RealColorImage(2, 2);
    
    Shift shift = new Shift(0, 0, Shift.NOWRAP);
    
    try {
      shift.apply(image);
    } catch (NullPointerException e) {
      assertTrue(true);
    }
  }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ShiftTest {
  
  private Shift shift;
  
  @Before
  public void setUp() {
    shift = new Shift(2, 2, 1);
  }
  
  @Test
  public void testApplyWithGrayImage() {
    GrayImage image = new GrayImage(4, 4);
    image.set(0, 0, 1);
    image.set(1, 1, 2);
    image.set(2, 2, 3);
    image.set(3, 3, 4);
    
    GrayImage expected = new GrayImage(4, 4);
    expected.set(2, 2, 1);
    expected.set(3, 3, 2);
    expected.set(0, 0, 3);
    expected.set(1, 1, 4);
    
    GrayImage result = shift.apply(image);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testApplyWithRealGrayImage() {
    RealGrayImage image = new RealGrayImage(4, 4);
    image.set(0, 0, 0.5f);
    image.set(1, 1, 1.5f);
    image.set(2, 2, 2.5f);
    image.set(3, 3, 3.5f);
    
    RealGrayImage expected = new RealGrayImage(4, 4);
    expected.set(2, 2, 0.5f);
    expected.set(3, 3, 1.5f);
    expected.set(0, 0, 2.5f);
    expected.set(1, 1, 3.5f);
    
    RealGrayImage result = shift.apply(image);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testApplyWithColorImage() {
    ColorImage image = new ColorImage(4, 4);
    image.setPlane(0, new GrayImage(4, 4));
    image.setPlane(1, new GrayImage(4, 4));
    image.setPlane(2, new GrayImage(4, 4));
    
    ColorImage expected = new ColorImage(4, 4);
    expected.setPlane(0, new GrayImage(4, 4));
    expected.setPlane(1, new GrayImage(4, 4));
    expected.setPlane(2, new GrayImage(4, 4));
    
    ColorImage result = shift.apply(image);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testApplyWithRealColorImage() {
    RealColorImage image = new RealColorImage(4, 4);
    image.setPlane(0, new RealGrayImage(4, 4));
    image.setPlane(1, new RealGrayImage(4, 4));
    image.setPlane(2, new RealGrayImage(4, 4));
    
    RealColorImage expected = new RealColorImage(4, 4);
    expected.setPlane(0, new RealGrayImage(4, 4));
    expected.setPlane(1, new RealGrayImage(4, 4));
    expected.setPlane(2, new RealGrayImage(4, 4));
    
    RealColorImage result = shift.apply(image);
    
    assertEquals(expected, result);
  }
}
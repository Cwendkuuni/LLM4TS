import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClipTest {
  
  private Clip clip;
  
  @Before
  public void setUp() {
    clip = new Clip(0, 255);
  }
  
  @Test
  public void testApply_GrayImage() {
    GrayImage image = new GrayImage(3, 3);
    image.set(0, 0, 100);
    image.set(1, 1, 200);
    image.set(2, 2, 300);
    
    GrayImage result = (GrayImage)clip.apply(image);
    
    assertEquals(0, result.get(0, 0));
    assertEquals(200, result.get(1, 1));
    assertEquals(255, result.get(2, 2));
  }
  
  @Test
  public void testApply_GrayImage_ROI() {
    GrayImage image = new GrayImage(3, 3);
    image.set(0, 0, 100);
    image.set(1, 1, 200);
    image.set(2, 2, 300);
    
    ROI roi = new ROI(1, 1, 2, 2);
    
    GrayImage result = (GrayImage)clip.apply(image, roi);
    
    assertEquals(100, result.get(0, 0));
    assertEquals(200, result.get(1, 1));
    assertEquals(255, result.get(2, 2));
  }
  
  @Test
  public void testApply_RealGrayImage() {
    RealGrayImage image = new RealGrayImage(3, 3);
    image.set(0, 0, 0.5f);
    image.set(1, 1, 0.75f);
    image.set(2, 2, 1.5f);
    
    RealGrayImage result = (RealGrayImage)clip.apply(image);
    
    assertEquals(0.0f, result.get(0, 0), 0.001f);
    assertEquals(0.75f, result.get(1, 1), 0.001f);
    assertEquals(1.0f, result.get(2, 2), 0.001f);
  }
  
  @Test
  public void testApply_RealGrayImage_ROI() {
    RealGrayImage image = new RealGrayImage(3, 3);
    image.set(0, 0, 0.5f);
    image.set(1, 1, 0.75f);
    image.set(2, 2, 1.5f);
    
    ROI roi = new ROI(1, 1, 2, 2);
    
    RealGrayImage result = (RealGrayImage)clip.apply(image, roi);
    
    assertEquals(0.5f, result.get(0, 0), 0.001f);
    assertEquals(0.75f, result.get(1, 1), 0.001f);
    assertEquals(1.0f, result.get(2, 2), 0.001f);
  }
  
  @Test
  public void testApply_ColorImage() {
    ColorImage image = new ColorImage(3, 3);
    image.set(0, 0, 0, 100);
    image.set(1, 1, 128, 200);
    image.set(2, 2, 255, 300);
    
    ColorImage result = (ColorImage)clip.apply(image);
    
    assertEquals(0, result.get(0, 0).get(0));
    assertEquals(100, result.get(0, 0).get(1));
    assertEquals(128, result.get(1, 1).get(0));
    assertEquals(200, result.get(1, 1).get(1));
    assertEquals(255, result.get(2, 2).get(0));
    assertEquals(255, result.get(2, 2).get(1));
  }
  
  @Test
  public void testApply_ColorImage_ROI() {
    ColorImage image = new ColorImage(3, 3);
    image.set(0, 0, 0, 100);
    image.set(1, 1, 128, 200);
    image.set(2, 2, 255, 300);
    
    ROI roi = new ROI(1, 1, 2, 2);
    
    ColorImage result = (ColorImage)clip.apply(image, roi);
    
    assertEquals(0, result.get(0, 0).get(0));
    assertEquals(100, result.get(0, 0).get(1));
    assertEquals(128, result.get(1, 1).get(0));
    assertEquals(200, result.get(1, 1).get(1));
    assertEquals(255, result.get(2, 2).get(0));
    assertEquals(255, result.get(2, 2).get(1));
  }
  
  @Test
  public void testApply_RealColorImage() {
    RealColorImage image = new RealColorImage(3, 3);
    image.set(0, 0, 0, 0.5f);
    image.set(1, 1, 0.5f, 0.75f);
    image.set(2, 2, 1.0f, 1.5f);
    
    RealColorImage result = (RealColorImage)clip.apply(image);
    
    assertEquals(0.0f, result.get(0, 0).get(0), 0.001f);
    assertEquals(0.5f, result.get(0, 0).get(1), 0.001f);
    assertEquals(0.5f, result.get(1, 1).get(0), 0.001f);
    assertEquals(0.75f, result.get(1, 1).get(1), 0.001f);
    assertEquals(1.0f, result.get(2, 2).get(0), 0.001f);
    assertEquals(1.0f, result.get(2, 2).get(1), 0.001f);
  }
  
  @Test
  public void testApply_RealColorImage_ROI() {
    RealColorImage image = new RealColorImage(3, 3);
    image.set(0, 0, 0, 0.5f);
    image.set(1, 1, 0.5f, 0.75f);
    image.set(2, 2, 1.0f, 1.5f);
    
    ROI roi = new ROI(1, 1, 2, 2);
    
    RealColorImage result = (RealColorImage)clip.apply(image, roi);
    
    assertEquals(0.0f, result.get(0, 0).get(0), 0.001f);
    assertEquals(0.5f, result.get(0, 0).get(1), 0.001f);
    assertEquals(0.5f, result.get(1, 1).get(0), 0.001f);
    assertEquals(0.75f, result.get(1, 1).get(1), 0.001f);
    assertEquals(1.0f, result.get(2, 2).get(0), 0.001f);
    assertEquals(1.0f, result.get(2, 2).get(1), 0.001f);
  }
}
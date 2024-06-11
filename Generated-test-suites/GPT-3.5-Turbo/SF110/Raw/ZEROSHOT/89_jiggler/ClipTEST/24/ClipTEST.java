import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;

public class ClipTest {
  
  @Test
  public void testApply_GrayImage_ReturnsGrayImage() {
    Clip clip = new Clip(0, 255);
    GrayImage image = new ByteGrayImage(10, 10);
    Image result = clip.apply(image);
    assertTrue(result instanceof GrayImage);
  }

  @Test
  public void testApply_GrayImageWithROI_ReturnsGrayImage() {
    Clip clip = new Clip(0, 255);
    GrayImage image = new ByteGrayImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    assertTrue(result instanceof GrayImage);
  }

  @Test
  public void testApply_RealGrayImage_ReturnsRealGrayImage() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealGrayImage image = new FloatGrayImage(10, 10);
    Image result = clip.apply(image);
    assertTrue(result instanceof RealGrayImage);
  }

  @Test
  public void testApply_RealGrayImageWithROI_ReturnsRealGrayImage() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealGrayImage image = new FloatGrayImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    assertTrue(result instanceof RealGrayImage);
  }

  @Test
  public void testApply_ColorImage_ReturnsColorImage() {
    Clip clip = new Clip(0, 255);
    ColorImage image = new RGBImage(10, 10);
    Image result = clip.apply(image);
    assertTrue(result instanceof ColorImage);
  }

  @Test
  public void testApply_ColorImageWithROI_ReturnsColorImage() {
    Clip clip = new Clip(0, 255);
    ColorImage image = new RGBImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    assertTrue(result instanceof ColorImage);
  }

  @Test
  public void testApply_RealColorImage_ReturnsRealColorImage() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealColorImage image = new RealRGBImage(10, 10);
    Image result = clip.apply(image);
    assertTrue(result instanceof RealColorImage);
  }

  @Test
  public void testApply_RealColorImageWithROI_ReturnsRealColorImage() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealColorImage image = new RealRGBImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    assertTrue(result instanceof RealColorImage);
  }
}
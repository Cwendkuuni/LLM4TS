import org.junit.Test;
import org.junit.Assert;

public class ClipTest {

  @Test
  public void testApplyGrayImage() {
    Clip clip = new Clip(0, 255);
    GrayImage image = new GrayImage(10, 10);
    Image result = clip.apply(image);
    Assert.assertNotNull(result);
  }

  @Test
  public void testApplyGrayImageWithROI() {
    Clip clip = new Clip(0, 255);
    GrayImage image = new GrayImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    Assert.assertNotNull(result);
  }

  @Test
  public void testApplyRealGrayImage() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealGrayImage image = new RealGrayImage(10, 10);
    Image result = clip.apply(image);
    Assert.assertNotNull(result);
  }

  @Test
  public void testApplyRealGrayImageWithROI() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealGrayImage image = new RealGrayImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    Assert.assertNotNull(result);
  }

  @Test
  public void testApplyColorImage() {
    Clip clip = new Clip(0, 255);
    ColorImage image = new ColorImage(10, 10);
    Image result = clip.apply(image);
    Assert.assertNotNull(result);
  }

  @Test
  public void testApplyColorImageWithROI() {
    Clip clip = new Clip(0, 255);
    ColorImage image = new ColorImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    Assert.assertNotNull(result);
  }

  @Test
  public void testApplyRealColorImage() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealColorImage image = new RealColorImage(10, 10);
    Image result = clip.apply(image);
    Assert.assertNotNull(result);
  }

  @Test
  public void testApplyRealColorImageWithROI() {
    Clip clip = new Clip(0.0f, 1.0f);
    RealColorImage image = new RealColorImage(10, 10);
    ROI roi = new ROI(0, 0, 9, 9);
    Image result = clip.apply(image, roi);
    Assert.assertNotNull(result);
  }

}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.*;
import jigl.image.io.*;
import jigl.image.types.*;
import jigl.image.ops.levelOps.*;

public class ClipTest {

  private Clip clip;

  @Before
  public void setUp() {
    clip = new Clip(0, 255);
  }

  @Test
  public void testApply_GrayImage_WithoutROI() {
    GrayImage image = new GrayImage(3, 3);
    image.set(0, 0, 100);
    image.set(1, 1, 200);
    image.set(2, 2, 300);

    Image resultImage = clip.apply(image);

    assertEquals(0, resultImage.get(0, 0));
    assertEquals(200, resultImage.get(1, 1));
    assertEquals(255, resultImage.get(2, 2));
  }

  @Test
  public void testApply_GrayImage_WithROI() {
    GrayImage image = new GrayImage(3, 3);
    image.set(0, 0, 100);
    image.set(1, 1, 200);
    image.set(2, 2, 300);

    ROI roi = new ROI(1, 1, 1, 1);

    Image resultImage = clip.apply(image, roi);

    assertEquals(100, resultImage.get(0, 0));
    assertEquals(200, resultImage.get(1, 1));
    assertEquals(200, resultImage.get(2, 2));
  }

  @Test
  public void testApply_RealGrayImage_WithoutROI() {
    RealGrayImage image = new RealGrayImage(3, 3);
    image.set(0, 0, 100.5f);
    image.set(1, 1, 200.7f);
    image.set(2, 2, 300.3f);

    Image resultImage = clip.apply(image);

    assertEquals(0.0f, resultImage.get(0, 0));
    assertEquals(200.7f, resultImage.get(1, 1));
    assertEquals(255.0f, resultImage.get(2, 2));
  }

  @Test
  public void testApply_RealGrayImage_WithROI() {
    RealGrayImage image = new RealGrayImage(3, 3);
    image.set(0, 0, 100.5f);
    image.set(1, 1, 200.7f);
    image.set(2, 2, 300.3f);

    ROI roi = new ROI(1, 1, 1, 1);

    Image resultImage = clip.apply(image, roi);

    assertEquals(100.5f, resultImage.get(0, 0));
    assertEquals(200.7f, resultImage.get(1, 1));
    assertEquals(200.7f, resultImage.get(2, 2));
  }

  @Test
  public void testApply_ColorImage_WithoutROI() {
    ColorImage image = new ColorImage(3, 3);
    GrayImage plane0 = new GrayImage(3, 3);
    GrayImage plane1 = new GrayImage(3, 3);
    GrayImage plane2 = new GrayImage(3, 3);
    plane0.set(0, 0, 100);
    plane1.set(1, 1, 200);
    plane2.set(2, 2, 300);
    image.setPlane(0, plane0);
    image.setPlane(1, plane1);
    image.setPlane(2, plane2);

    Image resultImage = clip.apply(image);

    assertEquals(0, resultImage.getPlane(0).get(0, 0));
    assertEquals(200, resultImage.getPlane(1).get(1, 1));
    assertEquals(255, resultImage.getPlane(2).get(2, 2));
  }

  @Test
  public void testApply_ColorImage_WithROI() {
    ColorImage image = new ColorImage(3, 3);
    GrayImage plane0 = new GrayImage(3, 3);
    GrayImage plane1 = new GrayImage(3, 3);
    GrayImage plane2 = new GrayImage(3, 3);
    plane0.set(0, 0, 100);
    plane1.set(1, 1, 200);
    plane2.set(2, 2, 300);
    image.setPlane(0, plane0);
    image.setPlane(1, plane1);
    image.setPlane(2, plane2);

    ROI roi = new ROI(1, 1, 1, 1);

    Image resultImage = clip.apply(image, roi);

    assertEquals(100, resultImage.getPlane(0).get(0, 0));
    assertEquals(200, resultImage.getPlane(1).get(1, 1));
    assertEquals(200, resultImage.getPlane(2).get(2, 2));
  }

  @Test
  public void testApply_RealColorImage_WithoutROI() {
    RealColorImage image = new RealColorImage(3, 3);
    RealGrayImage plane0 = new RealGrayImage(3, 3);
    RealGrayImage plane1 = new RealGrayImage(3, 3);
    RealGrayImage plane2 = new RealGrayImage(3, 3);
    plane0.set(0, 0, 100.5f);
    plane1.set(1, 1, 200.7f);
    plane2.set(2, 2, 300.3f);
    image.setPlane(0, plane0);
    image.setPlane(1, plane1);
    image.setPlane(2, plane2);

    Image resultImage = clip.apply(image);

    assertEquals(0.0f, resultImage.getPlane(0).get(0, 0));
    assertEquals(200.7f, resultImage.getPlane(1).get(1, 1));
    assertEquals(255.0f, resultImage.getPlane(2).get(2, 2));
  }

  @Test
  public void testApply_RealColorImage_WithROI() {
    RealColorImage image = new RealColorImage(3, 3);
    RealGrayImage plane0 = new RealGrayImage(3, 3);
    RealGrayImage plane1 = new RealGrayImage(3, 3);
    RealGrayImage plane2 = new RealGrayImage(3, 3);
    plane0.set(0, 0, 100.5f);
    plane1.set(1, 1, 200.7f);
    plane2.set(2, 2, 300.3f);
    image.setPlane(0, plane0);
    image.setPlane(1, plane1);
    image.setPlane(2, plane2);

    ROI roi = new ROI(1, 1, 1, 1);

    Image resultImage = clip.apply(image, roi);

    assertEquals(100.5f, resultImage.getPlane(0).get(0, 0));
    assertEquals(200.7f, resultImage.getPlane(1).get(1, 1));
    assertEquals(200.7f, resultImage.getPlane(2).get(2, 2));
  }

}
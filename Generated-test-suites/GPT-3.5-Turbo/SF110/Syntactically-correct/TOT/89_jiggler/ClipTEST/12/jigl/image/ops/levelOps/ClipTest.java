import static org.junit.Assert.*;
import org.junit.Test;

public class ClipTest {

  @Test
  public void testApply_GrayImage() {
    GrayImage image = new GrayImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, 15);

    Clip clip = new Clip(5, 20);

    Image result = clip.apply(image);

    assertEquals(20, result.get(2, 2));
    assertEquals(5, result.get(1, 1)); // edge case
    assertEquals(15, result.get(2, 3));
    assertEquals(10, result.get(0, 0)); // edge case
  }

  @Test
  public void testApply_GrayImage_ROI() {
    GrayImage image = new GrayImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, 15);

    Clip clip = new Clip(5, 20);
    ROI roi = new ROI(1, 1, 4, 4);

    Image result = clip.apply(image, roi);

    assertEquals(20, result.get(2, 2)); // pixel inside ROI
    assertEquals(10, result.get(0, 0)); // pixel outside ROI
    assertEquals(10, result.get(3, 3)); // pixel on the border of ROI
  }

  @Test
  public void testApply_RealGrayImage() {
    RealGrayImage image = new RealGrayImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, 15.5f);

    Clip clip = new Clip(5.5f, 20.5f);

    Image result = clip.apply(image);

    assertEquals(20.5f, result.get(2, 2));
    assertEquals(5.5f, result.get(1, 1)); // edge case
    assertEquals(15.5f, result.get(2, 3));
    assertEquals(10.0f, result.get(0, 0)); // edge case
  }

  @Test
  public void testApply_RealGrayImage_ROI() {
    RealGrayImage image = new RealGrayImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, 15.5f);

    Clip clip = new Clip(5.5f, 20.5f);
    ROI roi = new ROI(1, 1, 4, 4);

    Image result = clip.apply(image, roi);

    assertEquals(20.5f, result.get(2, 2)); // pixel inside ROI
    assertEquals(10.0f, result.get(0, 0)); // pixel outside ROI
    assertEquals(10.0f, result.get(3, 3)); // pixel on the border of ROI
  }

  @Test
  public void testApply_ColorImage() {
    ColorImage image = new ColorImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, new int[] {15, 15, 15});

    Clip clip = new Clip(5, 20);

    Image result = clip.apply(image);

    assertArrayEquals(new int[] {20, 20, 20}, result.get(2, 2));
    assertArrayEquals(new int[] {5, 5, 5}, result.get(1, 1)); // edge case
    assertArrayEquals(new int[] {15, 15, 15}, result.get(2, 3));
    assertArrayEquals(new int[] {10, 10, 10}, result.get(0, 0)); // edge case
  }

  @Test
  public void testApply_ColorImage_ROI() {
    ColorImage image = new ColorImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, new int[] {15, 15, 15});

    Clip clip = new Clip(5, 20);
    ROI roi = new ROI(1, 1, 4, 4);

    Image result = clip.apply(image, roi);

    assertArrayEquals(new int[] {20, 20, 20}, result.get(2, 2)); // pixel inside ROI
    assertArrayEquals(new int[] {10, 10, 10}, result.get(0, 0)); // pixel outside ROI
    assertArrayEquals(new int[] {10, 10, 10}, result.get(3, 3)); // pixel on the border of ROI
  }

  @Test
  public void testApply_RealColorImage() {
    RealColorImage image = new RealColorImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, new float[] {15.5f, 15.5f, 15.5f});

    Clip clip = new Clip(5.5f, 20.5f);

    Image result = clip.apply(image);

    assertArrayEquals(new float[] {20.5f, 20.5f, 20.5f}, result.get(2, 2), 0);
    assertArrayEquals(new float[] {5.5f, 5.5f, 5.5f}, result.get(1, 1), 0); // edge case
    assertArrayEquals(new float[] {15.5f, 15.5f, 15.5f}, result.get(2, 3), 0);
    assertArrayEquals(new float[] {10.0f, 10.0f, 10.0f}, result.get(0, 0), 0); // edge case
  }

  @Test
  public void testApply_RealColorImage_ROI() {
    RealColorImage image = new RealColorImage(5, 5);
    image.initImage(10); // Initialize all pixels to 10
    image.set(2, 2, new float[] {15.5f, 15.5f, 15.5f});

    Clip clip = new Clip(5.5f, 20.5f);
    ROI roi = new ROI(1, 1, 4, 4);

    Image result = clip.apply(image, roi);

    assertArrayEquals(new float[] {20.5f, 20.5f, 20.5f}, result.get(2, 2), 0); // pixel inside ROI
    assertArrayEquals(new float[] {10.0f, 10.0f, 10.0f}, result.get(0, 0), 0); // pixel outside ROI
    assertArrayEquals(new float[] {10.0f, 10.0f, 10.0f}, result.get(3, 3), 0); // pixel on the border of ROI
  }
}
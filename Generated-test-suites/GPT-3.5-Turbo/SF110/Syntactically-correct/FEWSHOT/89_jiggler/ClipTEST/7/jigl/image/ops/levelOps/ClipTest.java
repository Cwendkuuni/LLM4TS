
package jigl.image.ops.levelOps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import jigl.image.*;
import jigl.image.io.*;
import jigl.image.types.*;
import jigl.image.ops.levelOps.*;

public class ClipTest {
  
  private GrayImage grayImage;
  private RealGrayImage realGrayImage;
  private ColorImage colorImage;
  private RealColorImage realColorImage;

  @Before
  public void setUp() {
    grayImage = new GrayImage(10, 10);
    realGrayImage = new RealGrayImage(10, 10);
    colorImage = new ColorImage(10, 10);
    realColorImage = new RealColorImage(10, 10);
  }

  @Test
  public void testClipWithGrayImage() {
    Clip clip = new Clip(0, 255);
    Image result = clip.apply(grayImage);
    
    assertEquals(grayImage, result);
  }
  
  @Test
  public void testClipWithGrayImageAndROI() {
    Clip clip = new Clip(0, 255);
    ROI roi = new ROI(2, 2, 6, 6);
    Image result = clip.apply(grayImage, roi);
    
    assertEquals(grayImage, result);
  }

  @Test
  public void testClipWithRealGrayImage() {
    Clip clip = new Clip(0.0f, 255.0f);
    Image result = clip.apply(realGrayImage);
    
    assertEquals(realGrayImage, result);
  }
  
  @Test
  public void testClipWithRealGrayImageAndROI() {
    Clip clip = new Clip(0.0f, 255.0f);
    ROI roi = new ROI(2, 2, 6, 6);
    Image result = clip.apply(realGrayImage, roi);
    
    assertEquals(realGrayImage, result);
  }

  @Test
  public void testClipWithColorImage() {
    Clip clip = new Clip(0, 255);
    Image result = clip.apply(colorImage);
    
    assertEquals(colorImage, result);
  }
  
  @Test
  public void testClipWithColorImageAndROI() {
    Clip clip = new Clip(0, 255);
    ROI roi = new ROI(2, 2, 6, 6);
    Image result = clip.apply(colorImage, roi);
    
    assertEquals(colorImage, result);
  }

  @Test
  public void testClipWitRealColorImage() {
    Clip clip = new Clip(0.0f, 255.0f);
    Image result = clip.apply(realColorImage);
    
    assertEquals(realColorImage, result);
  }
  
  @Test
  public void testClipWitRealColorImageAndROI() {
    Clip clip = new Clip(0.0f, 255.0f);
    ROI roi = new ROI(2, 2, 6, 6);
    Image result = clip.apply(realColorImage, roi);
    
    assertEquals(realColorImage, result);
  }
}

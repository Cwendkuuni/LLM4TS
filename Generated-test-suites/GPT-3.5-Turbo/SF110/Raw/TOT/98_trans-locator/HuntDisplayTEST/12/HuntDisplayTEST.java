import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import static org.junit.Assert.*;

public class HuntDisplayTest {
  
  private HuntDisplay huntDisplay;
  
  @Before
  public void setUp() {
    huntDisplay = new HuntDisplay();
  }
  
  @Test
  public void testAddPoint() {
    TrigPoint point = new TrigPoint(45, 90, 0, false);
    huntDisplay.addPoint(point);
    
    assertEquals(1, huntDisplay.points.size());
    assertEquals(point.getY() / 90.0, huntDisplay.points.elementAt(0).getY(), 0.001);
    assertEquals(point.getX() / 180.0, huntDisplay.points.elementAt(0).getX(), 0.001);
    assertEquals(point.getTheta(), huntDisplay.points.elementAt(0).getTheta(), 0.001);
    assertEquals(point.isFox(), huntDisplay.points.elementAt(0).isFox());
  }
  
  @Test
  public void testAddLandMark() {
    Landmark landmark = new Landmark(45, 90, new ImageIcon());
    huntDisplay.addLandMark(landmark);
    
    assertEquals(1, huntDisplay.landmarks.size());
    assertEquals(landmark.getLat() / 90.0, huntDisplay.landmarks.elementAt(0).getLat(), 0.001);
    assertEquals(landmark.getLon() / 180.0, huntDisplay.landmarks.elementAt(0).getLon(), 0.001);
    assertEquals(landmark.getIcon(), huntDisplay.landmarks.elementAt(0).getIcon());
  }
  
  @Test
  public void testGetPreferredSize() {
    Dimension expected = new Dimension(200, 200);
    assertEquals(expected, huntDisplay.getPreferredSize());
  }
  
  @Test
  public void testGetMinimumSize() {
    Dimension expected = new Dimension(50, 50);
    assertEquals(expected, huntDisplay.getMinimumSize());
  }
  
  @Test
  public void testPaintComponent_noPointsOrLandmarks() {
    JFrame frame = new JFrame();
    frame.setSize(400, 400);
    BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
    huntDisplay.paintComponent(image.getGraphics());
    
    // The image should be completely filled with the background color
    for (int i = 0; i < frame.getWidth(); i++) {
      for (int j = 0; j < frame.getHeight(); j++) {
        assertEquals(huntDisplay.getBackground(), new Color(image.getRGB(i, j)));
      }
    }
  }
  
  @Test
  public void testPaintComponent_withPointsOnly() {
    JFrame frame = new JFrame();
    frame.setSize(400, 400);
    
    TrigPoint point1 = new TrigPoint(0.25, 0.25, 0, false);
    TrigPoint point2 = new TrigPoint(0.75, 0.75, 0, true);
    huntDisplay.addPoint(point1);
    huntDisplay.addPoint(point2);
    
    BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
    huntDisplay.paintComponent(image.getGraphics());
    
    // Point 1 should be drawn as black
    int expectedColor1 = Color.BLACK.getRGB();
    int actualColor1 = new Color(image.getRGB((int)(0.25 * frame.getWidth()), (int)(0.25 * frame.getHeight()))).getRGB();
    assertEquals(expectedColor1, actualColor1);
    
    // Point 2 should be drawn as green
    int expectedColor2 = Color.GREEN.getRGB();
    int actualColor2 = new Color(image.getRGB((int)(0.75 * frame.getWidth()), (int)(0.75 * frame.getHeight()))).getRGB();
    assertEquals(expectedColor2, actualColor2);
  }
  
  @Test
  public void testPaintComponent_withLandmarksOnly() {
    JFrame frame = new JFrame();
    frame.setSize(400, 400);
    
    Landmark landmark1 = new Landmark(0.25, 0.25, new ImageIcon());
    Landmark landmark2 = new Landmark(0.75, 0.75, new ImageIcon());
    huntDisplay.addLandMark(landmark1);
    huntDisplay.addLandMark(landmark2);
    
    BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
    huntDisplay.paintComponent(image.getGraphics());
    
    // Landmark 1 should be drawn as an image
    // Here, we will only check if the drawn color is different from the background color
    int expectedColor1 = huntDisplay.getBackground().getRGB();
    int actualColor1 = new Color(image.getRGB((int)(0.25 * frame.getWidth()), (int)(0.25 * frame.getHeight()))).getRGB();
    assertNotEquals(expectedColor1, actualColor1);
    
    // Landmark 2 should be drawn as an image
    // Here, we will only check if the drawn color is different from the background color
    int expectedColor2 = huntDisplay.getBackground().getRGB();
    int actualColor2 = new Color(image.getRGB((int)(0.75 * frame.getWidth()), (int)(0.75 * frame.getHeight()))).getRGB();
    assertNotEquals(expectedColor2, actualColor2);
  }
  
  @Test
  public void testPaintComponent_withPointsAndLandmarks() {
    JFrame frame = new JFrame();
    frame.setSize(400, 400);
    
    TrigPoint point1 = new TrigPoint(0.25, 0.25, 0, false);
    TrigPoint point2 = new TrigPoint(0.75, 0.75, 0, true);
    huntDisplay.addPoint(point1);
    huntDisplay.addPoint(point2);
    
    Landmark landmark1 = new Landmark(0.25, 0.25, new ImageIcon());
    Landmark landmark2 = new Landmark(0.75, 0.75, new ImageIcon());
    huntDisplay.addLandMark(landmark1);
    huntDisplay.addLandMark(landmark2);
    
    BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
    huntDisplay.paintComponent(image.getGraphics());
    
    // Point 1 should be drawn as black
    int expectedColor1 = Color.BLACK.getRGB();
    int actualColor1 = new Color(image.getRGB((int)(0.25 * frame.getWidth()), (int)(0.25 * frame.getHeight()))).getRGB();
    assertEquals(expectedColor1, actualColor1);
    
    // Point 2 should be drawn as green
    int expectedColor2 = Color.GREEN.getRGB();
    int actualColor2 = new Color(image.getRGB((int)(0.75 * frame.getWidth()), (int)(0.75 * frame.getHeight()))).getRGB();
    assertEquals(expectedColor2, actualColor2);
    
    // Landmark 1 should be drawn as an image
    // Here, we will only check if the drawn color is different from the background color
    int expectedColor3 = huntDisplay.getBackground().getRGB();
    int actualColor3 = new Color(image.getRGB((int)(0.25 * frame.getWidth()), (int)(0.25 * frame.getHeight()))).getRGB();
    assertNotEquals(expectedColor3, actualColor3);
    
    // Landmark 2 should be drawn as an image
    // Here, we will only check if the drawn color is different from the background color
    int expectedColor4 = huntDisplay.getBackground().getRGB();
    int actualColor4 = new Color(image.getRGB((int)(0.75 * frame.getWidth()), (int)(0.75 * frame.getHeight()))).getRGB();
    assertNotEquals(expectedColor4, actualColor4);
  }
}
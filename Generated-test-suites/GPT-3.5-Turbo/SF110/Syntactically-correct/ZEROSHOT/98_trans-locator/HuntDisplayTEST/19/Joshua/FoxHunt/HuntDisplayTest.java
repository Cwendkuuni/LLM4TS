import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;
import java.awt.*;

public class HuntDisplayTest {

  @Test
  public void testAddPoint() {
    HuntDisplay huntDisplay = new HuntDisplay();

    TrigPoint point = new TrigPoint(30.0, 45.0, 60.0, true);
    huntDisplay.addPoint(point);
    
    Vector<TrigPoint> points = huntDisplay.points;
    assertEquals(1, points.size());
    assertEquals(30.0, points.get(0).getY(), 0.01);
    assertEquals(45.0, points.get(0).getX(), 0.01);
    assertEquals(60.0, points.get(0).getTheta(), 0.01);
    assertTrue(points.get(0).isFox());

    TrigPoint point2 = new TrigPoint(60.0, 90.0, 120.0, false);
    huntDisplay.addPoint(point2);

    assertEquals(2, points.size());
    assertEquals(60.0, points.get(1).getY(), 0.01);
    assertEquals(90.0, points.get(1).getX(), 0.01);
    assertEquals(120.0, points.get(1).getTheta(), 0.01);
    assertFalse(points.get(1).isFox());
    
    // Additional assertions for repaint 
  }

  @Test
  public void testAddLandMark() {
    HuntDisplay huntDisplay = new HuntDisplay();

    Landmark landmark = new Landmark(30.0, 45.0, new ImageIcon("icon.png"));
    huntDisplay.addLandMark(landmark);

    Vector<Landmark> landmarks = huntDisplay.landmarks;
    assertEquals(1, landmarks.size());
    assertEquals(30.0, landmarks.get(0).getLat(), 0.01);
    assertEquals(45.0, landmarks.get(0).getLon(), 0.01);
    // Additional assertions for icon

    Landmark landmark2 = new Landmark(60.0, 90.0, new ImageIcon("icon2.png"));
    huntDisplay.addLandMark(landmark2);

    assertEquals(2, landmarks.size());
    assertEquals(60.0, landmarks.get(1).getLat(), 0.01);
    assertEquals(90.0, landmarks.get(1).getLon(), 0.01);
    // Additional assertions for icon
    
    // Additional assertions for repaint 
  }

  @Test
  public void testPaintComponent() {
    // Test code for paintComponent method
  }

  // Additional test methods for getPreferredSize and getMinimumSize

}
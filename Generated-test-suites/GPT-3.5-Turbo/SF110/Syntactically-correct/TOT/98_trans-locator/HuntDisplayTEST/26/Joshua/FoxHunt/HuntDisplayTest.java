import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class HuntDisplayTest {

  private HuntDisplay huntDisplay;

  @Before
  public void setUp() {
    huntDisplay = new HuntDisplay();
  }

  @Test
  public void testAddPoint() {
    TrigPoint point = new TrigPoint(45, 90, 45, true);
    huntDisplay.addPoint(point);
    Vector<TrigPoint> points = huntDisplay.points;
    assertEquals(1, points.size());
    TrigPoint addedPoint = points.elementAt(0);
    assertEquals(point.getY() / 90.0, addedPoint.getY(), 0);
    assertEquals(point.getX() / 180.0, addedPoint.getX(), 0);
    assertEquals(point.getTheta(), addedPoint.getTheta());
    assertEquals(point.isFox(), addedPoint.isFox());
  }

  @Test
  public void testAddLandmark() {
    Landmark landmark = new Landmark(45, 90, new ImageIcon());
    huntDisplay.addLandMark(landmark);
    Vector<Landmark> landmarks = huntDisplay.landmarks;
    assertEquals(1, landmarks.size());
    Landmark addedLandmark = landmarks.elementAt(0);
    assertEquals(landmark.getLat() / 90.0, addedLandmark.getLat(), 0);
    assertEquals(landmark.getLon() / 180.0, addedLandmark.getLon(), 0);
    assertEquals(landmark.getIcon(), addedLandmark.getIcon());
  }

  @Test
  public void testPaintComponentWithPoints() {
    TrigPoint point1 = new TrigPoint(45, 90, 45, true);
    TrigPoint point2 = new TrigPoint(60, 120, -90, false);
    huntDisplay.addPoint(point1);
    huntDisplay.addPoint(point2);
    BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    g2d.setPaint(Color.WHITE);
    g2d.fillRect(0, 0, 400, 400);
    huntDisplay.paintComponent(g2d);
    Color expectedPointColor = Color.BLACK;
    Color expectedFoxColor = Color.GREEN;
    assertEquals(expectedPointColor, new Color(image.getRGB((int)(point1.getX() + 0.5) * 400, (int)(point1.getY() + 0.5) * 400)));
    assertEquals(expectedFoxColor, new Color(image.getRGB((int)(point2.getX() + 0.5) * 400, (int)(point2.getY() + 0.5) * 400)));
  }

  @Test
  public void testPaintComponentWithLandmarks() {
    Landmark landmark1 = new Landmark(45, 90, new ImageIcon());
    Landmark landmark2 = new Landmark(60, 120, new ImageIcon());
    huntDisplay.addLandMark(landmark1);
    huntDisplay.addLandMark(landmark2);
    BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();
    g2d.setPaint(Color.WHITE);
    g2d.fillRect(0, 0, 400, 400);
    huntDisplay.paintComponent(g2d);
    Color expectedLandmark1Color = Color.BLACK;
    Color expectedLandmark2Color = Color.BLACK;
    assertEquals(expectedLandmark1Color, new Color(image.getRGB((int)(landmark1.getLon() + 0.5) * 400, (int)(landmark1.getLat() + 0.5) * 400)));
    assertEquals(expectedLandmark2Color, new Color(image.getRGB((int)(landmark2.getLon() + 0.5) * 400, (int)(landmark2.getLat() + 0.5) * 400)));
  }

  @After
  public void tearDown() {
    huntDisplay = null;
  }
}
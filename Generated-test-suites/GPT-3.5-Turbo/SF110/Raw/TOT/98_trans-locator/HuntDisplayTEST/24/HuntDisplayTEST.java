import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {
  
  @Test
  public void testConstructor() {
    HuntDisplay hd = new HuntDisplay();
    assertNotNull(hd.points);
    assertNotNull(hd.landmarks);
    assertEquals(Color.WHITE, hd.getBackground());
    assertEquals(new Dimension(50, 50), hd.getMinimumSize());
    assertEquals(new Dimension(200, 200), hd.getPreferredSize());
  }
  
  @Test
  public void testAddPoint() {
    HuntDisplay hd = new HuntDisplay();
    TrigPoint tp = new TrigPoint(90.0, 180.0, 0.0, true);
    hd.addPoint(tp);
    assertEquals(1, hd.points.size());
    assertEquals(tp.getY() / 90.0, hd.points.elementAt(0).getY(), 0.01);
    assertEquals(tp.getX() / 180.0, hd.points.elementAt(0).getX(), 0.01);
    assertEquals(tp.getTheta(), hd.points.elementAt(0).getTheta(), 0.01);
    assertTrue(hd.points.elementAt(0).isFox());
    // Test repaint
  }
  
  @Test
  public void testAddLandmark() {
    HuntDisplay hd = new HuntDisplay();
    Landmark lm = new Landmark(90.0, 180.0, new ImageIcon());
    hd.addLandmark(lm);
    assertEquals(1, hd.landmarks.size());
    assertEquals(lm.getLat() / 90.0, hd.landmarks.elementAt(0).getLat(), 0.01);
    assertEquals(lm.getLon() / 180.0, hd.landmarks.elementAt(0).getLon(), 0.01);
    assertSame(lm.getIcon(), hd.landmarks.elementAt(0).getIcon());
    // Test repaint
  }
  
  @Test
  public void testPaintComponent() {
    // Test the painting logic of the class based on different scenarios and positions of points and landmarks
  }
  
}
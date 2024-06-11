import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

  @Test
  public void testAddPoint() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("37.7881");
    frame.lon.setText("-122.407499");
    frame.addPoint();
    
    assertEquals(1, frame.points.size());
    assertEquals(1, frame.fox.size());
    assertEquals(37.7881, frame.points.get(0).getLatitude(), 0.001);
    assertEquals(-122.407499, frame.points.get(0).getLongitude(), 0.001);
    assertEquals(37.7993, frame.fox.get(0).getLatitude(), 0.001);
    assertEquals(-122.403, frame.fox.get(0).getLongitude(), 0.001);
  }
  
  @Test
  public void testAddPointWithMultiplePoints() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("37.7881");
    frame.lon.setText("-122.407499");
    frame.addPoint();
    
    frame.theta.setText("60");
    frame.lat.setText("37.7993");
    frame.lon.setText("-122.403");
    frame.addPoint();
    
    assertEquals(2, frame.points.size());
    assertEquals(2, frame.fox.size());
    assertEquals(37.7881, frame.points.get(0).getLatitude(), 0.001);
    assertEquals(-122.407499, frame.points.get(0).getLongitude(), 0.001);
    assertEquals(37.7993, frame.points.get(1).getLatitude(), 0.001);
    assertEquals(-122.403, frame.points.get(1).getLongitude(), 0.001);
    assertEquals(37.8112, frame.fox.get(0).getLatitude(), 0.001);
    assertEquals(-122.399, frame.fox.get(0).getLongitude(), 0.001);
    assertEquals(37.7993, frame.fox.get(1).getLatitude(), 0.001);
    assertEquals(-122.403, frame.fox.get(1).getLongitude(), 0.001);
  }
  
  @Test
  public void testLoadLandmarks() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.loadLandmarks();
    // test the loaded landmarks
  }
}
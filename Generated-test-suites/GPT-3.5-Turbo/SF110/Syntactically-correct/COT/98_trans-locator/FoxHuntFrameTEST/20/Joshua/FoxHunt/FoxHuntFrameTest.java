
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
  
  @Test
  public void testAddPoint() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("12.345");
    frame.lon.setText("67.890");
    frame.addPoint();
    assertEquals(1, frame.points.size());
    assertEquals(1, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_multiplePoints() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("12.345");
    frame.lon.setText("67.890");
    frame.addPoint();
    frame.theta.setText("60");
    frame.lat.setText("23.456");
    frame.lon.setText("78.901");
    frame.addPoint();
    assertEquals(2, frame.points.size());
    assertEquals(1, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_invalidInput() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("12.345");
    frame.lon.setText("abc");
    frame.addPoint();
    assertEquals(0, frame.points.size());
    assertEquals(0, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_noPoints() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("12.345");
    frame.lon.setText("67.890");
    frame.addPoint();
    assertEquals(1, frame.points.size());
    assertEquals(0, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_multipleFox() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("12.345");
    frame.lon.setText("67.890");
    frame.addPoint();
    frame.theta.setText("60");
    frame.lat.setText("23.456");
    frame.lon.setText("78.901");
    frame.addPoint();
    frame.theta.setText("75");
    frame.lat.setText("34.567");
    frame.lon.setText("89.012");
    frame.addPoint();
    assertEquals(3, frame.points.size());
    assertEquals(2, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_minimumValues() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("0");
    frame.lat.setText("0.0");
    frame.lon.setText("0.0");
    frame.addPoint();
    assertEquals(1, frame.points.size());
    assertEquals(0, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_maximumValues() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("90");
    frame.lat.setText("90.0");
    frame.lon.setText("180.0");
    frame.addPoint();
    assertEquals(1, frame.points.size());
    assertEquals(0, frame.fox.size());
  }

}

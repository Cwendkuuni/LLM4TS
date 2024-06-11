import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {

  @Test
  public void testAddPoint() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    frame.theta.setText("45");
    frame.lat.setText("37.7749");
    frame.lon.setText("-122.4194");
    frame.addPoint();
    
    assertEquals(1, frame.points.size());
    assertEquals(1, frame.fox.size());
    assertEquals(1, frame.posModel.getRowCount());
    
    TrigPoint point = frame.points.elementAt(0);
    TrigPoint foxPoint = frame.fox.elementAt(0);
    
    assertEquals(45, point.getAngle());
    assertEquals(37.7749, point.getLat(), 0.0001);
    assertEquals(-122.4194, point.getLon(), 0.0001);
    
    assertEquals(37.7749, foxPoint.getLat(), 0.0001);
    assertEquals(-122.4194, foxPoint.getLon(), 0.0001);
    
    String lattitude = frame.posModel.getValueAt(0, 0);
    String longitude = frame.posModel.getValueAt(0, 1);
    
    assertEquals("37.7749", lattitude);
    assertEquals("-122.4194", longitude);
  }

  @Test
  public void testLoadLandmarks() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    frame.loadLandmarks();
    
    assertEquals(3, frame.display.getLandmarks().size());
    
    Landmark landmark1 = frame.display.getLandmarks().get(0);
    Landmark landmark2 = frame.display.getLandmarks().get(1);
    Landmark landmark3 = frame.display.getLandmarks().get(2);
    
    assertEquals(37.7749, landmark1.getLat(), 0.0001);
    assertEquals(-122.4194, landmark1.getLon(), 0.0001);
    
    assertEquals(34.0522, landmark2.getLat(), 0.0001);
    assertEquals(-118.2437, landmark2.getLon(), 0.0001);
    
    assertEquals(39.9526, landmark3.getLat(), 0.0001);
    assertEquals(-75.1652, landmark3.getLon(), 0.0001);
  }

  @Test
  public void testFoxTableModel() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    
    assertEquals(2, model.getColumnCount());
    assertEquals(0, model.getRowCount());
    
    assertEquals("Lattitude", model.getColumnName(0));
    assertEquals("Longitude", model.getColumnName(1));
    
    assertTrue(model.getValueAt(0, 0) instanceof String);
    assertTrue(model.getValueAt(0, 1) instanceof String);
    
    assertFalse(model.isCellEditable(0, 0));
    assertFalse(model.isCellEditable(0, 1));
  }

}
import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

  @Test
  public void testAddPoint() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("10.1234");
    frame.lon.setText("20.5678");
    frame.addPoint();
    assertEquals(1, frame.points.size());

    // Verify that the TrigPoint was created correctly
    TrigPoint point = (TrigPoint) frame.points.elementAt(0);
    assertEquals(45, point.getAngle());
    assertEquals(10.1234, point.getLat(), 0.0001);
    assertEquals(20.5678, point.getLon(), 0.0001);

    // Verify that the TrigPoint was added to the display
    assertNotNull(frame.display.getPoint(point));

    // Verify that the fox vector was updated
    assertEquals(0, frame.fox.size());
  }

  @Test
  public void testAddPointWithExistingPoints() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.points.add(new TrigPoint(10, 20, 30));
    frame.display.addPoint(frame.points.elementAt(0));

    frame.theta.setText("45");
    frame.lat.setText("30.1234");
    frame.lon.setText("40.5678");
    frame.addPoint();
    assertEquals(2, frame.points.size());

    // Verify that the TrigPoint was created correctly
    TrigPoint point = (TrigPoint) frame.points.elementAt(1);
    assertEquals(45, point.getAngle());
    assertEquals(30.1234, point.getLat(), 0.0001);
    assertEquals(40.5678, point.getLon(), 0.0001);

    // Verify that the TrigPoint was added to the display
    assertNotNull(frame.display.getPoint(point));

    // Verify that the fox vector was updated
    assertEquals(1, frame.fox.size());

    // Verify that the table data was updated
    assertEquals(2, frame.posModel.getRowCount());
    assertEquals("30.1234", frame.posModel.getValueAt(1, 0));
    assertEquals("40.5678", frame.posModel.getValueAt(1, 1));
  }

  @Test
  public void testFinalPrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    assertNotNull(frame.zoom);
    assertNotNull(frame.display);
    assertNotNull(frame.panel1);
    assertNotNull(frame.panel2);
    assertNotNull(frame.panel3);
    assertNotNull(frame.panel);
  }

  @Test
  public void testTextPrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    assertNotNull(frame.theta);
    assertNotNull(frame.lat);
    assertNotNull(frame.lon);
    assertNotNull(frame.add);
    assertNotNull(frame.panel1);
  }

  @Test
  public void testTablePrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    assertNotNull(frame.posModel);
    assertNotNull(frame.pos);
    assertNotNull(frame.panel2);
  }

  @Test
  public void testDisplayPrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    assertNotNull(frame.display);
    assertNotNull(frame.iconFile);
    assertNotNull(frame.lat);
    assertNotNull(frame.lon);
    assertNotNull(frame.panel3);
  }

  @Test
  public void testLoadLandmarks() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.loadLandmarks();
    assertEquals(0, frame.display.getLandmarks().size());
  }

  @Test
  public void testError() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.error("Error message");
    // Verify that the error dialog was displayed
    // This cannot be tested since it is an interactive dialog
  }

  @Test
  public void testFoxTableModelGetColumnCount() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals(2, model.getColumnCount());
  }

  @Test
  public void testFoxTableModelGetRowCount() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.fox.add(new TrigPoint(10, 20, 30));
    frame.posModel.fireTableDataChanged();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals(1, model.getRowCount());
  }

  @Test
  public void testFoxTableModelGetColumnName() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals("Lattitude", model.getColumnName(0));
    assertEquals("Longitude", model.getColumnName(1));
  }

  @Test
  public void testFoxTableModelGetValueAt() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.fox.add(new TrigPoint(10, 20, 30));
    frame.posModel.fireTableDataChanged();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals("20.0", model.getValueAt(0, 0));
    assertEquals("10.0", model.getValueAt(0, 1));
  }

  @Test
  public void testFoxTableModelGetColumnClass() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.fox.add(new TrigPoint(10, 20, 30));
    frame.posModel.fireTableDataChanged();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals(String.class, model.getColumnClass(0));
    assertEquals(String.class, model.getColumnClass(1));
  }

  @Test
  public void testFoxTableModelIsCellEditable() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.fox.add(new TrigPoint(10, 20, 30));
    frame.posModel.fireTableDataChanged();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertFalse(model.isCellEditable(0, 0));
    assertFalse(model.isCellEditable(0, 1));
  }

}
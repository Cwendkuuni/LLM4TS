import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
  
  @Test
  public void testConstructor() {
    FoxHuntFrame frame = new FoxHuntFrame();
    assertNotNull(frame);
    assertEquals("Triangulation", frame.getTitle());
    assertEquals(6, frame.getState());
    assertTrue(frame.isVisible());
  }
  
  @Test
  public void testActionPerformed() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("35.12345");
    frame.lon.setText("120.98765");
    frame.addActionPerformed(null);
  }
  
  @Test
  public void testAddPoint() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.theta.setText("45");
    frame.lat.setText("35.12345");
    frame.lon.setText("120.98765");
    frame.addPoint();
  }
  
  @Test
  public void testFinalPrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.finalPrep();
    assertNotNull(frame.zoom);
    assertNotNull(frame.panel);
    assertNotNull(frame.panel3);
    assertEquals(BorderLayout.NORTH, frame.panel3.getLayout().getConstraints(frame.zoom));
    assertEquals(BorderLayout.CENTER, frame.panel3.getLayout().getConstraints(frame.display));
  }
  
  @Test
  public void testTextPrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.textPrep();
    assertNotNull(frame.theta);
    assertNotNull(frame.lat);
    assertNotNull(frame.lon);
    assertNotNull(frame.add);
    assertNotNull(frame.panel1);
    assertEquals(4, frame.panel1.getRowCount());
  }
  
  @Test
  public void testTablePrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.tablePrep();
    assertNotNull(frame.posModel);
    assertNotNull(frame.pos);
    assertNotNull(frame.panel2);
    assertEquals(BorderLayout.CENTER, frame.panel2.getLayout().getConstraints(frame.scroll));
    assertEquals(new Dimension(500, 70), frame.pos.getPreferredScrollableViewportSize());
  }
  
  @Test
  public void testDisplayPrep() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.displayPrep();
    assertNotNull(frame.display);
  }
  
  @Test
  public void testLoadLandmarks() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.loadLandmarks();
  }
  
  @Test
  public void testError() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.error("Test error message");
  }
  
  @Test
  public void testFoxTableModelConstructor() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertNotNull(model);
    assertNotNull(model.colNames);
    assertEquals(2, model.colNames.length);
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
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals(frame.fox.size(), model.getRowCount());
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
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals(Double.toString(frame.fox.elementAt(0).getY()), model.getValueAt(0, 0));
    assertEquals(Double.toString(frame.fox.elementAt(0).getX()), model.getValueAt(0, 1));
  }
  
  @Test
  public void testFoxTableModelGetColumnClass() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals(String.class, model.getColumnClass(0));
    assertEquals(String.class, model.getColumnClass(1));
  }
  
  @Test
  public void testFoxTableModelIsCellEditable() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertFalse(model.isCellEditable(0, 0));
    assertFalse(model.isCellEditable(0, 1));
  }
  
}
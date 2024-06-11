import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.text.*;

public class FoxHuntFrameTest {
  
  @Test
  public void testAddPoint_typicalCase() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    frame.theta.setText("45");
    frame.lat.setText("37.7749");
    frame.lon.setText("-122.4194");
    frame.addPoint();
    
    assertEquals(1, frame.points.size());
    assertEquals(1, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_multiplePoints() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    frame.theta.setText("45");
    frame.lat.setText("37.7749");
    frame.lon.setText("-122.4194");
    frame.addPoint();
    
    frame.theta.setText("60");
    frame.lat.setText("34.0522");
    frame.lon.setText("-118.2437");
    frame.addPoint();
    
    assertEquals(2, frame.points.size());
    assertEquals(1, frame.fox.size());
  }
  
  @Test
  public void testAddPoint_noPoints() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    frame.theta.setText("45");
    frame.lat.setText("37.7749");
    frame.lon.setText("-122.4194");
    frame.addPoint();
    
    assertTrue(frame.fox.isEmpty());
  }
  
  @Test
  public void testLoadLandmarks_fileNotFound() {
    FoxHuntFrame frame = new FoxHuntFrame();
    frame.loadLandmarks();
    
    // Assuming "./landmarks.cfg" file does not exist
    // The error dialog should prompt the user to abort or continue
    // In this case, we expect to continue without terminating the program
    assertTrue(frame.isVisible());
  }
  
  @Test
  public void testLoadLandmarks_invalidFormat() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    // Creating a dummy landmarks.config file with invalid format
    // This should throw an IOException and show an error dialog
    try {
      FileWriter writer = new FileWriter("./landmarks.config");
      writer.write("INVALID_FORMAT");
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // The error dialog should prompt the user to abort or continue
    // In this case, we expect to continue without terminating the program
    frame.loadLandmarks();
    assertTrue(frame.isVisible());
  }
  
  @Test
  public void testFoxTableModel_getRowCount() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    frame.theta.setText("45");
    frame.lat.setText("37.7749");
    frame.lon.setText("-122.4194");
    frame.addPoint();
    
    frame.theta.setText("60");
    frame.lat.setText("34.0522");
    frame.lon.setText("-118.2437");
    frame.addPoint();
    
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    assertEquals(1, model.getRowCount());
  }
  
  @Test
  public void testFoxTableModel_getColumnName() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    
    assertEquals("Lattitude", model.getColumnName(0));
    assertEquals("Longitude", model.getColumnName(1));
  }
  
  @Test
  public void testFoxTableModel_getValueAt() {
    FoxHuntFrame frame = new FoxHuntFrame();
    
    frame.theta.setText("45");
    frame.lat.setText("37.7749");
    frame.lon.setText("-122.4194");
    frame.addPoint();
    
    frame.theta.setText("60");
    frame.lat.setText("34.0522");
    frame.lon.setText("-118.2437");
    frame.addPoint();
    
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    
    assertEquals("37.7749", model.getValueAt(0, 0));
    assertEquals("-122.4194", model.getValueAt(0, 1));
  }
  
  @Test
  public void testFoxTableModel_getColumnClass() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    
    assertEquals(String.class, model.getColumnClass(0));
    assertEquals(String.class, model.getColumnClass(1));
  }
  
  @Test
  public void testFoxTableModel_isCellEditable() {
    FoxHuntFrame frame = new FoxHuntFrame();
    FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
    
    assertFalse(model.isCellEditable(0, 0));
    assertFalse(model.isCellEditable(0, 1));
  }
}
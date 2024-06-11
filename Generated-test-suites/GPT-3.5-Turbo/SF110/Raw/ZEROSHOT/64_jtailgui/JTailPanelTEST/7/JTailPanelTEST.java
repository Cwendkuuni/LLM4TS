import org.junit.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class JTailPanelTest {
  
  private JTailModel model;
  private JTailPanel tailPanel;
  
  @Before
  public void setUp() {
    model = new JTailModel();
    tailPanel = new JTailPanel(model);
  }
  
  @Test
  public void testGetCharacterHeight() {
    int characterHeight = tailPanel.getCharacterHeight();
    assertEquals(16, characterHeight);
  }
  
  @Test
  public void testGetVerticalScrollBar() {
    JScrollBar verticalScrollBar = tailPanel.getVerticalScrollBar();
    assertNotNull(verticalScrollBar);
  }
  
  @Test
  public void testUpdate() {
    // Test when observable is not JTailModel
    tailPanel.update(new Observable(), null);
    
    // Test when event type is RESIZED
    JTailModelEvent resizedEvent = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 10, 20);
    tailPanel.update(model, resizedEvent);
    
    // Test when event type is SCROLLED
    JTailModelEvent scrolledEvent = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
    tailPanel.update(model, scrolledEvent);
  }
  
  @Test
  public void testUpdateDataTable() {
    // Test with empty lineNumbers and lines
    String lineNumbers = "";
    String lines = "";
    tailPanel.updateDataTable(lineNumbers, lines);
    DefaultTableModel tableModel = (DefaultTableModel) tailPanel.dataTable.getModel();
    assertEquals(0, tableModel.getRowCount());
    
    // Test with non-empty lineNumbers and empty lines
    lineNumbers = "1\n2\n3\n4\n5";
    lines = "";
    tailPanel.updateDataTable(lineNumbers, lines);
    tableModel = (DefaultTableModel) tailPanel.dataTable.getModel();
    assertEquals(5, tableModel.getRowCount());
    
    // Test with empty lineNumbers and non-empty lines
    lineNumbers = "";
    lines = "line 1\nline 2\nline 3\nline 4\nline 5";
    tailPanel.updateDataTable(lineNumbers, lines);
    tableModel = (DefaultTableModel) tailPanel.dataTable.getModel();
    assertEquals(5, tableModel.getRowCount());
    
    // Test with matching lineNumbers and lines
    lineNumbers = "1\n2\n3\n4\n5";
    lines = "line 1\nline 2\nline 3\nline 4\nline 5";
    tailPanel.updateDataTable(lineNumbers, lines);
    tableModel = (DefaultTableModel) tailPanel.dataTable.getModel();
    assertEquals(5, tableModel.getRowCount());
  }
  
}
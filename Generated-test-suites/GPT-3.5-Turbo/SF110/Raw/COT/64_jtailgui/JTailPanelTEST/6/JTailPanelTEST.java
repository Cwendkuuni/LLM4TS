
import org.junit.*;
import static org.junit.Assert.*;

public class JTailPanelTest {
  
  @Test
  public void testGetCharacterHeight() {
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    int expectedHeight = 20; // assuming the default height is 20
    int actualHeight = panel.getCharacterHeight();
    assertEquals(expectedHeight, actualHeight);
  }
  
  @Test
  public void testGetVerticalScrollBar() {
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    JScrollBar expectedScrollBar = new JScrollBar();
    JScrollBar actualScrollBar = panel.getVerticalScrollBar();
    assertEquals(expectedScrollBar, actualScrollBar);
  }
  
  @Test
  public void testUpdate() {
    // Create JTailModel and JTailPanel objects
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    
    // Create a JTailModelEvent object
    JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 10, 5, 20);
    
    // Call the update method
    panel.update(model, event);
    
    // Check if the vertical scroll bar is enabled
    JScrollBar scrollBar = panel.getVerticalScrollBar();
    assertFalse(scrollBar.isEnabled());
  }
  
  @Test
  public void testUpdateDataTable() {
    // Create JTailModel and JTailPanel objects
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    
    // Create line numbers and lines strings
    String lineNumbers = "1\n2\n3\n4\n5";
    String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
    
    // Call the updateDataTable method
    panel.updateDataTable(lineNumbers, lines);
    
    // Check if the table model contains the correct number of rows
    DefaultTableModel tableModel = panel.tableModel;
    int expectedRowCount = 5;
    int actualRowCount = tableModel.getRowCount();
    assertEquals(expectedRowCount, actualRowCount);
    
    // Check if the table model contains the correct line numbers and lines
    String expectedLineNumber1 = "1";
    String expectedLine1 = "Line 1";
    String actualLineNumber1 = (String)tableModel.getValueAt(0, 0);
    String actualLine1 = (String)tableModel.getValueAt(0, 1);
    assertEquals(expectedLineNumber1, actualLineNumber1);
    assertEquals(expectedLine1, actualLine1);
    
    // Check if the table model contains the correct line numbers and lines
    String expectedLineNumber5 = "5";
    String expectedLine5 = "Line 5";
    String actualLineNumber5 = (String)tableModel.getValueAt(4, 0);
    String actualLine5 = (String)tableModel.getValueAt(4, 1);
    assertEquals(expectedLineNumber5, actualLineNumber5);
    assertEquals(expectedLine5, actualLine5);
  }
}


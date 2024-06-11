import org.junit.*;
import static org.junit.Assert.*;

public class JTailPanelTest {
  
  @Test
  public void testGetCharacterHeight() {
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    
    assertEquals(panel.getCharacterHeight(), panel.characterHeight);
  }
  
  @Test
  public void testGetVerticalScrollBar() {
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    
    assertEquals(panel.getVerticalScrollBar(), panel.verticalScrollBar);
  }
  
  @Test
  public void testUpdate() {
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    
    // create mock JTailModelEvent
    JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 1);
    
    panel.update(model, event);
    
    // check if values have been updated correctly
    assertEquals(panel.verticalScrollBar.getModel().getValueIsAdjusting(), false);
    assertEquals(panel.verticalScrollBar.getModel().getMinimum(), event.minLine);
    assertEquals(panel.verticalScrollBar.getModel().getValue(), event.firstLine);
    assertEquals(panel.verticalScrollBar.getModel().getExtent(), event.nbDisplayedLines);
    assertEquals(panel.verticalScrollBar.getModel().getMaximum(), event.maxLine);
    
    // create mock JTailModelEvent for scrolled event
    event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 1);
    
    panel.update(model, event);
    
    // check if JTailModelEvent has been processed correctly
    assertEquals(panel.tableModel.getRowCount(), 0);
  }
  
  @Test
  public void testUpdateDataTable() {
    JTailModel model = new JTailModel();
    JTailPanel panel = new JTailPanel(model);
    
    // create mock line numbers and lines
    String lineNumbers = "1\n2\n3";
    String lines = "line 1\nline 2\nline 3";
    
    panel.updateDataTable(lineNumbers, lines);
    
    // check if table model has been updated correctly
    assertEquals(panel.tableModel.getValueAt(0, 0), "1");
    assertEquals(panel.tableModel.getValueAt(0, 1), "line 1");
    assertEquals(panel.tableModel.getValueAt(1, 0), "2");
    assertEquals(panel.tableModel.getValueAt(1, 1), "line 2");
    assertEquals(panel.tableModel.getValueAt(2, 0), "3");
    assertEquals(panel.tableModel.getValueAt(2, 1), "line 3");
  }
  
}
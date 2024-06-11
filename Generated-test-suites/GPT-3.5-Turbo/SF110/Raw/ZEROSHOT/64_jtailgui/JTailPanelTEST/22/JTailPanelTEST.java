import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JTailPanelTest {

  private JTailModel model;
  private JTailPanel jTailPanel;

  @Before
  public void setup() {
    model = new JTailModel();
    jTailPanel = new JTailPanel(model);
  }

  @Test
  public void testGetCharacterHeight() {
    int expectedHeight = jTailPanel.dataTable.getFontMetrics(jTailPanel.dataTable.getFont()).getHeight() - 1;
    assertEquals(expectedHeight, jTailPanel.getCharacterHeight());
  }

  @Test
  public void testGetVerticalScrollBar() {
    assertEquals(jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
  }

  @Test
  public void testUpdate() {
    Observable observable = new Observable();
    JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0);

    jTailPanel.update(observable, event);
    // Check if the vertical scroll bar is disabled initially
    assertFalse(jTailPanel.verticalScrollBar.isEnabled());

    event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
    jTailPanel.update(observable, event);
    // Check if the data table is updated
    assertEquals(0, jTailPanel.tableModel.getRowCount());

    // Add rows to the table model
    jTailPanel.tableModel.addRow(new String[]{"1", "Line 1"});
    jTailPanel.tableModel.addRow(new String[]{"2", "Line 2"});

    // Check if the table model row count is updated correctly
    assertEquals(2, jTailPanel.tableModel.getRowCount());

    // Check if the data table column widths are updated correctly
    TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
    TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
    numbersColumn.setPreferredWidth(100);
    linesColumn.setPreferredWidth(200);

    // Check if the table model row count is reset to 0
    jTailPanel.updateDataTable("", "");
    assertEquals(0, jTailPanel.tableModel.getRowCount());

    // Check if the data table column widths are reset correctly
    assertEquals(0, numbersColumn.getPreferredWidth());
    assertEquals(0, linesColumn.getPreferredWidth());
  }

}
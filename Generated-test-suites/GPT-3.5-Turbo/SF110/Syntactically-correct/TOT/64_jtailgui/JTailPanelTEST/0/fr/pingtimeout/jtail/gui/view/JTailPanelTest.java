import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {
    
    private JTailModel model;
    private JTailPanel panel;
    private JScrollBar verticalScrollBar;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        panel = new JTailPanel(model);
        verticalScrollBar = panel.getVerticalScrollBar();
    }
    
    @Test
    public void testGetCharacterHeight() {
        int expectedHeight = getExpectedCharacterHeight();
        int actualHeight = panel.getCharacterHeight();
        assertEquals(expectedHeight, actualHeight);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JScrollBar expectedScrollBar = getExpectedVerticalScrollBar();
        JScrollBar actualScrollBar = panel.getVerticalScrollBar();
        assertEquals(expectedScrollBar, actualScrollBar);
    }
    
    @Test
    public void testUpdate_ResizedEvent() {
        JTailModelEvent resizedEvent = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 10, 5, 20);
        model.notifyObservers(resizedEvent);
        
        JScrollBar expectedScrollBar = getExpectedVerticalScrollBar();
        JScrollBar actualScrollBar = panel.getVerticalScrollBar();
        assertEquals(expectedScrollBar, actualScrollBar);
        
        DefaultTableModel expectedTableModel = createExpectedTableModel(resizedEvent.getLineNumbers(), resizedEvent.getContent());
        DefaultTableModel actualTableModel = (DefaultTableModel) panel.dataTable.getModel();
        assertEquals(expectedTableModel.getRowCount(), actualTableModel.getRowCount());
        for (int i = 0; i < expectedTableModel.getRowCount(); i++) {
            assertEquals(expectedTableModel.getValueAt(i, 0), actualTableModel.getValueAt(i, 0));
            assertEquals(expectedTableModel.getValueAt(i, 1), actualTableModel.getValueAt(i, 1));
        }
    }
    
    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModelEvent scrolledEvent = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 5, 0);
        model.notifyObservers(scrolledEvent);
        
        DefaultTableModel expectedTableModel = createExpectedTableModel(scrolledEvent.getLineNumbers(), scrolledEvent.getContent());
        DefaultTableModel actualTableModel = (DefaultTableModel) panel.dataTable.getModel();
        assertEquals(expectedTableModel.getRowCount(), actualTableModel.getRowCount());
        for (int i = 0; i < expectedTableModel.getRowCount(); i++) {
            assertEquals(expectedTableModel.getValueAt(i, 0), actualTableModel.getValueAt(i, 0));
            assertEquals(expectedTableModel.getValueAt(i, 1), actualTableModel.getValueAt(i, 1));
        }
    }
    
    private int getExpectedCharacterHeight() {
        FontMetrics metrics = panel.dataTable.getFontMetrics(panel.dataTable.getFont());
        return metrics.getHeight() - 1;
    }
    
    private JScrollBar getExpectedVerticalScrollBar() {
        JScrollBar expectedScrollBar = new JScrollBar();
        expectedScrollBar.addAdjustmentListener(new ScrollBarAdjustmentController(panel, model));
        return expectedScrollBar;
    }
    
    private DefaultTableModel createExpectedTableModel(String lineNumbers, String lines) {
        DefaultTableModel expectedTableModel = new DefaultTableModel(new String[] { "#", "" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        String[] lineNumbersArray = lineNumbers.split("\n");
        String[] linesArray = lines.split("\n");
        for (int i = 0; i < lineNumbersArray.length; i++) {
            String lineNumberTmp = lineNumbersArray[i];
            String lineTmp = (i < linesArray.length) ? linesArray[i] : "";
            expectedTableModel.addRow(new String[]{lineNumberTmp, lineTmp});
        }
        return expectedTableModel;
    }
}
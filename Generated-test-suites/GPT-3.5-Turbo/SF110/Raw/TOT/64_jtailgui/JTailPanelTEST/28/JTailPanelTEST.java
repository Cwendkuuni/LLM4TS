
import static org.junit.Assert.*;
import org.junit.*;
import java.awt.*;
import java.awt.event.*;

public class JTailPanelTest {
    
    private JTailModel model;
    private JTailPanel panel;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        panel = new JTailPanel(model);
    }
    
    @After
    public void tearDown() {
        model = null;
        panel = null;
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(panel.model);
        assertNotNull(panel.scrollPane);
        assertNotNull(panel.verticalScrollBar);
        assertNotNull(panel.dataTable);
        assertNotNull(panel.tableModel);
        assertEquals(0, panel.tableModel.getRowCount());
        assertEquals(2, panel.tableModel.getColumnCount());
        assertEquals(0, panel.characterHeight);
        assertEquals(0, panel.maxCharacterWidth);
    }
    
    @Test
    public void testGetCharacterHeight() {
        assertEquals(0, panel.getCharacterHeight());
        panel.characterHeight = 10;
        assertEquals(10, panel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertNotNull(panel.getVerticalScrollBar());
        assertEquals(panel.verticalScrollBar, panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate_ResizedEvent_NoLineNumbers() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 10, 10);
        panel.update(model, event);
        assertEquals(0, panel.tableModel.getRowCount());
        assertFalse(panel.verticalScrollBar.isEnabled());
    }
    
    @Test
    public void testUpdate_ResizedEvent_LineNumbers() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 10, 10);
        panel.update(model, event);
        assertEquals(9, panel.tableModel.getRowCount());
        assertTrue(panel.verticalScrollBar.isEnabled());
    }
    
    @Test
    public void testUpdate_ResizedEvent_MinMaxLines() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1);
        panel.update(model, event);
        assertEquals(1, panel.tableModel.getRowCount());
        assertFalse(panel.verticalScrollBar.isEnabled());
    }
    
    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        panel.update(model, event);
        assertEquals(0, panel.tableModel.getRowCount());
    }
}


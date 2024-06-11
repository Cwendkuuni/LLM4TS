import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class JTailPanelTest {
    
    @Mock
    private JTailModel mockModel;
    
    private JTailPanel panel;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        panel = new JTailPanel(mockModel);
    }
    
    @Test
    public void testGetCharacterHeight() {
        int expectedHeight = 16; // assuming default font metrics height is 16
        int actualHeight = panel.getCharacterHeight();
        
        assertEquals(expectedHeight, actualHeight);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JScrollBar expectedScrollBar = panel.getVerticalScrollBar();
        
        assertNotNull(expectedScrollBar);
    }
    
    @Test
    public void testUpdate_ResizedEvent_Enabled() {
        JTailModelEvent event = new JTailModelEvent();
        event.setType(JTailModelEvent.Type.RESIZED);
        event.setMinLine(0);
        event.setFirstLine(0);
        event.setNbDisplayedLines(10);
        event.setMaxLine(20);
        
        panel.update(null, event);
        
        assertFalse(panel.getVerticalScrollBar().isEnabled());
    }
    
    @Test
    public void testUpdate_ResizedEvent_Disabled() {
        JTailModelEvent event = new JTailModelEvent();
        event.setType(JTailModelEvent.Type.RESIZED);
        event.setMinLine(5);
        event.setFirstLine(5);
        event.setNbDisplayedLines(5);
        event.setMaxLine(5);
        
        panel.update(null, event);
        
        assertTrue(panel.getVerticalScrollBar().isEnabled());
    }
    
    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModelEvent event = new JTailModelEvent();
        event.setType(JTailModelEvent.Type.SCROLLED);
        
        panel.update(null, event);
        
        verify(mockModel, times(1)).getLineNumbers();
        verify(mockModel, times(1)).getContent();
    }
    
    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3";
        String lines = "a\nb\nc";
        
        panel.updateDataTable(lineNumbers, lines);
        
        DefaultTableModel tableModel = (DefaultTableModel) panel.dataTable.getModel();
        assertEquals(3, tableModel.getRowCount());
    }
}
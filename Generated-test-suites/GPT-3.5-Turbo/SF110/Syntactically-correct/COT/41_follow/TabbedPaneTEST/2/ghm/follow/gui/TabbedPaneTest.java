
import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(null);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.setSelectedIndex(0);
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.setSelectedComponent(null);
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.removeTabAt(0);
    }
}

import static org.junit.Assert.*;
import org.junit.Test;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        tabbedPane.add("Panel 1", panel1);
        tabbedPane.add("Panel 2", panel2);

        assertNotNull(tabbedPane.findComponentAt(0, 0));
        assertEquals(panel1, tabbedPane.findComponentAt(5, 5));
        assertEquals(panel2, tabbedPane.findComponentAt(100, 100));
        assertNull(tabbedPane.findComponentAt(200, 200));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        tabbedPane.add("Panel 1", panel1);
        tabbedPane.add("Panel 2", panel2);

        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        tabbedPane.add("Panel 1", panel1);
        tabbedPane.add("Panel 2", panel2);

        tabbedPane.setSelectedComponent(panel2);
        assertEquals(panel2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        tabbedPane.add("Panel 1", panel1);
        tabbedPane.add("Panel 2", panel2);

        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
    }

}
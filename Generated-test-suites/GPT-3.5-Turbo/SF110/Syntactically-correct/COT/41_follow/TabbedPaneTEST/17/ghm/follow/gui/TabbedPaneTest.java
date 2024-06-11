import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;

import static org.junit.Assert.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;

    @Before
    public void setup() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt() {
        // Test when x and y are within the bounds of the component
        Component result1 = tabbedPane.findComponentAt(10, 10);
        assertNotNull(result1);

        // Test when x and y are outside the bounds of the component
        Component result2 = tabbedPane.findComponentAt(100, 100);
        assertNull(result2);

        // Test when x is negative
        Component result3 = tabbedPane.findComponentAt(-10, 10);
        assertNull(result3);

        // Test when y is negative
        Component result4 = tabbedPane.findComponentAt(10, -10);
        assertNull(result4);
    }

    @Test
    public void testSetSelectedIndex() {
        // Test setting the selected index to a valid value
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());

        // Test setting the selected index to an invalid value
        int previousIndex = tabbedPane.getSelectedIndex();
        tabbedPane.setSelectedIndex(-1);
        assertEquals(previousIndex, tabbedPane.getSelectedIndex());

        // Test setting the selected index to an invalid value
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() + 1);
        assertEquals(previousIndex, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        // Test setting the selected component to a valid value
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());

        // Test setting the selected component to null
        tabbedPane.setSelectedComponent(null);
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        // Test removing a tab at a valid index
        int previousTabCount = tabbedPane.getTabCount();
        tabbedPane.removeTabAt(0);
        assertEquals(previousTabCount - 1, tabbedPane.getTabCount());

        // Test removing a tab at an invalid index
        tabbedPane.removeTabAt(-1);
        assertEquals(previousTabCount - 1, tabbedPane.getTabCount());

        // Test removing a tab at an invalid index
        tabbedPane.removeTabAt(tabbedPane.getTabCount());
        assertEquals(previousTabCount - 1, tabbedPane.getTabCount());
    }
}
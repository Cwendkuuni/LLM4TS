import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.io.File;
import javax.swing.JTabbedPane;
import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_NullResult() {
        Component result = tabbedPane.findComponentAt(0, 0);
        assertNull(result);
    }

    @Test
    public void testFindComponentAt_ComponentVisible_Container() {
        MockContainer container = new MockContainer();
        container.setVisible(true);
        tabbedPane.addTab("Container", container);
        Component result = tabbedPane.findComponentAt(10, 10);
        assertNotNull(result);
        assertEquals(container, result);
    }

    @Test
    public void testFindComponentAt_ComponentVisible_NotContainer() {
        MockComponent component = new MockComponent();
        component.setVisible(true);
        tabbedPane.addTab("Component", component);
        Component result = tabbedPane.findComponentAt(10, 10);
        assertNotNull(result);
        assertEquals(component, result);
    }

    @Test
    public void testFindComponentAt_ComponentNotVisible_Container() {
        MockContainer container = new MockContainer();
        container.setVisible(false);
        tabbedPane.addTab("Container", container);
        Component result = tabbedPane.findComponentAt(10, 10);
        assertNotNull(result);
        assertEquals(tabbedPane, result);
    }

    @Test
    public void testFindComponentAt_ComponentNotVisible_NotContainer() {
        MockComponent component = new MockComponent();
        component.setVisible(false);
        tabbedPane.addTab("Component", component);
        Component result = tabbedPane.findComponentAt(10, 10);
        assertNotNull(result);
        assertEquals(tabbedPane, result);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(1);
        // Verify selected index is set correctly
        assertEquals(1, tabbedPane.getSelectedIndex());
        // Verify handleSelectedFile() is called
        // Additional verification can be added here
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        // Verify selected component is set correctly
        assertEquals(pane, tabbedPane.getSelectedComponent());
        // Verify handleSelectedFile() is called
        // Additional verification can be added here
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", new FileFollowingPane());
        tabbedPane.addTab("Tab 2", new FileFollowingPane());
        int indexToRemove = 0;
        tabbedPane.removeTabAt(indexToRemove);
        // Verify tab is removed correctly
        assertNull(tabbedPane.getComponentAt(indexToRemove));
        // Verify handleSelectedFile() is called
        // Additional verification can be added here
    }

    @Test
    public void testHandleSelectedFile_NoSelectedComponent() {
        tabbedPane.handleSelectedFile();
        // Additional verification can be added here
    }

    @Test
    public void testHandleSelectedFile_SelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane();
        File file = new File("test.txt");
        pane.setFollowedFile(file);
        tabbedPane.addTab("Tab 1", pane);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        // Verify LastFileChooserDirectory is set correctly in attributes
        assertEquals(file.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
    }
}

class MockContainer extends Container {
    @Override
    public Component findComponentAt(int x, int y) {
        return this;
    }
}

class MockComponent extends Component {
    @Override
    public Component getComponentAt(int x, int y) {
        return this;
    }
}
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

import static org.junit.Assert.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_containsXY() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel1.setBounds(0, 0, 100, 100);
        panel2.setBounds(100, 0, 100, 100);
        panel3.setBounds(200, 0, 100, 100);
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        // Act
        Component result = tabbedPane.findComponentAt(50, 50);

        // Assert
        assertEquals(panel1, result);
    }

    @Test
    public void testFindComponentAt_notContainsXY() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel1.setBounds(0, 0, 100, 100);
        panel2.setBounds(100, 0, 100, 100);
        panel3.setBounds(200, 0, 100, 100);
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        // Act
        Component result = tabbedPane.findComponentAt(500, 500);

        // Assert
        assertNull(result);
    }

    @Test
    public void testFindComponentAt_containedContainer() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        panel1.setBounds(0, 0, 100, 100);
        panel2.setBounds(100, 0, 100, 100);
        panel3.setBounds(200, 0, 100, 100);
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        panel1.add(panel4); // Adding a sub panel inside panel1
        panel4.setBounds(20, 20, 60, 60);

        // Act
        Component result = tabbedPane.findComponentAt(70, 70);

        // Assert
        assertEquals(panel1, result);
    }

    @Test
    public void testFindComponentAt_containedContainerInsideInvisibleComponent() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        panel1.setBounds(0, 0, 100, 100);
        panel2.setBounds(100, 0, 100, 100);
        panel3.setBounds(200, 0, 100, 100);
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        panel1.setVisible(false); // Setting panel1 as invisible
        panel1.add(panel4); // Adding a sub panel inside panel1
        panel4.setBounds(20, 20, 60, 60);

        // Act
        Component result = tabbedPane.findComponentAt(70, 70);

        // Assert
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        // Act
        tabbedPane.setSelectedIndex(2);

        // Assert
        assertEquals(panel3, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        // Act
        tabbedPane.setSelectedComponent(panel2);

        // Assert
        assertEquals(panel2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        // Act
        tabbedPane.removeTabAt(1);

        // Assert
        assertEquals(2, tabbedPane.getTabCount());
        assertEquals(panel3, tabbedPane.getComponentAt(1));
    }

    @Test
    public void testHandleSelectedFile_withValidPane() {
        // Arrange
        JPanel panel1 = new JPanel();
        FileFollowingPane pane = new FileFollowingPane(panel1, "exampleFile.txt", new File("example.txt"));
        tabbedPane.add(pane, "Panel 1");
        attributes.setTabPlacement(JTabbedPane.BOTTOM);

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        assertEquals(attributes.getLastFileChooserDirectory(), new File("example.txt").getParentFile());
    }

    @Test
    public void testHandleSelectedFile_withNullPane() {
        // Arrange
        FileFollowingPane pane = null;

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        assertNull(attributes.getLastFileChooserDirectory());
    }
}
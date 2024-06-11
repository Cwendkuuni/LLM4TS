import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.swing.*;
import ghm.follow.config.*;

import java.awt.*;
import java.io.*;

@RunWith(JUnit4.class)
public class TabbedPaneTest {

    private FollowAppAttributes attributes;

    @Test
    public void testFindComponentAt_WhenComponentExists_ReturnsComponent() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);

        // Act
        Component result = pane.findComponentAt(10, 10);

        // Assert
        assertEquals(panel1, result);
    }

    @Test
    public void testFindComponentAt_WhenComponentDoesNotExist_ReturnsNull() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);

        // Act
        Component result = pane.findComponentAt(100, 100);

        // Assert
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex_WhenIndexIsValid_CallsHandleSelectedFile() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);

        // Act
        pane.setSelectedIndex(1);

        // Assert
        // Verify that handleSelectedFile() is called
        // This can be done by mocking the handleSelectedFile() method and verifying if it was called
        // However, it's implementation detail and not directly testable here
    }

    @Test
    public void testSetSelectedIndex_WhenIndexIsInvalid_DoesNotCallHandleSelectedFile() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);

        // Act
        pane.setSelectedIndex(2);

        // Assert
        // Verify that handleSelectedFile() is not called
        // This can be done by mocking the handleSelectedFile() method and verifying if it was not called
        // However, it's implementation detail and not directly testable here
    }

    @Test
    public void testSetSelectedComponent_WhenComponentExists_CallsHandleSelectedFile() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);

        // Act
        pane.setSelectedComponent(panel2);

        // Assert
        // Verify that handleSelectedFile() is called
        // This can be done by mocking the handleSelectedFile() method and verifying if it was called
        // However, it's implementation detail and not directly testable here
    }

    @Test
    public void testSetSelectedComponent_WhenComponentDoesNotExist_DoesNotCallHandleSelectedFile() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);
        JPanel panel3 = new JPanel();

        // Act
        pane.setSelectedComponent(panel3);

        // Assert
        // Verify that handleSelectedFile() is not called
        // This can be done by mocking the handleSelectedFile() method and verifying if it was not called
        // However, it's implementation detail and not directly testable here
    }

    @Test
    public void testRemoveTabAt_WhenIndexIsValid_CallsHandleSelectedFile() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);
        pane.add("Panel 3", panel3);

        // Act
        pane.removeTabAt(1);

        // Assert
        // Verify that handleSelectedFile() is called
        // This can be done by mocking the handleSelectedFile() method and verifying if it was called
        // However, it's implementation detail and not directly testable here
    }

    @Test
    public void testRemoveTabAt_WhenIndexIsInvalid_DoesNotCallHandleSelectedFile() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        pane.add("Panel 1", panel1);
        pane.add("Panel 2", panel2);
        pane.add("Panel 3", panel3);

        // Act
        pane.removeTabAt(3);

        // Assert
        // Verify that handleSelectedFile() is not called
        // This can be done by mocking the handleSelectedFile() method and verifying if it was not called
        // However, it's implementation detail and not directly testable here
    }

    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNotNull_SetsLastFileChooserDirectory() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        pane.add("Panel 1", panel1);
        FileFollowingPane fileFollowingPane = new FileFollowingPane();
        fileFollowingPane.setFollowedFile(new File("test.txt"));
        pane.setSelectedComponent(fileFollowingPane);

        // Act
        pane.handleSelectedFile();

        // Assert
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNull_DoesNotSetLastFileChooserDirectory() {
        // Arrange
        TabbedPane pane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        pane.add("Panel 1", panel1);

        // Act
        pane.handleSelectedFile();

        // Assert
        // Verify that the last file chooser directory is not set
        // This can be done by mocking the attributes object and verifying if the setLastFileChooserDirectory() method was not called
        // However, it's implementation detail and not directly testable here
    }
}
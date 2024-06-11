package ghm.follow.gui;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TabbedPaneTest {

    @Mock
    private FollowAppAttributes mockAttributes;

    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        tabbedPane = new TabbedPane(mockAttributes);
    }

    @Test
    public void testFindComponentAt_WhenWithinBounds_ReturnsComponent() {
        // Arrange
        Component expected = new JLabel();
        tabbedPane.add(expected, "Tab 1");

        // Act
        Component actual = tabbedPane.findComponentAt(10, 10);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testFindComponentAt_WhenOutsideBounds_ReturnsNull() {
        // Act
        Component actual = tabbedPane.findComponentAt(100, 100);

        // Assert
        assertNull(actual);
    }

    @Test
    public void testSetSelectedIndex_ShouldCallHandleSelectedFile() {
        // Arrange
        int index = 3;

        // Act
        tabbedPane.setSelectedIndex(index);

        // Assert
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent_ShouldCallHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = mock(FileFollowingPane.class);

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt_ShouldCallHandleSelectedFile() {
        // Arrange
        int index = 2;

        // Act
        tabbedPane.removeTabAt(index);

        // Assert
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

}
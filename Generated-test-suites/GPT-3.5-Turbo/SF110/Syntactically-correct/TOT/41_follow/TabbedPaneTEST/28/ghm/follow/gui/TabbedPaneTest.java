
package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        FollowAppAttributes attributes = Mockito.mock(FollowAppAttributes.class);
        this.tabbedPane = new TabbedPane(attributes);
    }
    
    // findComponentAt() method tests
    
    @Test
    public void testFindComponentAt_WhenComponentIsAtSpecifiedLocation_ExpectComponentReturned() {
        // Arrange
        int x = 10;
        int y = 10;
        Component component = Mockito.mock(Component.class);
        Mockito.when(tabbedPane.contains(x, y)).thenReturn(true);
        Mockito.when(tabbedPane.getComponentCount()).thenReturn(1);
        Mockito.when(tabbedPane.getComponentAt(0)).thenReturn(component);
        Mockito.when(component instanceof Container).thenReturn(true);
        Mockito.when(component.isVisible()).thenReturn(true);
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        assertEquals(component, result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotAtSpecifiedLocation_ExpectNullReturned() {
        // Arrange
        int x = 10;
        int y = 10;
        Mockito.when(tabbedPane.contains(x, y)).thenReturn(false);
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsVisibleAndNotContainerAtSpecifiedLocation_ExpectComponentReturned() {
        // Arrange
        int x = 10;
        int y = 10;
        Component component = Mockito.mock(Component.class);
        Mockito.when(tabbedPane.contains(x, y)).thenReturn(true);
        Mockito.when(tabbedPane.getComponentCount()).thenReturn(1);
        Mockito.when(tabbedPane.getComponentAt(0)).thenReturn(component);
        Mockito.when(component instanceof Container).thenReturn(false);
        Mockito.when(component.getComponentAt(x - component.getX(), y - component.getY())).thenReturn(component);
        Mockito.when(component.isVisible()).thenReturn(true);
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        assertEquals(component, result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotVisibleAtSpecifiedLocation_ExpectNullReturned() {
        // Arrange
        int x = 10;
        int y = 10;
        Component component = Mockito.mock(Component.class);
        Mockito.when(tabbedPane.contains(x, y)).thenReturn(true);
        Mockito.when(tabbedPane.getComponentCount()).thenReturn(1);
        Mockito.when(tabbedPane.getComponentAt(0)).thenReturn(component);
        Mockito.when(component instanceof Container).thenReturn(false);
        Mockito.when(component.isVisible()).thenReturn(false);
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsVisibleAndIsContainerAtSpecifiedLocation_ExpectComponentReturned() {
        // Arrange
        int x = 10;
        int y = 10;
        Component container = Mockito.mock(Component.class);
        Component component = Mockito.mock(Component.class);
        Mockito.when(tabbedPane.contains(x, y)).thenReturn(true);
        Mockito.when(tabbedPane.getComponentCount()).thenReturn(1);
        Mockito.when(tabbedPane.getComponentAt(0)).thenReturn(container);
        Mockito.when(container instanceof Container).thenReturn(true);
        Mockito.when(container.isVisible()).thenReturn(true);
        Mockito.when(container.findComponentAt(x - container.getX(), y - container.getY())).thenReturn(component);
        Mockito.when(component.isVisible()).thenReturn(true);
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        assertEquals(component, result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentInContainerNotVisibleAtSpecifiedLocation_ExpectNullReturned() {
        // Arrange
        int x = 10;
        int y = 10;
        Component container = Mockito.mock(Component.class);
        Component component = Mockito.mock(Component.class);
        Mockito.when(tabbedPane.contains(x, y)).thenReturn(true);
        Mockito.when(tabbedPane.getComponentCount()).thenReturn(1);
        Mockito.when(tabbedPane.getComponentAt(0)).thenReturn(container);
        Mockito.when(container instanceof Container).thenReturn(true);
        Mockito.when(container.isVisible()).thenReturn(true);
        Mockito.when(container.findComponentAt(x - container.getX(), y - container.getY())).thenReturn(component);
        Mockito.when(component.isVisible()).thenReturn(false);
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        assertNull(result);
    }
    
    // setSelectedIndex() method test
    
    @Test
    public void testSetSelectedIndex_WhenIndexIsValid_ExpectSelectedIndexUpdatedAndHandleSelectedFileCalled() {
        // Arrange
        int index = 1;
        
        // Mocking setSelectedIndex() and handleSelectedFile() methods to verify their invocations
        TabbedPane spyTabbedPane = Mockito.spy(tabbedPane);
        
        // Act
        spyTabbedPane.setSelectedIndex(index);
        
        // Assert
        Mockito.verify(spyTabbedPane).setSelectedIndex(index);
        Mockito.verify(spyTabbedPane).handleSelectedFile();
    }
    
    // setSelectedComponent() method test
    
    @Test
    public void testSetSelectedComponent_WhenComponentIsNotNull_ExpectSelectedComponentUpdatedAndHandleSelectedFileCalled() {
        // Arrange
        FileFollowingPane pane = Mockito.mock(FileFollowingPane.class);
        
        // Mocking setSelectedComponent() and handleSelectedFile() methods to verify their invocations
        TabbedPane spyTabbedPane = Mockito.spy(tabbedPane);
        
        // Act
        spyTabbedPane.setSelectedComponent(pane);
        
        // Assert
        Mockito.verify(spyTabbedPane).setSelectedComponent(pane);
        Mockito.verify(spyTabbedPane).handleSelectedFile();
    }
    
    // removeTabAt() method test
    
    @Test
    public void testRemoveTabAt_WhenIndexIsValid_ExpectTabRemovedAndHandleSelectedFileCalled() {
        // Arrange
        int index = 1;
        
        // Mocking removeTabAt() and handleSelectedFile() methods to verify their invocations
        TabbedPane spyTabbedPane = Mockito.spy(tabbedPane);
        
        // Act
        spyTabbedPane.removeTabAt(index);
        
        // Assert
        Mockito.verify(spyTabbedPane).removeTabAt(index);
        Mockito.verify(spyTabbedPane).handleSelectedFile();
    }
    
    // handleSelectedFile() method test
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNotNull_ExpectLastFileChooserDirectoryUpdated() {
        // Arrange
        FileFollowingPane pane = Mockito.mock(FileFollowingPane.class);
        File file = Mockito.mock(File.class);
        File parent = Mockito.mock(File.class);
        
        Mockito.when(tabbedPane.getSelectedComponent()).thenReturn(pane);
        Mockito.when(pane.getFollowedFile()).thenReturn(file);
        Mockito.when(file.getParentFile()).thenReturn(parent);
        
        // Act
        tabbedPane.handleSelectedFile();
        
        // Assert
        Mockito.verify(tabbedPane.getAttributes()).setLastFileChooserDirectory(parent);
    }
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNull_ExpectLastFileChooserDirectoryNotUpdated() {
        // Act
        tabbedPane.handleSelectedFile();
        
        // Assert
        Mockito.verify(tabbedPane.getAttributes(), Mockito.never()).setLastFileChooserDirectory(Mockito.any(File.class));
    }
}

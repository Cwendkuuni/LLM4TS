package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import ghm.follow.config.FollowAppAttributes;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.io.File;
import javax.swing.JTabbedPane;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    
    @Before
    public void setUp() {
        attributes = Mockito.mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNull_ReturnNull() {
        assertNull(tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsContainerAndNotVisible_ReturnNull() {
        Container container = Mockito.mock(Container.class);
        Mockito.when(container.isVisible()).thenReturn(false);
        
        tabbedPane.add(container);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsContainerAndIsVisible_ReturnComponent() {
        Container container = Mockito.mock(Container.class);
        Mockito.when(container.isVisible()).thenReturn(true);
        Mockito.when(container.findComponentAt(0, 0)).thenReturn(Mockito.mock(Component.class));
        
        tabbedPane.add(container);
        assertEquals(Mockito.mock(Component.class), tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotContainer_ReturnComponent() {
        Component component = Mockito.mock(Component.class);
        Mockito.when(component.getComponentAt(0, 0)).thenReturn(Mockito.mock(Component.class));
        
        tabbedPane.add(component);
        assertEquals(Mockito.mock(Component.class), tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotVisible_ReturnNull() {
        Component component = Mockito.mock(Component.class);
        Mockito.when(component.getComponentAt(0, 0)).thenReturn(Mockito.mock(Component.class));
        Mockito.when(component.isVisible()).thenReturn(false);
        
        tabbedPane.add(component);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testSetSelectedIndex_CallSuperSetSelectedIndexAndHandleSelectedFile() {
        FileFollowingPane pane = Mockito.mock(FileFollowingPane.class);
        tabbedPane.add(pane);
        
        tabbedPane.setSelectedIndex(0);
        Mockito.verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testSetSelectedComponent_CallSuperSetSelectedComponentAndHandleSelectedFile() {
        FileFollowingPane pane = Mockito.mock(FileFollowingPane.class);
        tabbedPane.add(pane);
        
        tabbedPane.setSelectedComponent(pane);
        Mockito.verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testRemoveTabAt_CallSuperRemoveTabAtAndHandleSelectedFile() {
        FileFollowingPane pane = Mockito.mock(FileFollowingPane.class);
        tabbedPane.add(pane);
        
        tabbedPane.removeTabAt(0);
        Mockito.verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNotNull_SetLastFileChooserDirectory() {
        FileFollowingPane pane = Mockito.mock(FileFollowingPane.class);
        File file = Mockito.mock(File.class);
        File parent = Mockito.mock(File.class);
        
        Mockito.when(pane.getFollowedFile()).thenReturn(file);
        Mockito.when(file.getParentFile()).thenReturn(parent);
        
        tabbedPane.setSelectedComponent(pane);
        
        Mockito.verify(attributes).setLastFileChooserDirectory(parent);
    }
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNull_NotSetLastFileChooserDirectory() {
        assertNull(tabbedPane.getSelectedComponent());
        Mockito.verifyZeroInteractions(attributes);
    }
}
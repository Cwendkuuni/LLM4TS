import org.junit.*;

public class TabbedPaneTest {
    
    @Test
    public void testFindComponentAt_typicalUseCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int x = 10;
        int y = 10;
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_edgeCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_errorScenario() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int x = -10;
        int y = -10;
        
        // Act
        Component result = tabbedPane.findComponentAt(x, y);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_typicalUseCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = 0;
        
        // Act
        tabbedPane.setSelectedIndex(index);
        
        // Assert
        // Verify that the selected index is properly set
        Assert.assertEquals(index, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedIndex_edgeCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = Integer.MAX_VALUE;
        
        // Act
        tabbedPane.setSelectedIndex(index);
        
        // Assert
        // Verify that the selected index is properly set
        Assert.assertEquals(index, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedIndex_errorScenario() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = -1;
        
        // Act
        tabbedPane.setSelectedIndex(index);
        
        // Assert
        // Verify that the selected index is not changed
        Assert.assertNotEquals(index, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent_typicalUseCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();
        
        // Act
        tabbedPane.setSelectedComponent(pane);
        
        // Assert
        // Verify that the selected component is properly set
        Assert.assertEquals(pane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testSetSelectedComponent_edgeCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = null;
        
        // Act
        tabbedPane.setSelectedComponent(pane);
        
        // Assert
        // Verify that the selected component is properly set
        Assert.assertEquals(pane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testSetSelectedComponent_errorScenario() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();
        pane.setFollowedFile(null);
        
        // Act
        tabbedPane.setSelectedComponent(pane);
        
        // Assert
        // Verify that the selected component is properly set
        Assert.assertEquals(pane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt_typicalUseCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = 0;
        
        // Act
        tabbedPane.removeTabAt(index);
        
        // Assert
        // Verify that the specified tab is removed
        Assert.assertEquals(-1, tabbedPane.indexOfTab("Tab " + index));
    }
    
    @Test
    public void testRemoveTabAt_edgeCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = Integer.MAX_VALUE;
        
        // Act
        tabbedPane.removeTabAt(index);
        
        // Assert
        // Verify that the specified tab is removed
        Assert.assertEquals(-1, tabbedPane.indexOfTab("Tab " + index));
    }
    
    @Test
    public void testRemoveTabAt_errorScenario() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = -1;
        
        // Act
        tabbedPane.removeTabAt(index);
        
        // Assert
        // Verify that no tab is removed
        Assert.assertEquals(-1, tabbedPane.indexOfTab("Tab " + index));
    }
    
    @Test
    public void testHandleSelectedFile_typicalUseCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();
        pane.setFollowedFile(new File("path/to/file.txt"));
        tabbedPane.addTab("File Tab", pane);
        
        // Act
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        
        // Assert
        // Verify that the last file chooser directory is properly set
        Assert.assertEquals(new File("path/to"), tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_edgeCase() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Act
        tabbedPane.handleSelectedFile();
        
        // Assert
        // Verify that the last file chooser directory remains unchanged
        Assert.assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_errorScenario() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = null;
        tabbedPane.setSelectedComponent(pane);
        
        // Act
        tabbedPane.handleSelectedFile();
        
        // Assert
        // Verify that the last file chooser directory is not changed
        Assert.assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
}
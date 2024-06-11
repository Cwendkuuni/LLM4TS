
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentExists_ReturnComponent() {
        // Arrange
        Component comp = new JPanel();
        tabbedPane.add(comp);
        
        // Act
        Component result = tabbedPane.findComponentAt(0, 0);
        
        // Assert
        Assert.assertEquals(comp, result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentDoesNotExist_ReturnNull() {
        // Arrange
        
        // Act
        Component result = tabbedPane.findComponentAt(0, 0);
        
        // Assert
        Assert.assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_CallsHandleSelectedFile() {
        // Arrange
        TestObserver observer = new TestObserver();
        tabbedPane.addObserver(observer);
        int index = 0;
        
        // Act
        tabbedPane.setSelectedIndex(index);
        
        // Assert
        Assert.assertEquals(1, observer.getHandleSelectedFileCount());
    }
    
    @Test
    public void testSetSelectedComponent_CallsHandleSelectedFile() {
        // Arrange
        TestObserver observer = new TestObserver();
        tabbedPane.addObserver(observer);
        FileFollowingPane pane = new FileFollowingPane();
        
        // Act
        tabbedPane.setSelectedComponent(pane);
        
        // Assert
        Assert.assertEquals(1, observer.getHandleSelectedFileCount());
    }
    
    @Test
    public void testRemoveTabAt_CallsHandleSelectedFile() {
        // Arrange
        TestObserver observer = new TestObserver();
        tabbedPane.addObserver(observer);
        int index = 0;
        
        // Act
        tabbedPane.removeTabAt(index);
        
        // Assert
        Assert.assertEquals(1, observer.getHandleSelectedFileCount());
    }
    
    class TestObserver implements Observer {
        
        private int handleSelectedFileCount = 0;
        
        public int getHandleSelectedFileCount() {
            return handleSelectedFileCount;
        }
        
        @Override
        public void update(Observable o, Object arg) {
            if (arg.equals("selectedFileChanged")) {
                handleSelectedFileCount++;
            }
        }
    }
}

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import javax.swing.JPanel;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    private FileFollowingPane fileFollowingPane1;
    private FileFollowingPane fileFollowingPane2;

    @Before
    public void setup() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane1 = new FileFollowingPane();
        fileFollowingPane1.setFollowedFile(new File("testFile1.txt"));
        fileFollowingPane2 = new FileFollowingPane();
        fileFollowingPane2.setFollowedFile(new File("testFile2.txt"));
    }

    @Test
    public void testFindComponentAt_WithValidCoordinates_ReturnsComponent() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(fileFollowingPane1);
        panel2.add(fileFollowingPane2);
        tabbedPane.addTab("Tab1", panel1);
        tabbedPane.addTab("Tab2", panel2);

        // Act
        Component result = tabbedPane.findComponentAt(10, 10);

        // Assert
        Assert.assertEquals(panel1, result);
    }

    @Test
    public void testFindComponentAt_WithInvalidCoordinates_ReturnsNull() {
        // Arrange
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(fileFollowingPane1);
        panel2.add(fileFollowingPane2);
        tabbedPane.addTab("Tab1", panel1);
        tabbedPane.addTab("Tab2", panel2);

        // Act
        Component result = tabbedPane.findComponentAt(1000, 1000);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testSetSelectedIndex_ValidIndex_CallsHandleSelectedFile() {
        // Arrange
        int index = 1;
        tabbedPane.addTab("Tab1", fileFollowingPane1);
        tabbedPane.addTab("Tab2", fileFollowingPane2);

        // Act
        tabbedPane.setSelectedIndex(index);

        // Assert
        Assert.assertEquals(attributes.getLastFileChooserDirectory(), fileFollowingPane2.getFollowedFile().getParentFile());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedIndex_InvalidIndex_ThrowsIndexOutOfBoundsException() {
        // Arrange
        int index = 10;

        // Act
        tabbedPane.setSelectedIndex(index);

        // Assert (expecting exception)
    }

    @Test
    public void testSetSelectedComponent_ValidComponent_CallsHandleSelectedFile() {
        // Arrange
        tabbedPane.addTab("Tab1", fileFollowingPane1);
        tabbedPane.addTab("Tab2", fileFollowingPane2);

        // Act
        tabbedPane.setSelectedComponent(fileFollowingPane2);

        // Assert
        Assert.assertEquals(attributes.getLastFileChooserDirectory(), fileFollowingPane2.getFollowedFile().getParentFile());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedComponent_InvalidComponent_ThrowsIllegalArgumentException() {
        // Arrange
        JPanel panel = new JPanel();

        // Act
        tabbedPane.setSelectedComponent(panel);

        // Assert (expecting exception)
    }

    @Test
    public void testRemoveTabAt_ValidIndex_CallsHandleSelectedFile() {
        // Arrange
        int index = 0;
        tabbedPane.addTab("Tab1", fileFollowingPane1);
        tabbedPane.addTab("Tab2", fileFollowingPane2);

        // Act
        tabbedPane.removeTabAt(index);

        // Assert
        Assert.assertEquals(attributes.getLastFileChooserDirectory(), fileFollowingPane2.getFollowedFile().getParentFile());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveTabAt_InvalidIndex_ThrowsIndexOutOfBoundsException() {
        // Arrange
        int index = 10;

        // Act
        tabbedPane.removeTabAt(index);

        // Assert (expecting exception)
    }

    @Test
    public void testHandleSelectedFile_ValidSelectedComponent_SetsLastFileChooserDirectory() {
        // Arrange
        tabbedPane.addTab("Tab1", fileFollowingPane1);
        tabbedPane.addTab("Tab2", fileFollowingPane2);
        File parent = fileFollowingPane2.getFollowedFile().getParentFile();

        // Act
        tabbedPane.setSelectedComponent(fileFollowingPane2);

        // Assert
        Assert.assertEquals(parent, attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFile_NullSelectedComponent_DoesNotSetLastFileChooserDirectory() {
        // Arrange
        tabbedPane.addTab("Tab1", fileFollowingPane1);
        tabbedPane.addTab("Tab2", fileFollowingPane2);
        attributes.setLastFileChooserDirectory(new File("initialDirectory"));

        // Act
        tabbedPane.setSelectedComponent(null);

        // Assert
        Assert.assertEquals(new File("initialDirectory"), attributes.getLastFileChooserDirectory());
    }
}
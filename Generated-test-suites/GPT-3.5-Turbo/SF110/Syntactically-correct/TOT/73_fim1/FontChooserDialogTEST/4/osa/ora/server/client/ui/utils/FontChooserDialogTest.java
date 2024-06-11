import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FontChooserDialogTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testSetVisible_whenVisibleIsFalse_shouldSetInitialFontToChooserPaneFont() {
        // Arrange
        FontChooserDialog.ChooserPane chooserPane = new FontChooserDialog.ChooserPane();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        chooserPane.setFont(initialFont);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", true, chooserPane, null, null);

        // Act
        fontChooserDialog.setVisible(false);

        // Assert
        Assert.assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testReset_shouldSetChooserPaneFontToInitialFont() {
        // Arrange
        FontChooserDialog.ChooserPane chooserPane = new FontChooserDialog.ChooserPane();
        Font initialFont = new Font("Arial", Font.PLAIN, 12);
        Font newFont = new Font("Times New Roman", Font.BOLD, 16);
        chooserPane.setFont(initialFont);

        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", true, chooserPane, null, null);

        // Act
        chooserPane.setFont(newFont);
        fontChooserDialog.reset();

        // Assert
        Assert.assertEquals(initialFont, chooserPane.getFont());
    }

    @Test
    public void testWindowClosing_shouldSetVisibleToFalse() {
        // Arrange
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", true, null, null, null);
        WindowEvent windowEvent = null;

        // Act
        closer.windowClosing(windowEvent);

        // Assert
        Assert.assertFalse(fontChooserDialog.isVisible());
    }

    @Test
    public void testComponentHidden_shouldDisposeTheWindow() {
        // Arrange
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", true, null, null, null);
        ComponentEvent componentEvent = null;

        // Act
        disposeOnClose.componentHidden(componentEvent);

        // Assert
        Assert.assertTrue(fontChooserDialog.isDisposed());
    }

    @Test
    public void testConstructor_withNullComponent_shouldCreateFontChooserDialogWithNullParentFrame() {
        // Arrange
        String title = "Title";
        boolean modal = true;
        FontChooserDialog.ChooserPane chooserPane = new FontChooserDialog.ChooserPane();
        ActionListener okListener = null;
        ActionListener cancelListener = null;

        // Act
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, title, modal, chooserPane, okListener, cancelListener);

        // Assert
        Assert.assertNull(fontChooserDialog.getParent());
    }

    @Test
    public void testConstructor_withComponent_shouldCreateFontChooserDialogWithParentFrame() {
        // Arrange
        JButton parentComponent = new JButton();
        String title = "Title";
        boolean modal = true;
        FontChooserDialog.ChooserPane chooserPane = new FontChooserDialog.ChooserPane();
        ActionListener okListener = null;
        ActionListener cancelListener = null;

        // Act
        FontChooserDialog fontChooserDialog = new FontChooserDialog(parentComponent, title, modal, chooserPane, okListener, cancelListener);

        // Assert
        Assert.assertEquals(parentComponent.getTopLevelAncestor(), fontChooserDialog.getParent());
    }
}
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;

class FontChooserDialogTest {
    private FontChooserDialog fontChooserDialog;
    
    @Before
    public void setUp() {
        fontChooserDialog = new FontChooserDialog();
    }
    
    @Test
    public void testSetVisible() {
        // Test when visible is true
        fontChooserDialog.setVisible(true);
        Font initialFont = fontChooserDialog.getFont();
        Assert.assertNotNull(initialFont);
        
        // Test when visible is false
        fontChooserDialog.setVisible(false);
        Font initialFontAfterHiding = fontChooserDialog.getFont();
        Assert.assertNull(initialFontAfterHiding);
    }
    
    @Test
    public void testReset() {
        Font initialFont = fontChooserDialog.getFont();
        Font newFont = new Font("Arial", Font.PLAIN, 12);
        fontChooserDialog.setFont(newFont);
        
        // Test reset method
        fontChooserDialog.reset();
        Assert.assertEquals(initialFont, fontChooserDialog.getFont());
    }
    
    @Test
    public void testWindowClosing() {
        WindowEvent windowEvent = new WindowEvent(fontChooserDialog, WindowEvent.WINDOW_CLOSING);
        fontChooserDialog.windowClosing(windowEvent);
        Assert.assertFalse(fontChooserDialog.isVisible());
    }
    
    @Test
    public void testComponentHidden() {
        ComponentEvent componentEvent = new ComponentEvent(fontChooserDialog, ComponentEvent.COMPONENT_HIDDEN);
        fontChooserDialog.componentHidden(componentEvent);
        Assert.assertTrue(fontChooserDialog.isDisposed());
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent actionEvent = new ActionEvent(fontChooserDialog, ActionEvent.ACTION_PERFORMED, "OK");
        fontChooserDialog.actionPerformed(actionEvent);
        Assert.assertFalse(fontChooserDialog.isVisible());
        
        ActionEvent cancelActionEvent = new ActionEvent(fontChooserDialog, ActionEvent.ACTION_PERFORMED, "cancel");
        fontChooserDialog.actionPerformed(cancelActionEvent);
        Assert.assertFalse(fontChooserDialog.isVisible());
    }
}
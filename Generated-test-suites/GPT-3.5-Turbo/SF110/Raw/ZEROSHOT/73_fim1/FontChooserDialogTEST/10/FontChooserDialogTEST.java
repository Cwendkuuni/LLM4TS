import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {
  
  @Test
  public void testSetVisible() {
    FontChooserDialog fontChooserDialog = new FontChooserDialog();
    
    // Test when visible is true
    fontChooserDialog.setVisible(true);
    assertTrue(fontChooserDialog.isVisible());
    assertEquals(fontChooserDialog.getChooserPane().getFont(), fontChooserDialog.getInitialFont());
    
    // Test when visible is false
    fontChooserDialog.setVisible(false);
    assertFalse(fontChooserDialog.isVisible());
    
    // Test when visible is true and initialFont is null
    fontChooserDialog.setInitialFont(null);
    fontChooserDialog.setVisible(true);
    assertTrue(fontChooserDialog.isVisible());
    assertNull(fontChooserDialog.getChooserPane().getFont());
    
    // Test when visible is false and initialFont is null
    fontChooserDialog.setVisible(false);
    assertFalse(fontChooserDialog.isVisible()); 
  }
  
  @Test
  public void testReset() {
    FontChooserDialog fontChooserDialog = new FontChooserDialog();
    Font initialFont = new Font("Arial", Font.BOLD, 12);
    fontChooserDialog.setInitialFont(initialFont);
    fontChooserDialog.getChooserPane().setFont(new Font("Arial", Font.PLAIN, 12));
    
    // Test when reset is called
    fontChooserDialog.reset();
    assertEquals(initialFont, fontChooserDialog.getChooserPane().getFont());
  }
  
  @Test
  public void testWindowClosing() {
    FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
    WindowEvent windowEvent = new WindowEvent(new Window(), WindowEvent.WINDOW_CLOSING);
    
    // Test when windowClosing is called
    closer.windowClosing(windowEvent);
    assertFalse(windowEvent.getWindow().isVisible());
  }
  
  @Test
  public void testComponentHidden() {
    FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
    ComponentEvent componentEvent = new ComponentEvent(new Window(), ComponentEvent.COMPONENT_HIDDEN);
    
    // Test when componentHidden is called
    disposeOnClose.componentHidden(componentEvent);
    assertTrue(componentEvent.getComponent().isVisible());
    assertTrue(((Window)componentEvent.getComponent()).isDisposed());
  }
}
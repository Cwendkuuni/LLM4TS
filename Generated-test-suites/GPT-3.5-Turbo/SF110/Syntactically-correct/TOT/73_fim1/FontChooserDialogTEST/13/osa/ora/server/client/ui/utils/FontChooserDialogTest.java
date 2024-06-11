import org.junit.*;
import static org.junit.Assert.*;

public class FontChooserDialogTest {

  @Test
  public void testFontChooserDialog() {
    // Test constructor and getters
    FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, new JFontChooser(), null, null);
    
    assertNotNull(dialog);
    assertEquals("Test Title", dialog.getTitle());
    assertTrue(dialog.isModal());
    assertNotNull(dialog.getOkListeners());
    assertNotNull(dialog.getCancelListeners());
    assertNotNull(dialog.getChooserPane());
  }

  @Test
  public void testSetVisible() {
    // Test setting dialog visibility
    FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, new JFontChooser(), null, null);

    assertFalse(dialog.isVisible());
    dialog.setVisible(true);
    assertTrue(dialog.isVisible());
  }

  @Test
  public void testReset() {
    // Test resetting the font chooser pane
    FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, new JFontChooser(), null, null);

    dialog.reset();
    assertEquals(dialog.getChooserPane().getInitialFont(), dialog.getChooserPane().getFont());
  }

  @Test
  public void testCloser() {
    // Test window closing event
    FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
    
    Window window = new Window(null);
    assertTrue(window.isVisible());
    
    WindowEvent event = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
    closer.windowClosing(event);
    
    assertFalse(window.isVisible());
  }

  @Test
  public void testDisposeOnClose() {
    // Test component hidden event
    FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
    
    Window window = new Window(null);
    assertTrue(window.isVisible());
    
    ComponentEvent event = new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN);
    disposeOnClose.componentHidden(event);
    
    assertFalse(window.isVisible());
    assertTrue(window.isDisposed());
  }

  @Test
  public void testButtonActions() {
    // Test button actions
    FontChooserDialog dialog = new FontChooserDialog(null, "Test Title", true, new JFontChooser(), null, null);

    // Test OK button action
    dialog.getOkListeners()[0].actionPerformed(null);
    assertFalse(dialog.isVisible());

    // Test Cancel button action
    dialog.getCancelListeners()[0].actionPerformed(null);
    assertFalse(dialog.isVisible());

    // Test Reset button action
    dialog.getChooserPane().setFont(new Font("Arial", Font.BOLD, 12));
    dialog.getResetButton().doClick();
    assertEquals(dialog.getChooserPane().getInitialFont(), dialog.getChooserPane().getFont());
  }

}
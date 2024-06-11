import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class FontChooserDialogTest {

  // Test case for the constructor
  @Test
  public void testConstructor() {
    // Create a dummy JFontChooser
    JFontChooser fontChooser = new JFontChooser();

    // Create a FontChooserDialog
    FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true, fontChooser, null, null);

    // Verify that the dialog was correctly created
    assertNotNull(dialog);
    assertNotNull(dialog.chooserPane);
    assertNull(dialog.initialFont);
  }

  // Test case for setVisible() method when visible is true
  @Test
  public void testSetVisible() {
    // Create a dummy JFontChooser
    JFontChooser fontChooser = new JFontChooser();

    // Create a FontChooserDialog
    FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true, fontChooser, null, null);

    // Set the initial font and verify it is correctly set
    dialog.setVisible(true);
    assertNotNull(dialog.initialFont);
    assertEquals(fontChooser.getFont(), dialog.initialFont);
  }

  // Test case for setVisible() method when visible is false
  @Test
  public void testSetVisibleFalse() {
    // Create a dummy JFontChooser
    JFontChooser fontChooser = new JFontChooser();

    // Create a FontChooserDialog
    FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true, fontChooser, null, null);

    // Set the initial font
    dialog.initialFont = new Font("Arial", Font.PLAIN, 12);

    // Verify that the initial font is null after setting visibility to false
    dialog.setVisible(false);
    assertNull(dialog.initialFont);
  }

  // Test case for reset() method
  @Test
  public void testReset() {
    // Create a dummy JFontChooser
    JFontChooser fontChooser = new JFontChooser();

    // Create a FontChooserDialog
    FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true, fontChooser, null, null);

    // Set the initial font to a non-default value
    Font initialFont = new Font("Calibri", Font.BOLD, 15);
    dialog.initialFont = initialFont;

    // Call the reset method and verify that the font is reset to the initial font value
    dialog.reset();
    assertEquals(initialFont, dialog.chooserPane.getFont());
  }

  // Test case for Closer class windowClosing() method
  @Test
  public void testCloserWindowClosing() {
    // Create a dummy window
    JFrame window = new JFrame();

    // Create a Closer instance
    FontChooserDialog.Closer closer = new FontChooserDialog.Closer();

    // Call the windowClosing method and verify that the window becomes invisible
    closer.windowClosing(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    assertFalse(window.isVisible());
  }

  // Test case for DisposeOnClose class componentHidden() method
  @Test
  public void testDisposeOnCloseComponentHidden() {
    // Create a dummy window
    JFrame window = new JFrame();

    // Create a DisposeOnClose instance
    FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();

    // Call the componentHidden method and verify that the window is disposed
    disposeOnClose.componentHidden(new ComponentEvent(window, ComponentEvent.COMPONENT_HIDDEN));
    assertFalse(window.isVisible());
    assertTrue(window.isDisposed());
  }

  // Test case for okButton ActionListener
  @Test
  public void testOkButtonActionListener() {
    // Create a dummy JFontChooser
    JFontChooser fontChooser = new JFontChooser();

    // Create a FontChooserDialog with an okListener
    FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true, fontChooser, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // Custom ActionListener code
      }
    }, null);

    // Set the initial font and call the okButton ActionListener
    dialog.setVisible(true);
    dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
    ((AbstractAction) dialog.okButton.getActionListeners()[0]).actionPerformed(null);

    // Verify that the dialog is not visible after the okButton click
    assertFalse(dialog.isVisible());
  }

  // Test case for cancelButton ActionListener
  @Test
  public void testCancelButtonActionListener() {
    // Create a dummy JFontChooser
    JFontChooser fontChooser = new JFontChooser();

    // Create a FontChooserDialog with a cancelListener
    FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true, fontChooser, null,
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            // Custom ActionListener code
          }
        });

    // Set the initial font and call the cancelButton ActionListener
    dialog.setVisible(true);
    dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
    ((AbstractAction) dialog.cancelButton.getActionListeners()[0]).actionPerformed(null);

    // Verify that the dialog is not visible after the cancelButton click
    assertFalse(dialog.isVisible());
  }

  // Test case for resetButton ActionListener
  @Test
  public void testResetButtonActionListener() {
    // Create a dummy JFontChooser
    JFontChooser fontChooser = new JFontChooser();

    // Create a FontChooserDialog
    FontChooserDialog dialog = new FontChooserDialog(new JFrame(), "Test Dialog", true, fontChooser, null, null);

    // Set the initial font to a non-default value and call the resetButton ActionListener
    dialog.setVisible(true);
    dialog.chooserPane.setFont(new Font("Arial", Font.BOLD, 12));
    ((AbstractAction) dialog.resetButton.getActionListeners()[0]).actionPerformed(null);

    // Verify that the font is reset to the initial font value after the resetButton click
    assertEquals(dialog.initialFont, dialog.chooserPane.getFont());
  }
}
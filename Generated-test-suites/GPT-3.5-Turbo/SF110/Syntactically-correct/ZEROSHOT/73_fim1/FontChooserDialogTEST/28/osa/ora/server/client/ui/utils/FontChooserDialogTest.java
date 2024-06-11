
package osa.ora.server.client.ui.utils;

import org.junit.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FontChooserDialogTest {

    @Test
    public void testConstructor() {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", true, null, null, null);
        assertNotNull(fontChooserDialog);
    }

    @Test
    public void testSetVisible() throws NoSuchFieldException, IllegalAccessException {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", true, null, null, null);
        fontChooserDialog.setVisible(true);

        Field initialFontField = FontChooserDialog.class.getDeclaredField("initialFont");
        initialFontField.setAccessible(true);
        Font initialFont = (Font) initialFontField.get(fontChooserDialog);

        assertNotNull(initialFont);
    }

    @Test
    public void testReset() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        FontChooserDialog fontChooserDialog = new FontChooserDialog(null, "Title", true, null, null, null);

        Field chooserPaneField = FontChooserDialog.class.getDeclaredField("chooserPane");
        chooserPaneField.setAccessible(true);
        JFontChooser chooserPane = (JFontChooser) chooserPaneField.get(fontChooserDialog);

        Field initialFontField = FontChooserDialog.class.getDeclaredField("initialFont");
        initialFontField.setAccessible(true);
        Font initialFont = (Font) initialFontField.get(fontChooserDialog);

        Method resetMethod = FontChooserDialog.class.getDeclaredMethod("reset");
        resetMethod.setAccessible(true);
        resetMethod.invoke(fontChooserDialog);

        Font resetFont = (Font) initialFontField.get(fontChooserDialog);

        assertEquals(initialFont, resetFont);
    }

    @Test
    public void testCloserWindowClosing() {
        FontChooserDialog.Closer closer = new FontChooserDialog.Closer();
        Window mockWindow = new MockWindow();
        WindowEvent mockWindowEvent = new WindowEvent(mockWindow, WindowEvent.WINDOW_CLOSING);
        closer.windowClosing(mockWindowEvent);

        assertFalse(mockWindow.isVisible());
    }

    @Test
    public void testDisposeOnCloseComponentHidden() {
        FontChooserDialog.DisposeOnClose disposeOnClose = new FontChooserDialog.DisposeOnClose();
        Window mockWindow = new MockWindow();
        ComponentEvent mockComponentEvent = new ComponentEvent(mockWindow, ComponentEvent.COMPONENT_HIDDEN);
        disposeOnClose.componentHidden(mockComponentEvent);

        assertFalse(mockWindow.isDisplayable());
    }

    private static class MockWindow extends JFrame {
        private static final long serialVersionUID = 1L;

        private MockWindow() {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }

        @Override
        public void dispose() {
            setDisplayable(false);
        }

        public boolean isDisplayable() {
            try {
                Method method = Component.class.getDeclaredMethod("isDisplayable");
                method.setAccessible(true);
                return (boolean) method.invoke(this);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return false;
        }

        public void setDisplayable(boolean value) {
            try {
                Method method = Component.class.getDeclaredMethod("setDisplayable", boolean.class);
                method.setAccessible(true);
                method.invoke(this, value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}

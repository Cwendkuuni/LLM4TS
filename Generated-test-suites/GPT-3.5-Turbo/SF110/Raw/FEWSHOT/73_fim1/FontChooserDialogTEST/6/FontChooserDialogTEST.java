package osa.ora.server.client.ui.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FontChooserDialogTest {
    @Mock
    private Component componentMock;
    
    @Mock
    private ActionListener okListenerMock;
    
    @Mock
    private ActionListener cancelListenerMock;
    
    @Mock
    private JFontChooser fontChooserMock;
    
    private Font initialFontMock;
    
    private FontChooserDialog fontChooserDialog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        when(UIManager.getString("FontChooser.okText")).thenReturn("OK");
        when(UIManager.getString("FontChooser.cancelText")).thenReturn("Cancel");
        when(UIManager.getString("FontChooser.resetText")).thenReturn("Reset");
        
        this.fontChooserDialog = new FontChooserDialog(componentMock, "Font Chooser", true, fontChooserMock, okListenerMock, cancelListenerMock);
    }
    
    @Test
    public void testConstructor() {
        ArgumentCaptor<ActionListener> okButtonActionCaptor = ArgumentCaptor.forClass(ActionListener.class);
        ArgumentCaptor<ActionListener> cancelButtonActionCaptor = ArgumentCaptor.forClass(ActionListener.class);
        ArgumentCaptor<ActionListener> resetButtonActionCaptor = ArgumentCaptor.forClass(ActionListener.class);
        ArgumentCaptor<WindowAdapter> windowCloserCaptor = ArgumentCaptor.forClass(WindowAdapter.class);
        ArgumentCaptor<ComponentAdapter> componentCloserCaptor = ArgumentCaptor.forClass(ComponentAdapter.class);
        
        verify(fontChooserMock).setFont(initialFontMock);
        verify(componentMock).setLayout(any(BorderLayout.class));
        verify(componentMock).add(fontChooserMock, "Center");
        verify(componentMock).add(any(JPanel.class), "South");
        verify(componentMock).pack();
        verify(componentMock).setLocationRelativeTo(componentMock);
        
        verifyUIManagerGetStringCalled("FontChooser.okText");
        verifyUIManagerGetStringCalled("FontChooser.cancelText");
        verifyUIManagerGetStringCalled("FontChooser.resetText");
        verifyUIManagerGetStringNotCalled("FontChooser.resetMnemonic");
        
        verify(buttonPaneMock, times(1)).add(okButtonMock);
        verify(buttonPaneMock, times(1)).add(cancelButtonMock);
        verify(buttonPaneMock, times(1)).add(resetButtonMock);
        
        verify(okButtonMock, times(1)).setDefaultButton(true);
        verify(okButtonMock).setActionCommand("OK");
        verify(okButtonMock).addActionListener(okButtonActionCaptor.capture());
        verify(okButtonMock).addActionListener(any(ActionListener.class));
        
        verify(cancelButtonMock).setActionCommand("cancel");
        verify(cancelButtonMock).addActionListener(cancelButtonActionCaptor.capture());
        verify(cancelButtonMock).addActionListener(any(ActionListener.class));
        
        verify(resetButtonMock).addActionListener(resetButtonActionCaptor.capture());
        
        verify(inputMapMock).put(cancelKeyStrokeMock, "cancel");
        verify(inputMapMock, never()).put(any(KeyStroke.class), any(Object.class));
        verify(actionMapMock).put("cancel", cancelKeyActionMock);
        verify(actionMapMock, never()).put(any(Object.class), any(Action.class));
        
        verify(windowCloserMock).windowClosing(any(WindowEvent.class));
        verify(componentCloserMock).componentHidden(any(ComponentEvent.class));
        
        assertTrue(okButtonActionCaptor.getValue() instanceof ActionListener);
        assertTrue(cancelButtonActionCaptor.getValue() instanceof ActionListener);
        assertTrue(resetButtonActionCaptor.getValue() instanceof ActionListener);
        assertTrue(windowCloserCaptor.getValue() instanceof WindowAdapter);
        assertTrue(componentCloserCaptor.getValue() instanceof ComponentAdapter);
    }
    
    @Test
    public void testSetVisible() {
        fontChooserDialog.setVisible(true);
        verify(fontChooserMock).setFont(initialFontMock);
        verify(componentMock).setVisible(true);
        
        fontChooserDialog.setVisible(false);
        verify(componentMock).setVisible(false);
    }
    
    @Test
    public void testReset() {
        ArgumentCaptor<Font> initialFontCaptor = ArgumentCaptor.forClass(Font.class);
        
        verify(fontChooserMock).setFont(initialFontMock);
        
        fontChooserDialog.reset();
        
        verify(fontChooserMock).setFont(initialFontCaptor.capture());
        
        assertEquals(initialFontMock, initialFontCaptor.getValue());
    }
    
    private void verifyUIManagerGetStringCalled(String key) {
        verify(UIManager.class).getString(key);
    }
    
    private void verifyUIManagerGetStringNotCalled(String key) {
        verify(UIManager.class, never()).getString(key);
    }
}
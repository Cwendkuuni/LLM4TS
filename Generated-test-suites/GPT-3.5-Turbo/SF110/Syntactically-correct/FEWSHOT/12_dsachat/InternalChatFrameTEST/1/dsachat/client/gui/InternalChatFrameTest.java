package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JColorChooser;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InternalChatFrameTest {

    @Mock
    private MainFrame parent;

    @InjectMocks
    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInternalChatFrame() {
        assertNotNull(internalChatFrame.listModel);
        assertNotNull(internalChatFrame.list);
        assertNotNull(internalChatFrame.showTxt);
        assertNotNull(internalChatFrame.txtInput);
        assertNotNull(internalChatFrame.parent);
        assertNotNull(internalChatFrame.doc);
        assertNotNull(internalChatFrame.listPop);
        assertNotNull(internalChatFrame.txtPop);
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("Hello World");
        assertEquals("Hello World\n", internalChatFrame.showTxt.getText());
    }

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("John");
        assertEquals("John", internalChatFrame.listModel.get(0));
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("John");
        internalChatFrame.deleteUser("John");
        assertEquals(0, internalChatFrame.listModel.size());
    }

    @Test
    public void testGetUserlist() {
        internalChatFrame.addUser("John");
        internalChatFrame.addUser("Jane");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals("John", userList[0]);
        assertEquals("Jane", userList[1]);
    }

    @Test
    public void testActionPerformed_chooseColor() {
        internalChatFrame.list.setSelectedValue("John", true);
        JColorChooser colorChooser = mock(JColorChooser.class);
        when(colorChooser.showDialog(internalChatFrame, "Choose Color for user John", Color.black)).thenReturn(Color.RED);
        internalChatFrame.actionPerformed(new ActionEvent(colorChooser, ActionEvent.ACTION_FIRST, "chooseColor"));
        verify(internalChatFrame).changeStyle(12, Color.RED, false, false, "John");
    }

    @Test
    public void testActionPerformed_bgColor() {
        JColorChooser colorChooser = mock(JColorChooser.class);
        when(colorChooser.showDialog(internalChatFrame, "Choose Color for user John", Color.black)).thenReturn(Color.BLUE);
        internalChatFrame.actionPerformed(new ActionEvent(colorChooser, ActionEvent.ACTION_FIRST, "bgColor"));
        assertEquals(Color.BLUE, internalChatFrame.showTxt.getBackground());
    }

    @Test
    public void testActionPerformed_whisper() {
        JTextField txtInput = mock(JTextField.class);
        when(txtInput.getText()).thenReturn("/w John ");
        internalChatFrame.txtInput = txtInput;
        internalChatFrame.list.setSelectedValue("John", true);
        internalChatFrame.actionPerformed(new ActionEvent(txtInput, ActionEvent.ACTION_FIRST, "whisper"));
        verify(internalChatFrame.txtInput).setText("/w John ");
    }

}
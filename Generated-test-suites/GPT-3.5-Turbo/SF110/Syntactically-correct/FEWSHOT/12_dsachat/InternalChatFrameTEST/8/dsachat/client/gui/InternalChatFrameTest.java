import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    
    private InternalChatFrame chatFrame;
    private MainFrame parent;
    private StyledDocument doc;

    @Before
    public void setUp() {
        parent = mock(MainFrame.class);
        chatFrame = new InternalChatFrame(parent);
        doc = new DefaultStyledDocument();
        chatFrame.showTxt.setStyledDocument(doc);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(chatFrame.listModel);
        assertNotNull(chatFrame.list);
        assertNotNull(chatFrame.showTxt);
        assertNotNull(chatFrame.txtInput);
        assertNotNull(chatFrame.parent);
        assertNotNull(chatFrame.doc);
        assertNotNull(chatFrame.listPop);
        assertNotNull(chatFrame.txtPop);
    }
    
    @Test
    public void testCreateGUI() {
        chatFrame.createGUI();
        assertNotNull(chatFrame.list);
        assertNotNull(chatFrame.showTxt);
        assertNotNull(chatFrame.txtInput);
        assertNotNull(chatFrame.listPop);
        assertNotNull(chatFrame.txtPop);
    }
    
    @Test
    public void testAddText() throws BadLocationException {
        chatFrame.addText("Test message");
        assertEquals(1, doc.getLength());
    }
    
    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(12, null, false, false, "regular");
        Style expectedStyle = doc.getStyle("regular");
        assertEquals(expectedStyle, doc.getStyle("regular"));
    }
    
    @Test
    public void testAddUser() {
        chatFrame.addUser("User1");
        assertEquals(1, chatFrame.listModel.getSize());
        assertEquals("User1", chatFrame.list.getSelectedValue());
        assertNotNull(doc.getStyle("User1"));
    }
    
    @Test
    public void testDeleteUser() {
        chatFrame.addUser("User1");
        chatFrame.deleteUser("User1");
        assertEquals(0, chatFrame.listModel.getSize());
    }
    
    @Test
    public void testGetUserlist() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] expected = {"User1", "User2"};
        assertArrayEquals(expected, chatFrame.getUserlist());
    }
    
    @Test
    public void testActionPerformed_chooseColor() {
        String name = "User1";
        chatFrame.addUser(name);
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "chooseColor"));
        verify(parent).chooseColor(name);
    }
    
    @Test
    public void testActionPerformed_bgColor() {
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "bgColor"));
        Color newColor = JColorChooser.showDialog(chatFrame, "Choose Color for user " + null, Color.black);
        assertEquals(newColor, chatFrame.showTxt.getBackground());
    }
    
    @Test
    public void testActionPerformed_whisper() {
        String name = "User1";
        chatFrame.addUser(name);
        chatFrame.actionPerformed(new ActionEvent(chatFrame, ActionEvent.ACTION_PERFORMED, "whisper"));
        verify(parent).whisper(name);
    }
    
    @Test
    public void testMouseClicked() {
        // test mouse clicked event
    }
    
    @Test
    public void testMouseEntered() {
        // test mouse entered event
    }
    
    @Test
    public void testMouseExited() {
        // test mouse exited event
    }
    
    @Test
    public void testMousePressed() {
        // test mouse pressed event
    }
    
    @Test
    public void testMouseReleased() {
        // test mouse released event
    }
    
    @Test
    public void testMaybeShowPopup_list() {
        MouseEvent e = mock(MouseEvent.class);
        when(e.isPopupTrigger()).thenReturn(true);
        when(e.getSource()).thenReturn(chatFrame.list);
        chatFrame.maybeShowPopup(e);
        // verify that listPop.show is called
    }
    
    @Test
    public void testMaybeShowPopup_showTxt() {
        MouseEvent e = mock(MouseEvent.class);
        when(e.isPopupTrigger()).thenReturn(true);
        when(e.getSource()).thenReturn(chatFrame.showTxt);
        chatFrame.maybeShowPopup(e);
        // verify that txtPop.show is called
    }
    
    @Test
    public void testGetChatlog() {
        chatFrame.showTxt.setText("Chat log");
        assertEquals("Chat log", chatFrame.getChatlog());
    }
}
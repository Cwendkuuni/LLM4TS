package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    
    private MainFrame mockParent;
    private InternalChatFrame internalChatFrame;
    private JTextPane mockShowTxt;
    private JTextField mockTxtInput;
    private StyledDocument mockDoc;
        
    @Before
    public void setUp() {
        mockParent = mock(MainFrame.class);
        internalChatFrame = new InternalChatFrame(mockParent);
        mockShowTxt = mock(JTextPane.class);
        internalChatFrame.showTxt = mockShowTxt;
        mockTxtInput = mock(JTextField.class);
        internalChatFrame.txtInput = mockTxtInput;
        mockDoc = mock(StyledDocument.class);
        internalChatFrame.doc = mockDoc;
    }
    
    @Test
    public void testAddText() {
        String line = "Test message";
        when(mockShowTxt.getStyledDocument()).thenReturn(mockDoc);
        internalChatFrame.addText(line);
        verify(mockDoc).insertString(mockDoc.getLength(), line + "\n", mockDoc.getStyle("system"));
    }
    
    @Test
    public void testAddTextWithWhispers() {
        String line = "Test message";
        when(mockShowTxt.getStyledDocument()).thenReturn(mockDoc);
        internalChatFrame.addText(line + " whispers something");
        verify(mockDoc).insertString(mockDoc.getLength(), line + "\n", mockDoc.getStyle("system"));
    }
    
    @Test
    public void testAddUser() {
        String user = "TestUser";
        internalChatFrame.addUser(user);
        verify(mockDoc).addStyle(user, mockDoc.getStyle("regular"));
    }
    
    @Test
    public void testDeleteUser() {
        String user = "TestUser";
        internalChatFrame.deleteUser(user);
        verifyNoInteractions(mockDoc);
    }
    
    @Test
    public void testGetUserlist() {
        String[] expectedList = {"User1", "User2", "User3"};
        internalChatFrame.listModel.addElement("User1");
        internalChatFrame.listModel.addElement("User2");
        internalChatFrame.listModel.addElement("User3");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(expectedList, userList);
    }

    @Test
    public void testActionPerformedChooseColor() {
        String name = "TestUser";
        String command = "chooseColor";
        internalChatFrame.list.setSelectedValue(name, true);
        internalChatFrame.actionPerformed(new ActionEvent(new Object(), 0, command));
        verify(mockDoc).getStyle(name);
    }
    
    @Test
    public void testActionPerformedBgColor() {
        String name = "TestUser";
        String command = "bgColor";
        internalChatFrame.list.setSelectedValue(name, true);
        internalChatFrame.actionPerformed(new ActionEvent(new Object(), 0, command));
        verify(mockShowTxt).setBackground(any(Color.class));
    }
    
    @Test
    public void testActionPerformedWhisper() {
        String name = "TestUser";
        String command = "whisper";
        internalChatFrame.list.setSelectedValue(name, true);
        internalChatFrame.actionPerformed(new ActionEvent(new Object(), 0, command));
        verify(mockTxtInput).setText("/w " + name + " ");
    }
    
    @Test
    public void testMousePressedList() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.isPopupTrigger()).thenReturn(true);
        when(event.getSource()).thenReturn(internalChatFrame.list);
        internalChatFrame.mousePressed(event);
        verify(internalChatFrame.listPop).show(internalChatFrame.list, event.getX(), event.getY());
    }
    
    @Test
    public void testMousePressedShowTxt() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.isPopupTrigger()).thenReturn(true);
        when(event.getSource()).thenReturn(internalChatFrame.showTxt);
        internalChatFrame.mousePressed(event);
        verify(internalChatFrame.txtPop).show(internalChatFrame.list, event.getX(), event.getY());
    }
    
    @Test
    public void testMouseReleasedList() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.isPopupTrigger()).thenReturn(true);
        when(event.getSource()).thenReturn(internalChatFrame.list);
        internalChatFrame.mouseReleased(event);
        verify(internalChatFrame.listPop).show(internalChatFrame.list, event.getX(), event.getY());
    }
    
    @Test
    public void testMouseReleasedShowTxt() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.isPopupTrigger()).thenReturn(true);
        when(event.getSource()).thenReturn(internalChatFrame.showTxt);
        internalChatFrame.mouseReleased(event);
        verify(internalChatFrame.txtPop).show(internalChatFrame.list, event.getX(), event.getY());
    }
    
    @Test
    public void testMaybeShowPopupList() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.isPopupTrigger()).thenReturn(true);
        when(event.getSource()).thenReturn(internalChatFrame.list);
        internalChatFrame.maybeShowPopup(event);
        verify(internalChatFrame.listPop).show(internalChatFrame.list, event.getX(), event.getY());
        verifyNoInteractions(internalChatFrame.txtPop);
    }
    
    @Test
    public void testMaybeShowPopupShowTxt() {
        MouseEvent event = mock(MouseEvent.class);
        when(event.isPopupTrigger()).thenReturn(true);
        when(event.getSource()).thenReturn(internalChatFrame.showTxt);
        internalChatFrame.maybeShowPopup(event);
        verifyNoInteractions(internalChatFrame.listPop);
        verify(internalChatFrame.txtPop).show(internalChatFrame.list, event.getX(), event.getY());
    }
    
    @Test
    public void testGetChatlog() {
        when(mockShowTxt.getText()).thenReturn("Chatlog");
        assertEquals("Chatlog", internalChatFrame.getChatlog());
    }
}
import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    // Test case for the constructor
    @Test
    public void testConstructor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Verify that the JList is initialized and has the correct selection mode
        assertNotNull(chatFrame.list);
        assertEquals(0, chatFrame.list.getSelectionMode());
        
        // Verify that the JTextPane is initialized and is not editable
        assertNotNull(chatFrame.showTxt);
        assertFalse(chatFrame.showTxt.isEditable());
        
        // Verify that the JTextField is initialized and is linked to the parent ActionListener
        assertNotNull(chatFrame.txtInput);
        assertSame(parent, chatFrame.txtInput.getActionListeners()[0]);
        assertEquals("sendInput", chatFrame.txtInput.getActionCommand());
    }
    
    // Test case for the addText method
    @Test
    public void testAddText() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Call the addText method with a test line
        chatFrame.addText("This is a test line.");
        
        // Verify that the text is added to the JTextPane
        assertEquals("This is a test line.\n", chatFrame.showTxt.getText());
    }
    
    // Test case for the addStylesToDocument method
    @Test
    public void testAddStylesToDocument() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Call the addStylesToDocument method
        chatFrame.addStylesToDocument(chatFrame.doc);
        
        // Verify that the styles are added to the StyledDocument
        assertNotNull(chatFrame.doc.getStyle("regular"));
        assertNotNull(chatFrame.doc.getStyle("italic"));
        assertNotNull(chatFrame.doc.getStyle("bold"));
        assertNotNull(chatFrame.doc.getStyle("bold-italic"));
        assertNotNull(chatFrame.doc.getStyle("name"));
        assertNotNull(chatFrame.doc.getStyle("system"));
        assertNotNull(chatFrame.doc.getStyle("small"));
        assertNotNull(chatFrame.doc.getStyle("large"));
    }
    
    // Test case for the addUser method
    @Test
    public void testAddUser() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Call the addUser method with a test user
        chatFrame.addUser("TestUser");
        
        // Verify that the user is added to the JList and StyledDocument
        assertEquals(1, chatFrame.listModel.size());
        assertEquals("TestUser", chatFrame.listModel.elementAt(0));
        assertNotNull(chatFrame.doc.getStyle("TestUser"));
    }
    
    // Test case for the deleteUser method
    @Test
    public void testDeleteUser() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add a test user to the JList
        chatFrame.listModel.addElement("TestUser");
        
        // Call the deleteUser method with the test user
        chatFrame.deleteUser("TestUser");
        
        // Verify that the user is removed from the JList
        assertEquals(0, chatFrame.listModel.size());
    }
    
    // Test case for the getUserlist method
    @Test
    public void testGetUserlist() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add test users to the JList
        chatFrame.listModel.addElement("User1");
        chatFrame.listModel.addElement("User2");
        chatFrame.listModel.addElement("User3");
        
        // Call the getUserlist method
        String[] userlist = chatFrame.getUserlist();
        
        // Verify that the correct userlist is returned
        assertEquals(3, userlist.length);
        assertEquals("User1", userlist[0]);
        assertEquals("User2", userlist[1]);
        assertEquals("User3", userlist[2]);
    }
    
    // Test case for the actionPerformed method
    @Test
    public void testActionPerformed() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add a test user to the JList
        chatFrame.listModel.addElement("TestUser");
        
        // Create a mock ActionEvent
        ActionEvent e = new ActionEvent(chatFrame.list, ActionEvent.ACTION_PERFORMED, "chooseColor");
        
        // Call the actionPerformed method with the mock ActionEvent
        chatFrame.actionPerformed(e);
        
        // Verify that the correct action is performed (changeStyle method is called)
        // and the color of the style is changed
        // (not possible to test JColorChooser.showDialog result directly)
        // (can test the change in the style's color after the method call)
        Style style = chatFrame.doc.getStyle("TestUser");
        assertEquals(Color.BLUE, StyleConstants.getForeground(style));
        
        // Create another mock ActionEvent for a different command
        e = new ActionEvent(chatFrame.showTxt, ActionEvent.ACTION_PERFORMED, "bgColor");
        
        // Call the actionPerformed method with the new mock ActionEvent
        chatFrame.actionPerformed(e);
        
        // Verify that the correct action is performed (changeStyle method is called)
        // and the background color of the JTextPane is changed
        // (not possible to test JColorChooser.showDialog result directly)
        // (can test the change in the JTextPane's background color after the method call)
        assertEquals(Color.BLACK, chatFrame.showTxt.getBackground());
    }
    
    // Test case for the mousePressed and mouseReleased methods
    @Test
    public void testMouseMethods() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Create a mock MouseEvent
        MouseEvent e = new MouseEvent(chatFrame.list, MouseEvent.MOUSE_PRESSED, 0, MouseEvent.BUTTON1, 10, 10, 1, false);
        
        // Call the mousePressed method with the mock MouseEvent
        chatFrame.mousePressed(e);
        
        // Verify that the correct action is performed (maybeShowPopup method is called)
        // and the JPopupMenu is shown
        assertTrue(chatFrame.listPop.isShowing());
        
        // Call the mouseReleased method with the same mock MouseEvent
        chatFrame.mouseReleased(e);
        
        // Verify that the JPopupMenu is hidden after the mouse is released
        assertFalse(chatFrame.listPop.isShowing());
    }
    
    // Test case for the getChatlog method
    @Test
    public void testGetChatlog() {
        // Create an instance of InternalChatFrame
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        
        // Add test lines of text to the JTextPane
        chatFrame.showTxt.setText("Line 1\nLine 2\nLine 3\n");
        
        // Call the getChatlog method
        String chatlog = chatFrame.getChatlog();
        
        // Verify that the correct chatlog is returned
        assertEquals("Line 1\nLine 2\nLine 3\n", chatlog);
    }
}
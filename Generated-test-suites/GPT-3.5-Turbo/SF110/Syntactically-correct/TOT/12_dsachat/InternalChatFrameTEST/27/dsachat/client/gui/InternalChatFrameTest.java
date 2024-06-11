import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class InternalChatFrameTest {
    
    private MainFrame parent;
    private InternalChatFrame internalChatFrame;
    
    @Before
    public void setup() {
        parent = mock(MainFrame.class);
        internalChatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testAddText() {
        // Typical use case
        internalChatFrame.addText("This is a regular message.");
        
        // Edge case: Empty message
        internalChatFrame.addText("");
        
        // Edge case: Message with special characters
        internalChatFrame.addText("*Bold* _Italic_ +Bold and Italic+");
        
        // Error scenario: BadLocationException
        internalChatFrame.addText(null);
    }
    
    @Test
    public void testAddUser() {
        // Typical use case
        internalChatFrame.addUser("User1");
        
        // Edge case: Empty username
        internalChatFrame.addUser("");
        
        // Error scenario: Null username
        internalChatFrame.addUser(null);
    }
    
    @Test
    public void testDeleteUser() {
        // Typical use case
        internalChatFrame.addUser("User1");
        internalChatFrame.deleteUser("User1");
        
        // Edge case: User not in list
        internalChatFrame.deleteUser("User2");
    }
    
    @Test
    public void testGetUserlist() {
        // Typical use case: Single user
        internalChatFrame.addUser("User1");
        String[] userList = internalChatFrame.getUserlist();
        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
        
        // Typical use case: Multiple users
        internalChatFrame.addUser("User2");
        userList = internalChatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertEquals("User2", userList[0]);
        assertEquals("User1", userList[1]);
        
        // Typical use case: Empty userlist
        internalChatFrame.deleteUser("User2");
        internalChatFrame.deleteUser("User1");
        userList = internalChatFrame.getUserlist();
        assertEquals(0, userList.length);
        
        // Error scenario: Null userlist
        when(internalChatFrame.listModel.size()).thenReturn(null);
        userList = internalChatFrame.getUserlist();
        assertNull(userList);
    }
    
    
    @Test
    public void testActionPerformed() {
        // Typical use case: chooseColor
        internalChatFrame.addUser("User1");
        internalChatFrame.list.setSelectedValue("User1");
        internalChatFrame.actionPerformed(actionEvent("chooseColor"));
        
        // Typical use case: bgColor
        internalChatFrame.actionPerformed(actionEvent("bgColor"));
        
        // Typical use case: whisper
        internalChatFrame.actionPerformed(actionEvent("whisper"));
        
        // Error scenario: Invalid action command
        internalChatFrame.actionPerformed(actionEvent("invalidAction"));
    }
    
    private ActionEvent actionEvent(String actionCommand) {
        return new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand);
    }
}
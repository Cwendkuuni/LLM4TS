import org.junit.*;
import static org.junit.Assert.*;

public class InternalChatFrameTest {
    
    @Test
    public void testAddText() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        
        // Test case for line without ":" should be inserted with system style
        chatFrame.addText("Test line without colon");
        String expected1 = "Test line without colon\n";
        assertEquals(expected1, chatFrame.getChatlog());
        
        // Test case for line with style name and colon at beginning
        chatFrame.addText("name: Test line with style name");
        String expected2 = "name: Test line with style name\n";
        assertEquals(expected2, chatFrame.getChatlog());
        
        // Test case for line with special style name and ":" at beginning
        chatFrame.addText("*boldStyle*: Test line with special style name");
        String expected3 = "*boldStyle*: Test line with special style name\n";
        assertEquals(expected3, chatFrame.getChatlog());
        
        // Test case for line with style name that starts with "You "
        chatFrame.addText("You style: Test line with style name starting with 'You'");
        String expected4 = "You style: Test line with style name starting with 'You'\n";
        assertEquals(expected4, chatFrame.getChatlog());
        
        // Test case for line with style name that starts with "*"
        chatFrame.addText("*style: Test line with style name starting with *");
        String expected5 = "*style: Test line with style name starting with *\n";
        assertEquals(expected5, chatFrame.getChatlog());
        
        // Test case for line with multiple styles
        chatFrame.addText("line with *multiple* +_styles");
        String expected6 = "line with \nmultiple\n +\nstyles\n\n";
        assertEquals(expected6, chatFrame.getChatlog());
    }
    
    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        
        // Test case for adding a new user to the user list
        chatFrame.addUser("user1");
        String[] expected1 = {"user1"};
        assertArrayEquals(expected1, chatFrame.getUserlist());
        
        // Test case for adding another user to the user list
        chatFrame.addUser("user2");
        String[] expected2 = {"user2", "user1"};
        assertArrayEquals(expected2, chatFrame.getUserlist());
    }
    
    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addUser("user1");
        chatFrame.addUser("user2");
        
        // Test case for deleting a user from the user list
        chatFrame.deleteUser("user1");
        String[] expected1 = {"user2"};
        assertArrayEquals(expected1, chatFrame.getUserlist());
    }
    
    @Test
    public void testActionPerformed() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        
        // Test case for "chooseColor" action command
        chatFrame.addUser("user1");
        chatFrame.list.setSelectedIndex(0);
        chatFrame.actionPerformed(new ActionEvent(this, 0, "chooseColor"));
        // Assert that the color of the selected user's style has changed
        
        // Test case for "bgColor" action command
        chatFrame.actionPerformed(new ActionEvent(this, 0, "bgColor"));
        // Assert that the background color of the text pane has changed
        
        // Test case for "whisper" action command
        chatFrame.actionPerformed(new ActionEvent(this, 0, "whisper"));
        // Assert that the text input field contains the "/w" command and the selected user's name
    }
    
    @Test
    public void testMaybeShowPopup() {
        // Test case for showing the listPopup when right-clicking on the list
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.list.addMouseListener(chatFrame);
        MouseEvent mouseEvent1 = new MouseEvent(chatFrame.list, MouseEvent.MOUSE_RELEASED, 
                                                System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.maybeShowPopup(mouseEvent1);
        // Assert that the listPopup is shown
        
        // Test case for showing the txtPopup when right-clicking on the text pane
        chatFrame.showTxt.addMouseListener(chatFrame);
        MouseEvent mouseEvent2 = new MouseEvent(chatFrame.showTxt, MouseEvent.MOUSE_RELEASED, 
                                                System.currentTimeMillis(), 0, 10, 10, 1, false);
        chatFrame.maybeShowPopup(mouseEvent2);
        // Assert that the txtPopup is shown
    }
    
    @Test
    public void testGetChatlog() {
        // Test case for getting the chat log from the text pane
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText("Test line 1");
        chatFrame.addText("Test line 2");
        String expected = "Test line 1\nTest line 2\n";
        assertEquals(expected, chatFrame.getChatlog());
    }
}
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class InternalChatFrameTest {

    @Mock
    private MainFrame mainFrame;

    private InternalChatFrame internalChatFrame;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        internalChatFrame = new InternalChatFrame(mainFrame);
    }

    // Test for the addText() method
    @Test
    public void testAddText() {
        String line = "Test Line";
        internalChatFrame.addText(line);
        // Assert that the line is added to the doc
        // ... (assertion code)
    }
    
    // Test for the addStylesToDocument() method
    @Test
    public void testAddStylesToDocument() {
        StyledDocument doc = mock(StyledDocument.class);
        internalChatFrame.addStylesToDocument(doc);
        // Assert that the necessary styles are added to the document
        // ... (assertion code)
    }

    // Test for the changeStyle() method
    @Test
    public void testChangeStyle() {
        int fontSize = 12;
        Color c = Color.RED;
        boolean isBold = true;
        boolean isItalic = false;
        String name = "testName";
        internalChatFrame.changeStyle(fontSize, c, isBold, isItalic, name);
        // Assert that the style is changed correctly
        // ... (assertion code)
    }

    // Test for the addUser() method
    @Test
    public void testAddUser() {
        String user = "testUser";
        internalChatFrame.addUser(user);
        // Assert that the user is added to the list model and document
        // ... (assertion code)
    }

    // Test for the deleteUser() method
    @Test
    public void testDeleteUser() {
        String user = "testUser";
        internalChatFrame.deleteUser(user);
        // Assert that the user is removed from the list model
        // ... (assertion code)
    }

    // Test for the getUserlist() method
    @Test
    public void testGetUserlist() {
        // Add test users to the list model
        internalChatFrame.addUser("user1");
        internalChatFrame.addUser("user2");
        internalChatFrame.addUser("user3");

        // Get the user list
        String[] userList = internalChatFrame.getUserlist();

        // Assert that the user list matches the expected values
        // ... (assertion code)
    }

    // Test for the actionPerformed() method
    @Test
    public void testActionPerformed() {
        ActionEvent chooseColorEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "chooseColor");
        internalChatFrame.actionPerformed(chooseColorEvent);
        // Assert that the choose color action is performed correctly
        // ... (assertion code)

        ActionEvent bgColorEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bgColor");
        internalChatFrame.actionPerformed(bgColorEvent);
        // Assert that the background color action is performed correctly
        // ... (assertion code)

        ActionEvent whisperEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "whisper");
        internalChatFrame.actionPerformed(whisperEvent);
        // Assert that the whisper action is performed correctly
        // ... (assertion code)
    }

    // Test for the mousePressed() method
    @Test
    public void testMousePressed() {
        MouseEvent mockMouseEvent = mock(MouseEvent.class);
        when(mockMouseEvent.isPopupTrigger()).thenReturn(true);
        when(mockMouseEvent.getSource()).thenReturn(internalChatFrame.getList());
        internalChatFrame.mousePressed(mockMouseEvent);

        // Assert that the correct popup menu is shown
        // ... (assertion code)
    }

    // Test for the mouseReleased() method
    @Test
    public void testMouseReleased() {
        MouseEvent mockMouseEvent = mock(MouseEvent.class);
        when(mockMouseEvent.isPopupTrigger()).thenReturn(true);
        when(mockMouseEvent.getSource()).thenReturn(internalChatFrame.getList());
        internalChatFrame.mouseReleased(mockMouseEvent);

        // Assert that the correct popup menu is shown
        // ... (assertion code)
    }

    // Test for the getChatlog() method
    @Test
    public void testGetChatlog() {
        // Add test text to the showTxt component
        internalChatFrame.addText("Test Line 1");
        internalChatFrame.addText("Test Line 2");
        internalChatFrame.addText("Test Line 3");

        String chatlog = internalChatFrame.getChatlog();

        // Assert that the chatlog contains the expected lines
        // ... (assertion code)
    }

}
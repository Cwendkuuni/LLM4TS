import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InternalChatFrameTest {

    @Mock
    MainFrame mockParent;

    @Test
    public void testAddText_RegularText_AddedToDocument() {
        InternalChatFrame internalChatFrame = new InternalChatFrame(mockParent);

        internalChatFrame.addText("This is a regular message.");

        // Verify that the message is added to the document
        assertEquals("This is a regular message.\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddUser_NewUser_UserAddedToListModel() {
        InternalChatFrame internalChatFrame = new InternalChatFrame(mockParent);

        internalChatFrame.addUser("JohnDoe");

        // Verify that the user is added to the list model
        assertTrue(internalChatFrame.getUserlist().length == 1);
        assertTrue(internalChatFrame.getUserlist()[0].equals("JohnDoe"));
    }

    @Test
    public void testDeleteUser_ExistingUser_UserRemovedFromListModel() {
        InternalChatFrame internalChatFrame = new InternalChatFrame(mockParent);
        internalChatFrame.addUser("JohnDoe");

        internalChatFrame.deleteUser("JohnDoe");

        // Verify that the user is removed from the list model
        assertTrue(internalChatFrame.getUserlist().length == 0);
    }

    @Test
    public void testGetUserlist_MultipleUsers_ReturnsCorrectUserList() {
        InternalChatFrame internalChatFrame = new InternalChatFrame(mockParent);
        internalChatFrame.addUser("JohnDoe");
        internalChatFrame.addUser("JaneDoe");

        String[] expectedUserList = {"JohnDoe", "JaneDoe"};

        // Verify that the correct user list is returned
        assertArrayEquals(expectedUserList, internalChatFrame.getUserlist());
    }

    // Add more test cases for remaining methods

}
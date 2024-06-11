import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.awt.Color;

public class InternalChatFrameTest {

    private InternalChatFrame chatFrame;

    @Before
    public void setUp() {
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testAddText() {
        chatFrame.addText("Test message");
        assertNotNull(chatFrame.getChatlog());
    }

    @Test
    public void testAddStylesToDocument() {
        chatFrame.addStylesToDocument(chatFrame.doc);
        assertNotNull(chatFrame.doc.getStyle("regular"));
        assertNotNull(chatFrame.doc.getStyle("italic"));
        assertNotNull(chatFrame.doc.getStyle("bold"));
        assertNotNull(chatFrame.doc.getStyle("bold-italic"));
        assertNotNull(chatFrame.doc.getStyle("name"));
        assertNotNull(chatFrame.doc.getStyle("system"));
        assertNotNull(chatFrame.doc.getStyle("small"));
        assertNotNull(chatFrame.doc.getStyle("large"));
    }

    @Test
    public void testChangeStyle() {
        chatFrame.changeStyle(12, Color.RED, true, true, "testStyle");
        assertNotNull(chatFrame.doc.getStyle("testStyle"));
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        assertEquals("TestUser", chatFrame.listModel.elementAt(0));
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("TestUserToDelete");
        chatFrame.deleteUser("TestUserToDelete");
        assertEquals(0, chatFrame.listModel.size());
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("User1");
        chatFrame.addUser("User2");
        String[] userList = chatFrame.getUserlist();
        assertEquals(2, userList.length);
        assertArrayEquals(new String[]{"User1", "User2"}, userList);
    }

    // Add more test cases for other methods as required

}
package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {

    private MainFrame parent;
    private InternalChatFrame chatFrame;

    @Before
    public void setup() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }

    @Test
    public void testConstructor() {
        assertNotNull(chatFrame.listModel);
        assertEquals(0, chatFrame.listModel.size());
        assertNotNull(chatFrame.list);
        assertNotNull(chatFrame.showTxt);
        assertNotNull(chatFrame.txtInput);
        assertNotNull(chatFrame.doc);
        assertNull(chatFrame.listPop);
        assertNull(chatFrame.txtPop);
    }

    @Test
    public void testAddText() {
        chatFrame.addText("test");
        assertEquals("test\n", chatFrame.showTxt.getText());
    }

    @Test
    public void testAddTextWithStyle() {
        parent.hero.setName("TestHero");
        chatFrame.addText("You test message");
        assertEquals("TestHero test message\n", chatFrame.showTxt.getText());
    }

    @Test
    public void testAddTextWithItalicStyle() {
        chatFrame.addText("Test *Italic* message");
        assertEquals("Test ", chatFrame.showTxt.getText());
        assertEquals(true, chatFrame.doc.getCharacterElement(5).isItalic());
        assertEquals(false, chatFrame.doc.getCharacterElement(6).isItalic());
        assertEquals("Italic", chatFrame.doc.getText(6, 6));
        assertEquals(false, chatFrame.doc.getCharacterElement(12).isItalic());
        assertEquals("\n", chatFrame.doc.getText(12, 1));
    }

    @Test
    public void testAddTextWithBoldStyle() {
        chatFrame.addText("Test +Bold+ message");
        assertEquals("Test ", chatFrame.showTxt.getText());
        assertEquals(true, chatFrame.doc.getCharacterElement(5).isBold());
        assertEquals(false, chatFrame.doc.getCharacterElement(6).isBold());
        assertEquals("Bold", chatFrame.doc.getText(6, 4));
        assertEquals(false, chatFrame.doc.getCharacterElement(11).isBold());
        assertEquals("\n", chatFrame.doc.getText(11, 1));
    }

    @Test
    public void testAddTextWithItalicAndBoldStyle() {
        chatFrame.addText("Test *Italic+Bold+* message");
        assertEquals("Test ", chatFrame.showTxt.getText());
        assertEquals(true, chatFrame.doc.getCharacterElement(5).isItalic());
        assertEquals(true, chatFrame.doc.getCharacterElement(6).isBold());
        assertEquals("Italic", chatFrame.doc.getText(6, 6));
        assertEquals(false, chatFrame.doc.getCharacterElement(13).isItalic());
        assertEquals(false, chatFrame.doc.getCharacterElement(13).isBold());
        assertEquals("\n", chatFrame.doc.getText(13, 1));
    }

    @Test
    public void testAddTextWithNoStyle() {
        chatFrame.addText("Test message");
        assertEquals("Test message\n", chatFrame.showTxt.getText());
    }

    @Test
    public void testAddUser() {
        chatFrame.addUser("TestUser");
        assertEquals(1, chatFrame.listModel.size());
        assertEquals("TestUser", chatFrame.listModel.getElementAt(0));
        assertEquals(1, chatFrame.list.getSelectedIndices().length);
        assertEquals("TestUser", chatFrame.list.getSelectedValue());
    }

    @Test
    public void testDeleteUser() {
        chatFrame.addUser("TestUser");
        chatFrame.deleteUser("TestUser");
        assertEquals(0, chatFrame.listModel.size());
        assertEquals(0, chatFrame.list.getSelectedIndices().length);
        assertNull(chatFrame.list.getSelectedValue());
    }

    @Test
    public void testGetUserlist() {
        chatFrame.addUser("TestUser1");
        chatFrame.addUser("TestUser2");
        chatFrame.addUser("TestUser3");
        String[] userList = chatFrame.getUserlist();
        assertEquals(3, userList.length);
        assertEquals("TestUser1", userList[0]);
        assertEquals("TestUser2", userList[1]);
        assertEquals("TestUser3", userList[2]);
    }

    @Test
    public void testActionPerformedChooseColor() {
        // test code to mock JColorChooser
        // replace with actual JColorChooser test
        java.awt.Color newColor = java.awt.Color.RED;
        
        int fontSize = 12;
        java.awt.Color c = java.awt.Color.RED;
        boolean isBold = false;
        boolean isItalic = false;
        String name = "TestUser";
        chatFrame.changeStyle(fontSize, newColor, isBold, isItalic, name);
        // assert color change
    }

    @Test
    public void testActionPerformedBgColor() {
        // test code to mock JColorChooser
        // replace with actual JColorChooser test
        java.awt.Color newColor = java.awt.Color.RED;
        
        chatFrame.actionPerformed(new java.awt.event.ActionEvent(chatFrame, 0, "bgColor"));

        // assert background color change
        assertEquals(newColor, chatFrame.showTxt.getBackground());
    }

    @Test
    public void testActionPerformedWhisper() {
        String name = "TestUser";
        chatFrame.list.setSelectedValue(name, true);

        chatFrame.actionPerformed(new java.awt.event.ActionEvent(chatFrame, 0, "whisper"));

        // assert input text is set to /w TestUser
        assertEquals("/w " + name + " ", chatFrame.txtInput.getText());
    }
}
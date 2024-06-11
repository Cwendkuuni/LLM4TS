import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class InternalChatFrameTest {
    
    @Test
    public void testAddText_noColonInLine_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("Hello World");
        String expected = "Hello World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddText_colonInLine_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("User: Hello World");
        String expected = "User: Hello World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddText_whisperStyle_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("User whispers: Hello World");
        String expected = "User: Hello World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddText_youStyle_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("You User: Hello World");
        String expected = "User: Hello World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddText_boldAndItalicStyle_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("Hello *bold_italic* World");
        String expected = "Hello bolditalic World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddText_boldStyle_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("Hello *bold* World");
        String expected = "Hello bold World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddText_italicStyle_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("Hello +italic+ World");
        String expected = "Hello italic World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddText_regularStyle_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addText("Hello World");
        String expected = "Hello World\n";
        String actual = frame.getChatlog();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testAddStylesToDocument_validStyles_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addStylesToDocument(frame.doc);
        Style regularStyle = frame.doc.getStyle("regular");
        assertTrue(StyleConstants.isItalic(regularStyle));
        assertFalse(StyleConstants.isBold(regularStyle));
        Style boldStyle = frame.doc.getStyle("bold");
        assertTrue(StyleConstants.isItalic(boldStyle));
        assertTrue(StyleConstants.isBold(boldStyle));
        Style italicStyle = frame.doc.getStyle("italic");
        assertTrue(StyleConstants.isItalic(italicStyle));
        assertFalse(StyleConstants.isBold(italicStyle));
        Style nameStyle = frame.doc.getStyle("name");
        assertFalse(StyleConstants.isItalic(nameStyle));
        assertTrue(StyleConstants.isBold(nameStyle));
        Style systemStyle = frame.doc.getStyle("system");
        assertTrue(StyleConstants.isItalic(systemStyle));
        assertFalse(StyleConstants.isBold(systemStyle));
        Style smallStyle = frame.doc.getStyle("small");
        assertFalse(StyleConstants.isItalic(smallStyle));
        assertFalse(StyleConstants.isBold(smallStyle));
        Style largeStyle = frame.doc.getStyle("large");
        assertFalse(StyleConstants.isItalic(largeStyle));
        assertFalse(StyleConstants.isBold(largeStyle));
    }
    
    @Test
    public void testChangeStyle_validChanges_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addStylesToDocument(frame.doc);
        frame.changeStyle(12, Color.BLUE, true, true, "name");
        Style style = frame.doc.getStyle("name");
        assertEquals(Color.BLUE, StyleConstants.getForeground(style));
        assertTrue(StyleConstants.isBold(style));
        assertTrue(StyleConstants.isItalic(style));
    }
    
    @Test
    public void testAddUser_validUser_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addUser("User");
        String[] expected = {"User"};
        String[] actual = frame.getUserlist();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeleteUser_existingUser_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addUser("User");
        frame.deleteUser("User");
        String[] expected = {};
        String[] actual = frame.getUserlist();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetUserlist_multipleUsers_success() {
        InternalChatFrame frame = new InternalChatFrame();
        frame.addUser("User1");
        frame.addUser("User2");
        frame.addUser("User3");
        String[] expected = {"User3", "User2", "User1"};
        String[] actual = frame.getUserlist();
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testActionPerformed_chooseColorAction_success() {
        InternalChatFrame frame = Mockito.spy(new InternalChatFrame());
        ActionEvent event = new ActionEvent(new Object(), 1, "chooseColor");
        frame.actionPerformed(event);
        Mockito.verify(frame).changeStyle(12, Mockito.any(), false, false, Mockito.any());
    }
    
    @Test
    public void testActionPerformed_bgColorAction_success() {
        InternalChatFrame frame = Mockito.spy(new InternalChatFrame());
        ActionEvent event = new ActionEvent(new Object(), 1, "bgColor");
        frame.actionPerformed(event);
        Mockito.verify(frame).showTxt.setBackground(Mockito.any());
    }
    
    @Test
    public void testActionPerformed_whisperAction_success() {
        InternalChatFrame frame = Mockito.spy(new InternalChatFrame());
        ActionEvent event = new ActionEvent(new Object(), 1, "whisper");
        frame.actionPerformed(event);
        Mockito.verify(frame).txtInput.setText(Mockito.any());
    }
    
    @Test
    public void testMousePressed_listSource_success() {
        InternalChatFrame frame = new InternalChatFrame();
        MouseEvent event = new MouseEvent(frame.list, 1, 1, 1, 1, 1, 1, false);
        frame.mousePressed(event);
        assertNotNull(frame.listPop);
    }
    
    @Test
    public void testMousePressed_showTxtSource_success() {
        InternalChatFrame frame = new InternalChatFrame();
        MouseEvent event = new MouseEvent(frame.showTxt, 1, 1, 1, 1, 1, 1, false);
        frame.mousePressed(event);
        assertNotNull(frame.txtPop);
    }
}
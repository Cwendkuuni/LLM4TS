
package dsachat.client.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

public class InternalChatFrameTest {
    private InternalChatFrame chatFrame;
    private MainFrame parent;
    
    @Before
    public void setUp() {
        parent = new MainFrame();
        chatFrame = new InternalChatFrame(parent);
    }
    
    @Test
    public void testConstructor() {
        assertEquals("Chat", chatFrame.getTitle());
        assertTrue(chatFrame.isResizable());
        assertFalse(chatFrame.isClosable());
        assertFalse(chatFrame.isMaximizable());
        assertTrue(chatFrame.isIconifiable());
        
        assertNotNull(chatFrame.getList());
        assertNotNull(chatFrame.getListModel());
        assertNotNull(chatFrame.getShowTxt());
        assertNotNull(chatFrame.getTxtInput());
        assertNotNull(chatFrame.getParentFrame());
        assertNotNull(chatFrame.getDoc());
        assertNotNull(chatFrame.getListPop());
        assertNotNull(chatFrame.getTxtPop());
        
        assertEquals(0, chatFrame.getLocation().getX(), 0);
        assertEquals(0, chatFrame.getLocation().getY(), 0);
    }
    
    @Test
    public void testAddText() {
        chatFrame.addText("This is a test");
        assertEquals("This is a test", chatFrame.getDoc().getText(0, chatFrame.getDoc().getLength()));
    }
    
    @Test
    public void testAddStylesToDocument() {
        StyledDocument doc = chatFrame.getDoc();
        
        // Verify default style
        Style defStyle = doc.getStyle("default");
        assertNotNull(defStyle);
        assertEquals("SansSerif", StyleConstants.getFontFamily(defStyle));
        
        // Verify regular style
        Style regStyle = doc.getStyle("regular");
        assertNotNull(regStyle);
        assertEquals(defStyle, StyleConstants.getParent(regStyle));
        
        // Verify italic style
        Style italicStyle = doc.getStyle("italic");
        assertNotNull(italicStyle);
        assertEquals(true,StyleConstants.isItalic(italicStyle));
        
        // Verify bold style
        Style boldStyle = doc.getStyle("bold");
        assertNotNull(boldStyle);
        assertEquals(true,StyleConstants.isBold(boldStyle));
        
        // Verify bold-italic style
        Style boldItalicStyle = doc.getStyle("bold-italic");
        assertNotNull(boldItalicStyle);
        assertEquals(true,StyleConstants.isItalic(boldItalicStyle));
        assertEquals(true,StyleConstants.isBold(boldItalicStyle));
        
        // Verify name style
        Style nameStyle = doc.getStyle("name");
        assertNotNull(nameStyle);
        assertEquals(Color.BLUE, StyleConstants.getForeground(nameStyle));
        assertEquals(16, StyleConstants.getFontSize(nameStyle));
        
        // Verify system style
        Style systemStyle = doc.getStyle("system");
        assertNotNull(systemStyle);
        assertEquals(new Color(0.0f, 0.6f, 0.0f, 1.0f), StyleConstants.getForeground(systemStyle));
        assertEquals(true,StyleConstants.isItalic(systemStyle));
        assertEquals(14, StyleConstants.getFontSize(systemStyle));
        
        // Verify small style
        Style smallStyle = doc.getStyle("small");
        assertNotNull(smallStyle);
        assertEquals(10, StyleConstants.getFontSize(smallStyle));
        
        // Verify large style
        Style largeStyle = doc.getStyle("large");
        assertNotNull(largeStyle);
        assertEquals(16, StyleConstants.getFontSize(largeStyle));
    }
    
    @Test
    public void testChangeStyle() {
        Style docStyle = chatFrame.getDoc().addStyle("docStyle", null);
        
        String name = "docStyle";
        int fontSize = 12;
        Color color = Color.RED;
        boolean isBold = true;
        boolean isItalic = true;
        
        chatFrame.changeStyle(fontSize, color, isBold, isItalic, name);
        
        Style style = chatFrame.getDoc().getStyle(name);
        assertNotNull(style);
        assertEquals(color, StyleConstants.getForeground(style));
        assertEquals(isBold, StyleConstants.isBold(style));
        assertEquals(isItalic, StyleConstants.isItalic(style));
    }
    
    // Add more test cases for the remaining methods
    
}


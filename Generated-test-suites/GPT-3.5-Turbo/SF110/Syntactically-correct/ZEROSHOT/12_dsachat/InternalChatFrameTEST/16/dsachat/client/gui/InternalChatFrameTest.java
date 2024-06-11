
package dsachat.client.gui;

import org.junit.Before;
import org.junit.Test;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;

import static org.mockito.Mockito.*;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;
    private DefaultListModel defaultListModel;
    private MainFrame parent;
    private JTextPane showTxt;
    private JTextField txtInput;
    private StyledDocument doc;
    private JList list;
    private JPopupMenu listPop;
    private JPopupMenu txtPop;

    @Before
    public void setUp() {
        parent = mock(MainFrame.class);
        internalChatFrame = new InternalChatFrame(parent);
        defaultListModel = mock(DefaultListModel.class);
        showTxt = mock(JTextPane.class);
        txtInput = mock(JTextField.class);
        doc = mock(StyledDocument.class);
        list = mock(JList.class);
        listPop = mock(JPopupMenu.class);
        txtPop = mock(JPopupMenu.class);

        internalChatFrame.list = list;
        internalChatFrame.listModel = defaultListModel;
        internalChatFrame.showTxt = showTxt;
        internalChatFrame.txtInput = txtInput;
        internalChatFrame.doc = doc;
        internalChatFrame.listPop = listPop;
        internalChatFrame.txtPop = txtPop;
    }

    @Test
    public void testAddText() {
        String line = "test line";
        internalChatFrame.addText(line);

        // verify if the line is inserted into the styled document
        verify(doc).insertString(doc.getLength(), line, doc.getStyle("regular"));
        verify(doc).insertString(doc.getLength(), "\n", doc.getStyle("regular"));
    }

    @Test
    public void testAddUser() {
        String user = "test user";
        internalChatFrame.addUser(user);

        // verify if the user is added to the list model
        verify(defaultListModel).add(0, user);
        verify(list).ensureIndexIsVisible(0);
        verify(list).setSelectedIndex(0);
        verify(list).validate();
        verify(doc).addStyle(user, doc.getStyle("regular"));
    }

    @Test
    public void testDeleteUser() {
        String user = "test user";
        internalChatFrame.deleteUser(user);

        // verify if the user is removed from the list model
        verify(defaultListModel).removeElement(user);
    }

    @Test
    public void testGetUserlist() {
        internalChatFrame.getUserlist();

        // verify if the elements of the list model are returned as an array
        verify(defaultListModel).size();
        verify(defaultListModel, times(2)).elementAt(anyInt());
    }

    @Test
    public void testActionPerformed_chooseColor() {
        String name = "test name";
        when(list.getSelectedValue()).thenReturn(name);

        internalChatFrame.actionPerformed(new ActionEvent(new Object(), 0, "chooseColor"));

        // verify if the style is changed with the chosen color
        verify(list).getSelectedValue();
        verify(JColorChooser.class).showDialog(internalChatFrame, "Choose Color for user " + name, Color.BLACK);
        verify(doc).getStyle(name);
        verify(StyleConstants.class).setForeground(any(), any());
        verify(StyleConstants.class, times(2)).setBold(any(), any());
        verify(StyleConstants.class).setItalic(any(), any());
    }

    @Test
    public void testActionPerformed_bgColor() {
        String name = "test name";
        when(list.getSelectedValue()).thenReturn(name);

        internalChatFrame.actionPerformed(new ActionEvent(new Object(), 0, "bgColor"));

        // verify if the background color of the text pane is changed
        verify(list).getSelectedValue();
        verify(JColorChooser.class).showDialog(internalChatFrame, "Choose Color for user " + name, Color.BLACK);
        verify(showTxt).setBackground(any());
    }

    @Test
    public void testActionPerformed_whisper() {
        String name = "test name";
        String wUser = "/w " + name + " ";
        when(list.getSelectedValue()).thenReturn(name);

        internalChatFrame.actionPerformed(new ActionEvent(new Object(), 0, "whisper"));

        // verify if the text field's text is set to whisper command with the selected name
        verify(list).getSelectedValue();
        verify(txtInput).setText(wUser);
    }

    @Test
    public void testMouseClicked() {
        MouseEvent e = mock(MouseEvent.class);
        internalChatFrame.mouseClicked(e);

        // verify if the method does not perform any actions
        verifyZeroInteractions(e);
    }

    @Test
    public void testMouseEntered() {
        MouseEvent e = mock(MouseEvent.class);
        internalChatFrame.mouseEntered(e);

        // verify if the method does not perform any actions
        verifyZeroInteractions(e);
    }

    @Test
    public void testMouseExited() {
        MouseEvent e = mock(MouseEvent.class);
        internalChatFrame.mouseExited(e);

        // verify if the method does not perform any actions
        verifyZeroInteractions(e);
    }

    @Test
    public void testMousePressed_list() {
        MouseEvent e = mock(MouseEvent.class);
        when(e.isPopupTrigger()).thenReturn(true);
        when(e.getSource()).thenReturn(list);

        internalChatFrame.mousePressed(e);

        // verify if the list popup is shown on mouse press
        verify(e, times(2)).isPopupTrigger();
        verify(e).getSource();
        verify(listPop).show(list, e.getX(), e.getY());
    }

    @Test
    public void testMousePressed_showTxt() {
        MouseEvent e = mock(MouseEvent.class);
        when(e.isPopupTrigger()).thenReturn(true);
        when(e.getSource()).thenReturn(showTxt);

        internalChatFrame.mousePressed(e);

        // verify if the text popup is shown on mouse press
        verify(e, times(2)).isPopupTrigger();
        verify(e).getSource();
        verify(txtPop).show(list, e.getX(), e.getY());
    }

    @Test
    public void testMouseReleased() {
        MouseEvent e = mock(MouseEvent.class);
        internalChatFrame.mouseReleased(e);

        // verify if maybeShowPopup method is called
        verify(internalChatFrame).maybeShowPopup(e);
    }

    @Test
    public void testMaybeShowPopup_list() {
        MouseEvent e = mock(MouseEvent.class);
        when(e.isPopupTrigger()).thenReturn(true);
        when(e.getSource()).thenReturn(list);

        internalChatFrame.maybeShowPopup(e);

        // verify if the list popup is shown
        verify(e).isPopupTrigger();
        verify(e).getSource();
        verify(listPop).show(list, e.getX(), e.getY());
    }

    @Test
    public void testMaybeShowPopup_showTxt() {
        MouseEvent e = mock(MouseEvent.class);
        when(e.isPopupTrigger()).thenReturn(true);
        when(e.getSource()).thenReturn(showTxt);

        internalChatFrame.maybeShowPopup(e);

        // verify if the text popup is shown
        verify(e).isPopupTrigger();
        verify(e).getSource();
        verify(txtPop).show(list, e.getX(), e.getY());
    }

    @Test
    public void testGetChatlog() {
        internalChatFrame.getChatlog();

        // verify if the text from the text pane is returned
        verify(showTxt).getText();
    }
}


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.event.ActionEvent;
import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setup() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord());

        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord());
    }

    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertNotNull(listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertNotNull(listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertNotNull(listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertNotNull(listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertNotNull(listItemDeleter);
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.setNewRecord(false);
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.isAllowRecordEditing());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertTrue(displayableListPanel.getAddButton().isEnabled());
        assertTrue(displayableListPanel.getSelectButton().isEnabled());

        displayableListPanel.setNewRecord(true);
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.getAddButton().isEnabled());
        assertFalse(displayableListPanel.getSelectButton().isEnabled());
        assertFalse(displayableListPanel.getDeleteButton().isEnabled());
        assertFalse(displayableListPanel.getEditButton().isEnabled());
        assertFalse(displayableListPanel.getViewButton().isEnabled());
    }

    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Test Owner";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.getListOwnerName());
    }

    @Test
    public void testActionPerformed() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        ActionEvent event = new ActionEvent(displayableListPanel.getAddButton(), ActionEvent.ACTION_PERFORMED, null);
        displayableListPanel.actionPerformed(event);
        assertTrue(listItemAdder.isAddItemsCalled());
    }
}

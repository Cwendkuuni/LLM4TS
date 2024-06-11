import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.awt.event.ActionEvent;

public class DisplayableListPanelTest {
    
    @Test
    public void testSetNewRecord() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setNewRecord(true);
        boolean isNewRecord = displayableListPanel.isNewRecord();
        assertTrue(isNewRecord);
    }
    
    @Test
    public void testGetPanel() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        assertNotNull(displayableListPanel.getPanel());
    }
    
    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertTrue(displayableListPanel.getListItemSelector() == listItemSelector);
    }
    
    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertTrue(displayableListPanel.getListItemAdder() == listItemAdder);
    }
    
    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertTrue(displayableListPanel.getListItemEditor() == listItemEditor);
    }
    
    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertTrue(displayableListPanel.getListItemViewer() == listItemViewer);
    }
    
    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertTrue(displayableListPanel.getListItemDeleter() == listItemDeleter);
    }
    
    @Test
    public void testUpdateButtonStates() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.updateButtonStates();
        JButton add = displayableListPanel.getAddButton();
        assertFalse(add.isEnabled());
        
        JButton delete = displayableListPanel.getDeleteButton();
        assertFalse(delete.isEnabled());
        
        JButton edit = displayableListPanel.getEditButton();
        assertFalse(edit.isEnabled());
        
        JButton view = displayableListPanel.getViewButton();
        assertFalse(view.isEnabled());
        
        JButton select = displayableListPanel.getSelectButton();
        assertFalse(select.isEnabled());
        
        displayableListPanel.setNewRecord(false);
        displayableListPanel.updateButtonStates();
        assertTrue(add.isEnabled());
        assertTrue(select.isEnabled());
        assertFalse(delete.isEnabled());
        assertFalse(edit.isEnabled());
        assertFalse(view.isEnabled());
        
        displayableListPanel.getDisplayableList().setAllItems(new ArrayList<Displayable>());
        displayableListPanel.updateButtonStates();
        assertFalse(delete.isEnabled());
        assertTrue(edit.isEnabled());
        
        displayableListPanel.setAllowRecordEditing(true);
        displayableListPanel.updateButtonStates();
        assertFalse(view.isEnabled());
        
        displayableListPanel.setAllowRecordEditing(false);
        displayableListPanel.updateButtonStates();
        assertTrue(view.isEnabled());
    }
    
    @Test
    public void testAcionPerformed() {
        DisplayableListItemSelector listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        
        displayableListPanel.actionPerformed(null);
        Mockito.verify(listItemSelector).selectListItems(Mockito.anyString(), Mockito.any(DisplayableList.class));
        
        DisplayableListItemAdder listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        displayableListPanel.actionPerformed(null);
        Mockito.verify(listItemAdder).addItems(Mockito.anyString(), Mockito.any(DisplayableList.class));
        
        DisplayableListItemEditor listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        displayableListPanel.actionPerformed(null);
        Mockito.verify(listItemEditor).editListItem(Mockito.anyString(), Mockito.any(Displayable.class), Mockito.any(DisplayableList.class));
        
        DisplayableListItemViewer listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        displayableListPanel.actionPerformed(null);
        Mockito.verify(listItemViewer).viewListItem(Mockito.any(Displayable.class));
        
        DisplayableListItemDeleter listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        displayableListPanel.actionPerformed(null);
        Mockito.verify(listItemDeleter).deleteListItems(Mockito.anyString(), Mockito.any(DisplayableList.class));
    }
}
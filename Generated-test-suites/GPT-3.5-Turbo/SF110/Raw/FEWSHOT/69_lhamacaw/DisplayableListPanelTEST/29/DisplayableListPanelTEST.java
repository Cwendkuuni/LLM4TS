package macaw.util;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

	private DisplayableListPanel panel;
	private ArrayList<Displayable> masterList;
	private SessionProperties sessionProperties;
	private Log log;
	private String panelTitleText;
	private String currentUserID;
	private UserInterfaceFactory userInterfaceFactory;
	private DisplayableListParentForm parentForm;
	private DisplayableListItemSelector listItemSelector;
	private DisplayableListItemAdder listItemAdder;
	private DisplayableListItemEditor listItemEditor;
	private DisplayableListItemViewer listItemViewer;
	private DisplayableListItemDeleter listItemDeleter;
	private String listOwnerName;
	private boolean allowWriteAccess;
	private boolean allowRecordEditing;
	private ArrayList<Displayable> masterSourceVariableCollection;

	@Before
	public void setUp() {
		sessionProperties = new SessionProperties();
		log = new Log();
		sessionProperties.setLog(log);
		panelTitleText = "Sample Panel Title";
		currentUserID = "123456";
		allowWriteAccess = true;
		allowRecordEditing = true;
		parentForm = new DisplayableListParentForm();
		userInterfaceFactory = new UserInterfaceFactory();
		listOwnerName = "SampleOwner";
		panel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
	}

	@Test
	public void testSetNewRecord() {
		panel.setNewRecord(true);
		assertTrue(panel.isNewRecord);
		panel.setNewRecord(false);
		assertFalse(panel.isNewRecord);
	}

	@Test
	public void testGetPanel() {
		assertNotNull(panel.getPanel());
	}

	@Test
	public void testUpdateButtonStates() {
		panel.setNewRecord(false);
		panel.updateButtonStates();
		assertTrue(panel.add.isEnabled());
		assertTrue(panel.select.isEnabled());
		assertTrue(panel.edit.isEnabled());
		assertTrue(panel.view.isEnabled());
		assertTrue(panel.delete.isEnabled());

		panel.setNewRecord(true);
		panel.updateButtonStates();
		assertFalse(panel.add.isEnabled());
		assertFalse(panel.select.isEnabled());
		assertFalse(panel.edit.isEnabled());
		assertFalse(panel.view.isEnabled());
		assertFalse(panel.delete.isEnabled());

		panel.setNewRecord(false);
		panel.displayableList.setDisplayItems(new ArrayList<Displayable>());
		panel.updateButtonStates();
		assertFalse(panel.add.isEnabled());
		assertTrue(panel.select.isEnabled());
		assertFalse(panel.edit.isEnabled());
		assertFalse(panel.view.isEnabled());
		assertFalse(panel.delete.isEnabled());

		ArrayList<Displayable> items = new ArrayList<Displayable>();
		items.add(new Displayable());
		panel.displayableList.setDisplayItems(items);
		panel.updateButtonStates();
		assertTrue(panel.add.isEnabled());
		assertTrue(panel.select.isEnabled());
		assertTrue(panel.edit.isEnabled());
		assertFalse(panel.view.isEnabled());
		assertTrue(panel.delete.isEnabled());

		panel.allowWriteAccess = false;
		panel.updateButtonStates();
		assertFalse(panel.add.isEnabled());
		assertFalse(panel.select.isEnabled());
		assertFalse(panel.edit.isEnabled());
		assertTrue(panel.view.isEnabled());
		assertFalse(panel.delete.isEnabled());
	}

	@Test
	public void testSetDisplayableItems() {
		masterList = new ArrayList<Displayable>();
		panel.setDisplayableItems(masterList);
		assertEquals(masterList, panel.masterList);
		assertEquals(masterList, panel.displayableList.getDisplayItems());
	}
}
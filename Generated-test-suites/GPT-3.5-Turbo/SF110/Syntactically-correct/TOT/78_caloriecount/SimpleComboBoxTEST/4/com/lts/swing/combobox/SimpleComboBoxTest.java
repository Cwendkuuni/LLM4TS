import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        comboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @After
    public void tearDown() {
        comboBox = null;
    }

    @Test
    public void testSetModel_ValidModel_AssignModel() {
        // Arrange
        ComboBoxModel model = new SimpleComboBoxModel();

        // Act
        comboBox.setModel(model);

        // Assert
        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testSetModel_InvalidModel_DoNotAssignModel() {
        // Arrange
        ComboBoxModel model = new DefaultComboBoxModel();

        // Act
        comboBox.setModel(model);

        // Assert
        assertNotEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NoModel_ReturnNull() {
        // Arrange

        // Act
        Object selectedValue = comboBox.getSelectedValue();

        // Assert
        assertNull(selectedValue);
    }

    @Test
    public void testGetSelectedValue_ModelWithNoSelection_ReturnNull() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        // Act
        Object selectedValue = comboBox.getSelectedValue();

        // Assert
        assertNull(selectedValue);
    }

    @Test
    public void testGetSelectedValue_ModelWithSelection_ReturnSelectedValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option 1");
        model.addElement("Option 2");
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        Object selectedValue = comboBox.getSelectedValue();

        // Assert
        assertEquals("Option 2", selectedValue);
    }

    @Test
    public void testSetSelectedValue_ModelWithSelection_SelectNewValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option 1");
        model.addElement("Option 2");
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedValue("Option 1");
        Object selectedValue = comboBox.getSelectedValue();

        // Assert
        assertEquals("Option 1", selectedValue);
    }

    @Test
    public void testGetSelectedInt_ModelWithIntegerSelection_ReturnSelectedIntValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        int selectedInt = comboBox.getSelectedInt();

        // Assert
        assertEquals(2, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ModelWithLongSelection_ReturnSelectedIntValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((long) 1);
        model.addElement((long) 1000);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        int selectedInt = comboBox.getSelectedInt();

        // Assert
        assertEquals(1000, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ModelWithShortSelection_ReturnSelectedIntValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 10);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        int selectedInt = comboBox.getSelectedInt();

        // Assert
        assertEquals(10, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ModelWithByteSelection_ReturnSelectedIntValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 5);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        int selectedInt = comboBox.getSelectedInt();

        // Assert
        assertEquals(5, selectedInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_ModelWithInvalidSelection_ThrowIllegalArgumentException() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(0);
        int selectedInt = comboBox.getSelectedInt();

        // Assert (Exception expected)
    }

    @Test
    public void testGetSelectedLong_ModelWithLongSelection_ReturnSelectedLongValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((long) 1);
        model.addElement((long) 1000);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        long selectedLong = comboBox.getSelectedLong();

        // Assert
        assertEquals(1000, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ModelWithIntegerSelection_ReturnSelectedLongValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        long selectedLong = comboBox.getSelectedLong();

        // Assert
        assertEquals(2, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ModelWithShortSelection_ReturnSelectedLongValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 10);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        long selectedLong = comboBox.getSelectedLong();

        // Assert
        assertEquals(10, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ModelWithByteSelection_ReturnSelectedLongValue() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 5);
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(1);
        long selectedLong = comboBox.getSelectedLong();

        // Assert
        assertEquals(5, selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_ModelWithInvalidSelection_ThrowIllegalArgumentException() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        comboBox.setModel(model);

        // Act
        comboBox.setSelectedIndex(0);
        long selectedLong = comboBox.getSelectedLong();

        // Assert (Exception expected)
    }
}
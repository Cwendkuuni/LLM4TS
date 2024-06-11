import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel_withSimpleComboBoxModel_shouldSetSimpleComboBoxModel() {
        // Arrange
        SimpleComboBoxModel model = new SimpleComboBoxModel();

        // Act
        simpleComboBox.setModel(model);

        // Assert
        assertEquals(model, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testSetModel_withNonSimpleComboBoxModel_shouldNotSetSimpleComboBoxModel() {
        // Arrange
        ComboBoxModel model = new DefaultComboBoxModel();

        // Act
        simpleComboBox.setModel(model);

        // Assert
        assertNull(simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue_withNullSimpleComboBoxModel_shouldReturnNull() {
        // Arrange
        simpleComboBox.mySimpleModel = null;

        // Act
        Object selectedValue = simpleComboBox.getSelectedValue();

        // Assert
        assertNull(selectedValue);
    }

    @Test
    public void testGetSelectedValue_withNonNullSimpleComboBoxModel_shouldReturnSelectedValue() {
        // Arrange
        Object value = "selected";
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        Object selectedValue = simpleComboBox.getSelectedValue();

        // Assert
        assertEquals(value, selectedValue);
    }

    @Test
    public void testSetSelectedValue_withNonNullSimpleComboBoxModel_shouldSetSelectedValue() {
        // Arrange
        Object value = "selected";
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();

        // Act
        simpleComboBox.setSelectedValue(value);

        // Assert
        assertEquals(value, simpleComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_withNullSelectedValue_shouldReturnNegativeOne() {
        // Arrange
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();

        // Act
        int selectedInt = simpleComboBox.getSelectedInt();

        // Assert
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedInt_withIntegerSelectedValue_shouldReturnIntegerSelectedValue() {
        // Arrange
        int value = 42;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        int selectedInt = simpleComboBox.getSelectedInt();

        // Assert
        assertEquals(value, selectedInt);
    }

    @Test
    public void testGetSelectedInt_withLongSelectedValue_shouldReturnCastedLongValue() {
        // Arrange
        long value = 12345L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        int selectedInt = simpleComboBox.getSelectedInt();

        // Assert
        assertEquals((int) value, selectedInt);
    }

    @Test
    public void testGetSelectedInt_withShortSelectedValue_shouldReturnShortSelectedValue() {
        // Arrange
        short value = 100;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        int selectedInt = simpleComboBox.getSelectedInt();

        // Assert
        assertEquals(value, selectedInt);
    }

    @Test
    public void testGetSelectedInt_withByteSelectedValue_shouldReturnByteSelectedValue() {
        // Arrange
        byte value = 2;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        int selectedInt = simpleComboBox.getSelectedInt();

        // Assert
        assertEquals(value, selectedInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_withInvalidSelectedValue_shouldThrowIllegalArgumentException() {
        // Arrange
        Object value = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_withNullSelectedValue_shouldReturnNegativeOne() {
        // Arrange
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();

        // Act
        long selectedLong = simpleComboBox.getSelectedLong();

        // Assert
        assertEquals(-1L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_withIntegerSelectedValue_shouldReturnIntegerSelectedValue() {
        // Arrange
        int value = 42;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        long selectedLong = simpleComboBox.getSelectedLong();

        // Assert
        assertEquals((long) value, selectedLong);
    }

    @Test
    public void testGetSelectedLong_withLongSelectedValue_shouldReturnLongSelectedValue() {
        // Arrange
        long value = 12345L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        long selectedLong = simpleComboBox.getSelectedLong();

        // Assert
        assertEquals(value, selectedLong);
    }

    @Test
    public void testGetSelectedLong_withShortSelectedValue_shouldReturnShortSelectedValue() {
        // Arrange
        short value = 100;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        long selectedLong = simpleComboBox.getSelectedLong();

        // Assert
        assertEquals((long) value, selectedLong);
    }

    @Test
    public void testGetSelectedLong_withByteSelectedValue_shouldReturnByteSelectedValue() {
        // Arrange
        byte value = 2;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        long selectedLong = simpleComboBox.getSelectedLong();

        // Assert
        assertEquals((long) value, selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_withInvalidSelectedValue_shouldThrowIllegalArgumentException() {
        // Arrange
        Object value = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);

        // Act
        simpleComboBox.getSelectedLong();
    }
}
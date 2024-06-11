import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBoxModel model = new SimpleComboBoxModel();
    private SimpleComboBox comboBox = new SimpleComboBox(model);

    @Test
    public void testSetModelWithSimpleComboBoxModel() {
        ComboBoxModel comboBoxModel = new SimpleComboBoxModel();
        comboBox.setModel(comboBoxModel);
        assertEquals(comboBoxModel, comboBox.getModel());
    }

    @Test
    public void testSetModelWithRegularComboBoxModel() {
        ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        comboBox.setModel(comboBoxModel);
        assertEquals(comboBoxModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWithoutModel() {
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithModel() {
        Object value = "Test Value";
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithValidValue() {
        Object value = "Test Value";
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidValue() {
        comboBox.setSelectedValue(new Object()); // Invalid value
    }

    @Test
    public void testGetSelectedIntWithoutModel() {
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        Integer value = 10;
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        Long value = 10L;
        comboBox.setSelectedValue(value);
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        Short value = 10;
        comboBox.setSelectedValue(value);
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        Byte value = 10;
        comboBox.setSelectedValue(value);
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        comboBox.setSelectedValue("Invalid Value");
        comboBox.getSelectedInt(); // Invalid value
    }

    @Test
    public void testGetSelectedLongWithoutModel() {
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        Integer value = 10;
        comboBox.setSelectedValue(value);
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        Long value = 10L;
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        Short value = 10;
        comboBox.setSelectedValue(value);
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        Byte value = 10;
        comboBox.setSelectedValue(value);
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        comboBox.setSelectedValue("Invalid Value");
        comboBox.getSelectedLong(); // Invalid value
    }
}
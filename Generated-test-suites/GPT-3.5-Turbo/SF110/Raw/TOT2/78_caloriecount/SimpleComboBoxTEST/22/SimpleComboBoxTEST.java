import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);

        assertEquals(newModel, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals("test", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue("test");
        assertEquals("test", comboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(100L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("invalid");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("invalid");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.getSelectedLong();
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);
        assertEquals(newModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertNull(comboBox.getSelectedValue());
        comboBox.setSelectedIndex(0);
        assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue("Test1");
        assertEquals("Test1", comboBox.getSelectedItem());
        comboBox.setSelectedValue("Test2");
        assertEquals("Test2", comboBox.getSelectedItem());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedIndex(0);
        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedIndex(0);
        assertEquals(100L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedIndex(0);
        comboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedIndex(0);
        comboBox.getSelectedLong();
    }
}
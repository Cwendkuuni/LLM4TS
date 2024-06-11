
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    // SimpleComboBox constructor test
    @Test
    public void testConstructor() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNotNull(comboBox);
    }

    // setModel() test
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);

        assertEquals(newModel, comboBox.getModel());
    }

    // getSelectedValue() test
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedItem("Item 2");

        assertEquals("Item 2", comboBox.getSelectedValue());
    }

    // setSelectedValue() test
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue("Item 2");

        assertEquals("Item 2", comboBox.getSelectedValue());
    }

    // getSelectedInt() test
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        model.addElement(20);

        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedItem(20);

        assertEquals(20, comboBox.getSelectedInt());
    }

    // getSelectedLong() test
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10L);
        model.addElement(20L);

        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedItem(20L);

        assertEquals(20L, comboBox.getSelectedLong());
    }

    // Test IllegalArgumentException for getSelectedInt() when the selected value is not a numeric type
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNonNumericValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedItem("Item 2");

        comboBox.getSelectedInt();
    }

    // Test IllegalArgumentException for getSelectedLong() when the selected value is not a numeric type
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNonNumericValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedItem("Item 2");

        comboBox.getSelectedLong();
    }
}

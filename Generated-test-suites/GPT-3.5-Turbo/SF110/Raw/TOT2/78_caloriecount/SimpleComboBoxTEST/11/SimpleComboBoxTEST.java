import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        // Test if setModel method sets the model correctly
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);
        assertEquals(newModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        // Test if getSelectedValue method returns the correct selected value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value1");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedItem("Value1");
        assertEquals("Value1", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        // Test if setSelectedValue method sets the selected value correctly
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("Value1");
        assertEquals("Value1", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        // Test if getSelectedInt method returns the correct selected integer value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedItem(10);
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        // Test if getSelectedLong method returns the correct selected long value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedItem(100L);
        assertEquals(100L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntInvalid() {
        // Test if getSelectedInt method throws IllegalArgumentException for invalid type
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedItem("String");
        comboBox.getSelectedInt();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongInvalid() {
        // Test if getSelectedLong method throws IllegalArgumentException for invalid type
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedItem("String");
        comboBox.getSelectedLong();
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        ComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);
        
        assertEquals(newModel, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Object selectedValue = comboBox.getSelectedValue();
        
        assertEquals("Test Value", selectedValue);
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Test Value");
        
        assertEquals("Test Value", model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        int selectedInt = comboBox.getSelectedInt();
        
        assertEquals(10, selectedInt);
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        long selectedLong = comboBox.getSelectedLong();
        
        assertEquals(100L, selectedLong);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Invalid Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.getSelectedInt();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Invalid Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.getSelectedLong();
    }
}
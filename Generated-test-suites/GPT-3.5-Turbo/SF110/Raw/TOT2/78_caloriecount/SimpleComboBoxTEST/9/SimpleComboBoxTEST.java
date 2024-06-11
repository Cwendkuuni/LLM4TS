import static org.junit.Assert.*;
import org.junit.Test;

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
        model.addElement("value1");
        model.addElement("value2");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals("value2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("new value");
        
        assertEquals("new value", comboBox.getSelectedValue());
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
    public void testGetSelectedIntInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("invalid");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        comboBox.getSelectedInt();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("invalid");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        comboBox.getSelectedLong();
    }
}
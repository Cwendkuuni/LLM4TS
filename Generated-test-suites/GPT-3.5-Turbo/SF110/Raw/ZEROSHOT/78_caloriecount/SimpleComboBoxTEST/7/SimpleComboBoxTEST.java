import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);
        
        assertEquals(model, comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals("Value 1", comboBox.getSelectedValue());
        
        comboBox.setSelectedIndex(1);
        assertEquals("Value 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Value 1");
        assertEquals("Value 1", comboBox.getSelectedValue());
        
        comboBox.setSelectedValue("Value 2");
        assertEquals("Value 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
        
        comboBox.setSelectedIndex(1);
        assertEquals(2, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        model.addElement(200L);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedIndex(0);
        assertEquals(100L, comboBox.getSelectedLong());
        
        comboBox.setSelectedIndex(1);
        assertEquals(200L, comboBox.getSelectedLong());
    }
}
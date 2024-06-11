import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    @Test
    public void testConstructor() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertNotNull(comboBox);
        assertEquals(model, comboBox.getModel());
    }
    
    @Test
    public void testSetModel() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        
        final ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);
        
        assertEquals(newModel, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals("Value 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Value 2");
        assertEquals(1, comboBox.getSelectedIndex());
    }
    
    @Test
    public void testGetSelectedInt() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        assertEquals(100L, comboBox.getSelectedLong());
    }
}
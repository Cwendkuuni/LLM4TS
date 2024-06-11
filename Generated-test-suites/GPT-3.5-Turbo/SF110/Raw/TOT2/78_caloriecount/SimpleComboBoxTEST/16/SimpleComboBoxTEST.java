import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);
        
        assertSame(newModel, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem("Item 1");
        assertEquals("Item 1", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Item 2");
        
        assertEquals("Item 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem(10);
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem(100L);
        assertEquals(100L, comboBox.getSelectedLong());
    }
}
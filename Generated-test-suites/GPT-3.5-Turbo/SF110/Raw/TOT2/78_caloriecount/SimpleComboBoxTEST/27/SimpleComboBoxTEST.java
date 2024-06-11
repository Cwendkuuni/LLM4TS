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
        
        comboBox.setSelectedIndex(0);
        
        assertEquals("Test", comboBox.getSelectedValue());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertNull(comboBox.getSelectedValue());
        
        model.add("Item 1");
        model.add("Item 2");
        model.setSelectedValue("Item 2");
        
        assertEquals("Item 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertNull(comboBox.getSelectedValue());
        
        model.add("Item 1");
        model.add("Item 2");
        comboBox.setSelectedValue("Item 2");
        
        assertEquals("Item 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.add(1);
        model.add(2);
        model.setSelectedValue(2);
        
        assertEquals(2, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.add(1L);
        model.add(2L);
        model.setSelectedValue(2L);
        
        assertEquals(2L, comboBox.getSelectedLong());
    }
    
}
import org.junit.Test;
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
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertNull(comboBox.getSelectedValue());
        
        model.addElement("Test");
        comboBox.setSelectedIndex(0);
        assertEquals("Test", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Test");
        assertEquals("Test", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.addElement(123);
        comboBox.setSelectedIndex(0);
        assertEquals(123, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.addElement(123L);
        comboBox.setSelectedIndex(0);
        assertEquals(123L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.addElement("Test");
        comboBox.setSelectedIndex(0);
        comboBox.getSelectedInt();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.addElement("Test");
        comboBox.setSelectedIndex(0);
        comboBox.getSelectedLong();
    }
}
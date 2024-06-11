
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
    public void testGetSelectedValue_NullModel() {
        SimpleComboBoxModel model = null;
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals("Item 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        comboBox.setSelectedValue("Item 2");
        
        assertEquals("Item 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        
        assertEquals(2L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        comboBox.getSelectedLong();
    }
}


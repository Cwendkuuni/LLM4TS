import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    @Test
    public void testGetSelectedValue_NullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_WithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals("test", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("test");
        
        assertEquals("test", model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_IllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_IllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.getSelectedLong();
    }
}
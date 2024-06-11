import static org.junit.Assert.*;

import javax.swing.DefaultComboBoxModel;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleModel;

    @Before
    public void setup() {
        simpleModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleModel);
    }
    
    @Test
    public void testSetModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
        
        String value = "Test";
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        String value = "Test";
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        int intValue = 10;
        simpleModel.setSelectedValue(intValue);
        assertEquals(intValue, simpleComboBox.getSelectedInt());
        
        long longValue = 20L;
        simpleModel.setSelectedValue(longValue);
        assertEquals((int) longValue, simpleComboBox.getSelectedInt());
        
        short shortValue = 30;
        simpleModel.setSelectedValue(shortValue);
        assertEquals(shortValue, simpleComboBox.getSelectedInt());
        
        byte byteValue = 40;
        simpleModel.setSelectedValue(byteValue);
        assertEquals(byteValue, simpleComboBox.getSelectedInt());
        
        assertNull(simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        long longValue = 20L;
        simpleModel.setSelectedValue(longValue);
        assertEquals(longValue, simpleComboBox.getSelectedLong());
        
        int intValue = 10;
        simpleModel.setSelectedValue(intValue);
        assertEquals(intValue, simpleComboBox.getSelectedLong());
        
        short shortValue = 30;
        simpleModel.setSelectedValue(shortValue);
        assertEquals((long) shortValue, simpleComboBox.getSelectedLong());
        
        byte byteValue = 40;
        simpleModel.setSelectedValue(byteValue);
        assertEquals((long) byteValue, simpleComboBox.getSelectedLong());
        
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }
}
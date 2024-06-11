import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        int intValue = 5;
        simpleComboBox.setSelectedValue(intValue);
        assertEquals(intValue, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        long longValue = 100L;
        simpleComboBox.setSelectedValue(longValue);
        assertEquals(longValue, simpleComboBox.getSelectedLong());
    }
}
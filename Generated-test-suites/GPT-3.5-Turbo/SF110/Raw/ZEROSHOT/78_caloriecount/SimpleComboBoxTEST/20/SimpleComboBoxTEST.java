import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel model;
    
    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel() {
        ComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
        
        Object value = new Object();
        model.setSelectedValue(value);
        selectedValue = simpleComboBox.getSelectedValue();
        assertEquals(value, selectedValue);
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
        
        int value = 5;
        model.setSelectedValue(value);
        selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(value, selectedInt);
        
        short shortValue = 2;
        model.setSelectedValue(shortValue);
        selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(shortValue, selectedInt);
        
        byte byteValue = 3;
        model.setSelectedValue(byteValue);
        selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(byteValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedLong() {
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);
        
        long value = 10L;
        model.setSelectedValue(value);
        selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(value, selectedLong);
        
        int intValue = 20;
        model.setSelectedValue(intValue);
        selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(intValue, selectedLong);
        
        short shortValue = 30;
        model.setSelectedValue(shortValue);
        selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(shortValue, selectedLong);
        
        byte byteValue = 40;
        model.setSelectedValue(byteValue);
        selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(byteValue, selectedLong);
    }
}


// Placeholder for the test file.


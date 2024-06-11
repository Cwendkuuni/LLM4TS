import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;
    private SimpleComboBoxModel model;
    
    @Before
    public void setup() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel_ValidModel_AssignsModel() {
        ComboBoxModel newModel = new SimpleComboBoxModel();
        
        comboBox.setModel(newModel);
        
        assertEquals(newModel, comboBox.getModel());
    }
    
    @Test
    public void testSetModel_InvalidModel_DoesNotAssignModel() {
        ComboBoxModel newModel = new DefaultComboBoxModel();
        
        comboBox.setModel(newModel);
        
        assertEquals(model, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue_NoModel_ReturnsNull() {
        Object selectedValue = comboBox.getSelectedValue();
        
        assertEquals(null, selectedValue);
    }
    
    @Test
    public void testGetSelectedValue_ModelWithSelectedValue_ReturnsSelectedValue() {
        Object expectedValue = "Test";
        model.setSelectedValue(expectedValue);
        
        Object selectedValue = comboBox.getSelectedValue();
        
        assertEquals(expectedValue, selectedValue);
    }
    
    @Test
    public void testSetSelectedValue_ValidValue_SetsSelectedValue() {
        Object value = "Test";
        
        comboBox.setSelectedValue(value);
        
        assertEquals(value, model.getSelectedValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValue_InvalidValue_ThrowsIllegalArgumentException() {
        Object value = new Object();
        
        comboBox.setSelectedValue(value);
    }
    
    @Test
    public void testGetSelectedInt_NoModel_ReturnsNegativeOne() {
        int selectedInt = comboBox.getSelectedInt();
        
        assertEquals(-1, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_ModelWithInteger_ReturnsIntegerValue() {
        int expectedValue = 10;
        model.setSelectedValue(expectedValue);
        
        int selectedInt = comboBox.getSelectedInt();
        
        assertEquals(expectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_ModelWithLong_ReturnsCastLongValue() {
        long expectedValue = 10L;
        model.setSelectedValue(expectedValue);
        
        int selectedInt = comboBox.getSelectedInt();
        
        assertEquals((int)expectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_ModelWithShort_ReturnsShortValue() {
        short expectedValue = 10;
        model.setSelectedValue(expectedValue);
        
        int selectedInt = comboBox.getSelectedInt();
        
        assertEquals(expectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_ModelWithByte_ReturnsByteValue() {
        byte expectedValue = 10;
        model.setSelectedValue(expectedValue);
        
        int selectedInt = comboBox.getSelectedInt();
        
        assertEquals(expectedValue, selectedInt);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_ModelWithInvalidType_ThrowsIllegalArgumentException() {
        Object value = new Object();
        model.setSelectedValue(value);
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_NoModel_ReturnsNegativeOne() {
        long selectedLong = comboBox.getSelectedLong();
        
        assertEquals(-1L, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_ModelWithInteger_ReturnsIntegerValue() {
        int expectedValue = 10;
        model.setSelectedValue(expectedValue);
        
        long selectedLong = comboBox.getSelectedLong();
        
        assertEquals(expectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_ModelWithLong_ReturnsLongValue() {
        long expectedValue = 10L;
        model.setSelectedValue(expectedValue);
        
        long selectedLong = comboBox.getSelectedLong();
        
        assertEquals(expectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_ModelWithShort_ReturnsShortValue() {
        short expectedValue = 10;
        model.setSelectedValue(expectedValue);
        
        long selectedLong = comboBox.getSelectedLong();
        
        assertEquals(expectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_ModelWithByte_ReturnsByteValue() {
        byte expectedValue = 10;
        model.setSelectedValue(expectedValue);
        
        long selectedLong = comboBox.getSelectedLong();
        
        assertEquals(expectedValue, selectedLong);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_ModelWithInvalidType_ThrowsIllegalArgumentException() {
        Object value = new Object();
        model.setSelectedValue(value);
        
        comboBox.getSelectedLong();
    }
}
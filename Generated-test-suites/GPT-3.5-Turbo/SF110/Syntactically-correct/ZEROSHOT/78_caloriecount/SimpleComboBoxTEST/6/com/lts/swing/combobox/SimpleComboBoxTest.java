import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        
        assertEquals(model, comboBox.mySimpleModel);
        assertEquals(model, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        assertNull(comboBox.getSelectedValue());
        
        String value = "Test Value";
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        String value = "Test Value";
        comboBox.setSelectedValue(value);
        
        assertEquals(value, comboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        assertEquals(-1, comboBox.getSelectedInt());
        
        int value = 10;
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        assertEquals(-1L, comboBox.getSelectedLong());
        
        long value = 100L;
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedLong());
    }
}
import org.junit.Before;
import org.junit.Test;
import javax.swing.DefaultComboBoxModel;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    private SimpleComboBox comboBox;
    private SimpleComboBoxModel model;
    
    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        model.addElement("Item 3");
        comboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel_ValidModel_UpdatesSimpleModel() {
        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);
        
        assertEquals(newModel, comboBox.getModel());
        assertNull(comboBox.mySimpleModel);
    }
    
    @Test
    public void testSetModel_InvalidModel_NoChanges() {
        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.mySimpleModel = model; // Existing simple model
        
        comboBox.setModel(newModel);
        
        assertEquals(model, comboBox.mySimpleModel);
        assertNotEquals(newModel, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue_SimpleModel_ReturnsSelectedValue() {
        comboBox.setSelectedIndex(1);
        
        assertEquals("Item 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_NullSimpleModel_ReturnsNull() {
        comboBox.setSelectedIndex(1);
        comboBox.mySimpleModel = null;
        
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue_SimpleModel_SetsSelectedValue() {
        comboBox.setSelectedValue("Item 3");
        
        assertEquals("Item 3", comboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_IntegerValue_ReturnsCorrectValue() {
        comboBox.setSelectedIndex(1);
        
        assertEquals(1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_LongValue_ReturnsCastedValue() {
        comboBox.setSelectedValue(100L);
        
        assertEquals(100, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ShortValue_ReturnsCorrectValue() {
        comboBox.setSelectedValue((short) 10);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ByteValue_ReturnsCorrectValue() {
        comboBox.setSelectedValue((byte) 5);
        
        assertEquals(5, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_UnsupportedType_ThrowsException() {
        comboBox.setSelectedValue("Item");
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_IntegerValue_ReturnsCastedValue() {
        comboBox.setSelectedValue(200);
        
        assertEquals(200L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_LongValue_ReturnsCorrectValue() {
        comboBox.setSelectedValue(1000L);
        
        assertEquals(1000L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ShortValue_ReturnsCorrectValue() {
        comboBox.setSelectedValue((short) 20);
        
        assertEquals(20L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ByteValue_ReturnsCorrectValue() {
        comboBox.setSelectedValue((byte) 3);
        
        assertEquals(3L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_UnsupportedType_ThrowsException() {
        comboBox.setSelectedValue("Item");
        
        comboBox.getSelectedLong();
    }
}
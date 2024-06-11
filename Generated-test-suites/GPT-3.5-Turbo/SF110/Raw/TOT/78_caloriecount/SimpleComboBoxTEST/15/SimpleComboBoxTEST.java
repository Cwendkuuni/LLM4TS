import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNotNull(simpleComboBox);
    }

    // Test case for setModel method with SimpleComboBoxModel
    @Test
    public void testSetModelWithValidModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        
        assertEquals(newModel, simpleComboBox.mySimpleModel);
    }
    
    // Test case for setModel method with another ComboBoxModel
    @Test
    public void testSetModelWithInvalidModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        DefaultComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        
        assertEquals(null, simpleComboBox.mySimpleModel);
    }
    
    // Test case for getSelectedValue method with valid selection
    @Test
    public void testGetSelectedValueWithValidSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        model.addElement("test");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedItem("test");
        assertEquals("test", simpleComboBox.getSelectedValue());
    }
    
    // Test case for getSelectedValue method with null selection
    @Test
    public void testGetSelectedValueWithNullSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("test");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedItem(null);
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    // Test case for setSelectedValue method with valid value
    @Test
    public void testSetSelectedValueWithValidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.mySimpleModel.getSelectedValue());
    }
    
    // Test case for getSelectedInt method with valid selection as Integer
    @Test
    public void testGetSelectedIntWithIntegerSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }
    
    // Test case for getSelectedInt method with valid selection as Long
    @Test
    public void testGetSelectedIntWithLongSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100000000000L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue(100000000000L);
        assertEquals(-205032704, simpleComboBox.getSelectedInt());
    }
    
    // Test case for getSelectedInt method with valid selection as Short
    @Test
    public void testGetSelectedIntWithShortSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 100);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue((short) 100);
        assertEquals(100, simpleComboBox.getSelectedInt());
    }
    
    // Test case for getSelectedInt method with valid selection as Byte
    @Test
    public void testGetSelectedIntWithByteSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue((byte) 10);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }
    
    // Test case for getSelectedInt method with null selection
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNullSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("test");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedItem(null);
        simpleComboBox.getSelectedInt();
    }
    
    // Test case for getSelectedLong method with valid selection as Integer
    @Test
    public void testGetSelectedLongWithIntegerSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue(10);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }
    
    // Test case for getSelectedLong method with valid selection as Long
    @Test
    public void testGetSelectedLongWithLongSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100000000000L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue(100000000000L);
        assertEquals(100000000000L, simpleComboBox.getSelectedLong());
    }
    
    // Test case for getSelectedLong method with valid selection as Short
    @Test
    public void testGetSelectedLongWithShortSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 100);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue((short) 100);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }
    
    // Test case for getSelectedLong method with valid selection as Byte
    @Test
    public void testGetSelectedLongWithByteSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 10);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedValue((byte) 10);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }
    
    // Test case for getSelectedLong method with null selection
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNullSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("test");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        
        simpleComboBox.setSelectedItem(null);
        simpleComboBox.getSelectedLong();
    }
}
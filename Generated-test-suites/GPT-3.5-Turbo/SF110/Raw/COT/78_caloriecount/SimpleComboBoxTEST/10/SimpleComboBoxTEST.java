
import static org.junit.Assert.*;
import org.junit.*;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
        simpleComboBox = new SimpleComboBox(null);
    }

    @Test
    public void testSetModel() {
        // Test when model is SimpleComboBoxModel
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.mySimpleModel);
        
        // Test when model is not SimpleComboBoxModel
        DefaultComboBoxModel defaultModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(defaultModel);
        assertNull(simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        // Test when mySimpleModel is null
        assertNull(simpleComboBox.getSelectedValue());
        
        // Test when mySimpleModel is not null
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        simpleComboBox.setModel(model);
        assertEquals("Value 1", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        
        simpleComboBox.setSelectedValue("Value 1");
        assertEquals("Value 1", simpleComboBox.getSelectedValue());
        
        simpleComboBox.setSelectedValue("Value 2");
        assertEquals("Value 2", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        // Test when selected value is Integer
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        simpleComboBox.setModel(model);
        assertEquals(10, simpleComboBox.getSelectedInt());
        
        // Test when selected value is Long
        model.removeAllElements();
        model.addElement(10000L);
        simpleComboBox.setModel(model);
        assertEquals(10000, simpleComboBox.getSelectedInt());
        
        // Test when selected value is Short
        model.removeAllElements();
        model.addElement((short)5);
        simpleComboBox.setModel(model);
        assertEquals(5, simpleComboBox.getSelectedInt());
        
        // Test when selected value is Byte
        model.removeAllElements();
        model.addElement((byte)3);
        simpleComboBox.setModel(model);
        assertEquals(3, simpleComboBox.getSelectedInt());
        
        // Test when selected value is null
        model.removeAllElements();
        simpleComboBox.setModel(model);
        assertEquals(-1, simpleComboBox.getSelectedInt());
        
        // Test when selected value is of invalid type
        model.removeAllElements();
        model.addElement("Invalid Value");
        simpleComboBox.setModel(model);
        try {
            simpleComboBox.getSelectedInt();
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSelectedLong() {
        // Test when selected value is Integer
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1000);
        simpleComboBox.setModel(model);
        assertEquals(1000L, simpleComboBox.getSelectedLong());
        
        // Test when selected value is Long
        model.removeAllElements();
        model.addElement(100000L);
        simpleComboBox.setModel(model);
        assertEquals(100000L, simpleComboBox.getSelectedLong());
        
        // Test when selected value is Short
        model.removeAllElements();
        model.addElement((short)50);
        simpleComboBox.setModel(model);
        assertEquals(50L, simpleComboBox.getSelectedLong());
        
        // Test when selected value is Byte
        model.removeAllElements();
        model.addElement((byte)30);
        simpleComboBox.setModel(model);
        assertEquals(30L, simpleComboBox.getSelectedLong());
        
        // Test when selected value is null
        model.removeAllElements();
        simpleComboBox.setModel(model);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
        
        // Test when selected value is of invalid type
        model.removeAllElements();
        model.addElement("Invalid Value");
        simpleComboBox.setModel(model);
        try {
            simpleComboBox.getSelectedLong();
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
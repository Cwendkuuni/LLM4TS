import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        
        Assert.assertEquals(newModel, simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        // Test case 1: SimpleComboBoxModel is null
        simpleComboBox.mySimpleModel = null;
        Assert.assertNull(simpleComboBox.getSelectedValue());
        
        // Test case 2: SimpleComboBoxModel is not null
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        simpleComboBox.mySimpleModel = model;
        
        Assert.assertEquals("Test", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel = model;
        
        // Test case 1: Set selected value to null
        simpleComboBox.setSelectedValue(null);
        Assert.assertNull(model.getSelectedValue());
        
        // Test case 2: Set selected value to a String
        simpleComboBox.setSelectedValue("Test");
        Assert.assertEquals("Test", model.getSelectedValue());
        
        // Test case 3: Set selected value to an Integer
        simpleComboBox.setSelectedValue(10);
        Assert.assertEquals(10, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        // Test case 1: Selected value is null
        simpleComboBox.mySimpleModel = null;
        Assert.assertEquals(-1, simpleComboBox.getSelectedInt());
        
        // Test case 2: Selected value is an Integer
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        simpleComboBox.mySimpleModel = model;
        Assert.assertEquals(10, simpleComboBox.getSelectedInt());
        
        // Test case 3: Selected value is a Long
        model.setSelectedValue(100000000000L);
        Assert.assertEquals(100000000000, simpleComboBox.getSelectedInt());
        
        // Test case 4: Selected value is a Short
        model.setSelectedValue((short) 100);
        Assert.assertEquals(100, simpleComboBox.getSelectedInt());
        
        // Test case 5: Selected value is a Byte
        model.setSelectedValue((byte) 1);
        Assert.assertEquals(1, simpleComboBox.getSelectedInt());
        
        // Test case 6: Selected value is not a valid type
        model.setSelectedValue("Test");
        try {
            simpleComboBox.getSelectedInt();
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSelectedLong() {
        // Test case 1: Selected value is null
        simpleComboBox.mySimpleModel = null;
        Assert.assertEquals(-1L, simpleComboBox.getSelectedLong());
        
        // Test case 2: Selected value is an Integer
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        simpleComboBox.mySimpleModel = model;
        Assert.assertEquals(10L, simpleComboBox.getSelectedLong());
        
        // Test case 3: Selected value is a Long
        model.setSelectedValue(100000000000L);
        Assert.assertEquals(100000000000L, simpleComboBox.getSelectedLong());
        
        // Test case 4: Selected value is a Short
        model.setSelectedValue((short) 100);
        Assert.assertEquals(100L, simpleComboBox.getSelectedLong());
        
        // Test case 5: Selected value is a Byte
        model.setSelectedValue((byte) 1);
        Assert.assertEquals(1L, simpleComboBox.getSelectedLong());
        
        // Test case 6: Selected value is not a valid type
        model.setSelectedValue("Test");
        try {
            simpleComboBox.getSelectedLong();
            Assert.fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}
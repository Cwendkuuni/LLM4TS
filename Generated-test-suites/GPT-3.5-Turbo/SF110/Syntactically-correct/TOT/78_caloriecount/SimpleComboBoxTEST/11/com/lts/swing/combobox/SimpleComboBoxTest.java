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
    public void testSetModel_ValidModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        
        simpleComboBox.setModel(model);
        
        Assert.assertEquals(model, simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue_WithoutModel() {
        simpleComboBox.mySimpleModel = null;
        
        Object selectedValue = simpleComboBox.getSelectedValue();
        
        Assert.assertNull(selectedValue);
    }
    
    @Test
    public void testGetSelectedValue_WithModel() {
        Object expectedValue = "test";
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        Object selectedValue = simpleComboBox.getSelectedValue();
        
        Assert.assertEquals(expectedValue, selectedValue);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValue_InvalidValueType() {
        Object invalidValue = new Object();
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(invalidValue);
        simpleComboBox.mySimpleModel = model;
        
        simpleComboBox.getSelectedValue();
    }
    
    @Test
    public void testSetSelectedValue_ValidValue() {
        Object value = "test";
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel = model;
        
        simpleComboBox.setSelectedValue(value);
        
        Assert.assertEquals(value, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_IntegerValue() {
        int expectedValue = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        Assert.assertEquals(expectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_LongValue() {
        long expectedValue = 100L;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        Assert.assertEquals((int)expectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_ShortValue() {
        short expectedValue = 10;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        Assert.assertEquals(expectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_ByteValue() {
        byte expectedValue = 2;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        int selectedInt = simpleComboBox.getSelectedInt();
        
        Assert.assertEquals(expectedValue, selectedInt);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValueType() {
        Object invalidValue = new Object();
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(invalidValue);
        simpleComboBox.mySimpleModel = model;
        
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_IntegerValue() {
        int expectedValue = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        Assert.assertEquals(expectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_LongValue() {
        long expectedValue = 100L;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        Assert.assertEquals(expectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_ShortValue() {
        short expectedValue = 10;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        Assert.assertEquals(expectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_ByteValue() {
        byte expectedValue = 2;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(expectedValue);
        simpleComboBox.mySimpleModel = model;
        
        long selectedLong = simpleComboBox.getSelectedLong();
        
        Assert.assertEquals(expectedValue, selectedLong);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValueType() {
        Object invalidValue = new Object();
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(invalidValue);
        simpleComboBox.mySimpleModel = model;
        
        simpleComboBox.getSelectedLong();
    }
}
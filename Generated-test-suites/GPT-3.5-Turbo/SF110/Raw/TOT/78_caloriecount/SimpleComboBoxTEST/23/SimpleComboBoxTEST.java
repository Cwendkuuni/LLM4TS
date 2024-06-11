import org.junit.Test;
import org.junit.Assert;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);
        
        Assert.assertEquals(newModel, comboBox.getModel());
        Assert.assertNull(comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue_NullModel() {
        SimpleComboBoxModel model = null;
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Assert.assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_ValidModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals("Value 1", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("Value 2");
        
        Assert.assertEquals("Value 2", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(100, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100000000L);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(100000000, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 5);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(5, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(100L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100000000L);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(100000000L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 10);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 5);
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        Assert.assertEquals(5L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Invalid");
        
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        
        comboBox.getSelectedLong();
    }
}
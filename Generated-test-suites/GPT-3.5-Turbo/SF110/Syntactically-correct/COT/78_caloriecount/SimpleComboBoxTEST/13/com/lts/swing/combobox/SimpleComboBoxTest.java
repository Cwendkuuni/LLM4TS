import org.junit.Assert;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    @Test
    public void testSetModel() {
        // Test if setModel correctly assigns the mySimpleModel variable
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setModel(model);
        
        Assert.assertEquals(model, comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        // Test if getSelectedValue returns the correct selected value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem("Item 1");
        
        Assert.assertEquals("Item 1", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        // Test if setSelectedValue correctly sets the selected value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Item 1");
        
        Assert.assertEquals("Item 1", comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        // Test if getSelectedInt returns the correct value for Integer
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem(1);
        
        Assert.assertEquals(1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        // Test if getSelectedLong returns the correct value for Long
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1000L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem(1000L);
        
        Assert.assertEquals(1000L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_Exception() {
        // Test if getSelectedInt throws IllegalArgumentException for non-Integer values
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem("Item 1");
        
        comboBox.getSelectedInt();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_Exception() {
        // Test if getSelectedLong throws IllegalArgumentException for non-Long values
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedItem("Item 1");
        
        comboBox.getSelectedLong();
    }
}
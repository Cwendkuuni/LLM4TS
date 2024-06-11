
package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setModel(model);
        
        assertEquals(model, comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue_WithNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_WithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Object value = new Object();
        model.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Object value = new Object();
        comboBox.setSelectedValue(value);
        
        assertEquals(value, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_WithNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        
        assertEquals(-1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Integer value = 10;
        model.setSelectedValue(value);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Long value = 10L;
        model.setSelectedValue(value);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Short value = 10;
        model.setSelectedValue(value);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_WithByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Byte value = 10;
        model.setSelectedValue(value);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_WithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.setSelectedValue("invalid");
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_WithNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        
        assertEquals(-1L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Integer value = 10;
        model.setSelectedValue(value);
        
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Long value = 10L;
        model.setSelectedValue(value);
        
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Short value = 10;
        model.setSelectedValue(value);
        
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_WithByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        Byte value = 10;
        model.setSelectedValue(value);
        
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_WithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.setSelectedValue("invalid");
        
        comboBox.getSelectedLong();
    }
}

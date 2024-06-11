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
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.mySimpleModel = model;
        
        Object selectedValue = comboBox.getSelectedValue();
        
        assertNull(selectedValue);
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.mySimpleModel = model;
        
        Object value = new Object();
        comboBox.setSelectedValue(value);
        
        assertEquals(value, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.mySimpleModel = model;
        
        int selectedInt = comboBox.getSelectedInt();
        
        assertEquals(-1, selectedInt);
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.mySimpleModel = model;
        
        long selectedLong = comboBox.getSelectedLong();
        
        assertEquals(-1L, selectedLong);
    }
}
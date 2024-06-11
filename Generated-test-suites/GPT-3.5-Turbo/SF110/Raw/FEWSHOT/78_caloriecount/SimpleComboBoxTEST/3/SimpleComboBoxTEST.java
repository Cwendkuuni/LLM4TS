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
        assertNull(comboBox.getSelectedValue());
        
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
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        int intValue = 3;
        model.setSelectedValue(intValue);
        assertEquals(intValue, comboBox.getSelectedInt());
        
        long longValue = 5L;
        model.setSelectedValue(longValue);
        assertEquals((int) longValue, comboBox.getSelectedInt());
        
        short shortValue = 7;
        model.setSelectedValue(shortValue);
        assertEquals(shortValue, comboBox.getSelectedInt());
        
        byte byteValue = 9;
        model.setSelectedValue(byteValue);
        assertEquals(byteValue, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        int intValue = 3;
        model.setSelectedValue(intValue);
        assertEquals((long) intValue, comboBox.getSelectedLong());
        
        long longValue = 5L;
        model.setSelectedValue(longValue);
        assertEquals(longValue, comboBox.getSelectedLong());
        
        short shortValue = 7;
        model.setSelectedValue(shortValue);
        assertEquals((long) shortValue, comboBox.getSelectedLong());
        
        byte byteValue = 9;
        model.setSelectedValue(byteValue);
        assertEquals((long) byteValue, comboBox.getSelectedLong());
    }
}
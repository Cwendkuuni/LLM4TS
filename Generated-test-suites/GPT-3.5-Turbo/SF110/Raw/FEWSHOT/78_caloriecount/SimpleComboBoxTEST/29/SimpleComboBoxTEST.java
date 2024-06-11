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
        assertEquals(model, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertNull(comboBox.getSelectedValue());
        
        model.setSelectedValue("test");
        assertEquals("test", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("test");
        
        assertEquals("test", model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.setSelectedValue(10);
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue(10L);
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue((short) 10);
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue((byte) 10);
        assertEquals(10, comboBox.getSelectedInt());
        
        assertNull(comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        model.setSelectedValue(10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        model.setSelectedValue(10L);
        assertEquals(10L, comboBox.getSelectedLong());
        
        model.setSelectedValue((short) 10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        model.setSelectedValue((byte) 10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        assertNull(comboBox.getSelectedLong());
    }
}
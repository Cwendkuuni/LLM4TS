package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleComboBoxTest {
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        
        assertNull(comboBox.mySimpleModel);
        
        comboBox.setModel(newModel);
        
        assertEquals(newModel, comboBox.mySimpleModel);
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
        
        model.setSelectedValue("old value");
        comboBox.setSelectedValue("new value");
        
        assertEquals("new value", model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(-1, comboBox.getSelectedInt());
        
        model.setSelectedValue(10);
        
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue(10L);
        
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue((short) 10);
        
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue((byte) 10);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(-1L, comboBox.getSelectedLong());
        
        model.setSelectedValue(10);
        
        assertEquals(10L, comboBox.getSelectedLong());
        
        model.setSelectedValue(10L);
        
        assertEquals(10L, comboBox.getSelectedLong());
        
        model.setSelectedValue((short) 10);
        
        assertEquals(10L, comboBox.getSelectedLong());
        
        model.setSelectedValue((byte) 10);
        
        assertEquals(10L, comboBox.getSelectedLong());
    }
}
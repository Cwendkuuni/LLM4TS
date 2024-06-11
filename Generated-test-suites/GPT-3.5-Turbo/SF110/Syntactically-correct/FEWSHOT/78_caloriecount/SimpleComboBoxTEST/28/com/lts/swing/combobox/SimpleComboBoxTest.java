package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleComboBoxTest {
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(model, comboBox.mySimpleModel);
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
        
        comboBox.setSelectedValue(10);
        assertEquals(10, comboBox.getSelectedInt());
        
        comboBox.setSelectedValue(10L);
        assertEquals(10, comboBox.getSelectedInt());
        
        comboBox.setSelectedValue((short) 10);
        assertEquals(10, comboBox.getSelectedInt());
        
        comboBox.setSelectedValue((byte) 10);
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("test");
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue(10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        comboBox.setSelectedValue(10L);
        assertEquals(10L, comboBox.getSelectedLong());
        
        comboBox.setSelectedValue((short) 10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        comboBox.setSelectedValue((byte) 10);
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("test");
        comboBox.getSelectedLong();
    }
    
}
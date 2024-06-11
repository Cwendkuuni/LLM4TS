package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBoxModel model;
    private SimpleComboBox comboBox;
    
    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetSelectedValue() {
        model.setSelectedValue("value1");
        comboBox.setSelectedValue("value1");
        assertEquals("value1", comboBox.getSelectedValue());
        
        model.setSelectedValue("value2");
        comboBox.setSelectedValue("value2");
        assertEquals("value2", comboBox.getSelectedValue());
        
        model.setSelectedValue(null);
        comboBox.setSelectedValue(null);
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        model.setSelectedValue(10);
        comboBox.setSelectedValue(10);
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue(20);
        comboBox.setSelectedValue(20);
        assertEquals(20, comboBox.getSelectedInt());
        
        model.setSelectedValue(null);
        comboBox.setSelectedValue(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        model.setSelectedValue(100L);
        comboBox.setSelectedValue(100L);
        assertEquals(100L, comboBox.getSelectedLong());
        
        model.setSelectedValue(200L);
        comboBox.setSelectedValue(200L);
        assertEquals(200L, comboBox.getSelectedLong());
        
        model.setSelectedValue(null);
        comboBox.setSelectedValue(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }
}
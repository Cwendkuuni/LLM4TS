package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleComboBoxTest {
    @Test
    public void testSetModel() {
        // Test case 1: model is an instance of SimpleComboBoxModel
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);
        assertEquals(model, comboBox.mySimpleModel);
        
        // Test case 2: model is not an instance of SimpleComboBoxModel
        ComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        comboBox.setModel(comboBoxModel);
        assertNull(comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        // Test case 1: mySimpleModel is null
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
        
        // Test case 2: mySimpleModel is not null
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        comboBox = new SimpleComboBox(model);
        assertEquals("value", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        // Test case 1: setting selected value when mySimpleModel is null
        comboBox.setSelectedValue("value");
        assertNull(model.getSelectedValue());
        
        // Test case 2: setting selected value when mySimpleModel is not null
        comboBox.setModel(model);
        comboBox.setSelectedValue("value");
        assertEquals("value", model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        
        // Test case 1: o is null
        assertNull(comboBox.getSelectedInt());
        
        // Test case 2: o is an Integer
        comboBox.setSelectedValue(10);
        assertEquals(10, comboBox.getSelectedInt());
        
        // Test case 3: o is a Long
        comboBox.setSelectedValue(10L);
        assertEquals(10, comboBox.getSelectedInt());
        
        // Test case 4: o is a Short
        comboBox.setSelectedValue((short) 10);
        assertEquals(10, comboBox.getSelectedInt());
        
        // Test case 5: o is a Byte
        comboBox.setSelectedValue((byte) 10);
        assertEquals(10, comboBox.getSelectedInt());
        
        // Test case 6: o is not a valid type
        comboBox.setSelectedValue("value");
        try {
            comboBox.getSelectedInt();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        
        // Test case 1: o is null
        assertEquals(-1L, comboBox.getSelectedLong());
        
        // Test case 2: o is an Integer
        comboBox.setSelectedValue(10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        // Test case 3: o is a Long
        comboBox.setSelectedValue(10L);
        assertEquals(10L, comboBox.getSelectedLong());
        
        // Test case 4: o is a Short
        comboBox.setSelectedValue((short) 10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        // Test case 5: o is a Byte
        comboBox.setSelectedValue((byte) 10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        // Test case 6: o is not a valid type
        comboBox.setSelectedValue("value");
        try {
            comboBox.getSelectedLong();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
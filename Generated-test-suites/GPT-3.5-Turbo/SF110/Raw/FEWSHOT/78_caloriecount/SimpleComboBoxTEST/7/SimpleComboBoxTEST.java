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
        
        ComboBoxModel differentModel = new DefaultComboBoxModel();
        comboBox.setModel(differentModel);
        assertNull(comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Foo");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals("Foo", comboBox.getSelectedValue());
        
        comboBox.mySimpleModel = null;
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Foo");
        assertEquals("Foo", comboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(123);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123, comboBox.getSelectedInt());
        
        model.setSelectedValue((short) 10);
        assertEquals(10, comboBox.getSelectedInt());
        
        model.setSelectedValue((byte) 5);
        assertEquals(5, comboBox.getSelectedInt());
        
        model.setSelectedValue(1L);
        assertEquals(1, comboBox.getSelectedInt());
        
        model.setSelectedValue("Not a number");
        try {
            comboBox.getSelectedInt();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
        
        comboBox.mySimpleModel = null;
        assertEquals(-1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(123L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123L, comboBox.getSelectedLong());
        
        model.setSelectedValue((short) 10);
        assertEquals(10L, comboBox.getSelectedLong());
        
        model.setSelectedValue((byte) 5);
        assertEquals(5L, comboBox.getSelectedLong());
        
        model.setSelectedValue(1);
        assertEquals(1L, comboBox.getSelectedLong());
        
        model.setSelectedValue("Not a number");
        try {
            comboBox.getSelectedLong();
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
        
        comboBox.mySimpleModel = null;
        assertEquals(-1L, comboBox.getSelectedLong());
    }
}
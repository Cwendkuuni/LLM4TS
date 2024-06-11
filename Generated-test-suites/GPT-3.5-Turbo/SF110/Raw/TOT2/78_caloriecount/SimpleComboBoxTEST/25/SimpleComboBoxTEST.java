package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setup() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
        
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        Object value = 10;
        simpleComboBox.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());
        
        value = Long.valueOf(20);
        simpleComboBox.setSelectedValue(value);
        assertEquals(20, simpleComboBox.getSelectedInt());
        
        value = Short.valueOf((short)30);
        simpleComboBox.setSelectedValue(value);
        assertEquals(30, simpleComboBox.getSelectedInt());
        
        value = Byte.valueOf((byte)40);
        simpleComboBox.setSelectedValue(value);
        assertEquals(40, simpleComboBox.getSelectedInt());
        
        try {
            simpleComboBox.setSelectedValue("Invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testGetSelectedLong() {
        Object value = 100L;
        simpleComboBox.setSelectedValue(value);
        assertEquals(100L, simpleComboBox.getSelectedLong());
        
        value = Integer.valueOf(200);
        simpleComboBox.setSelectedValue(value);
        assertEquals(200, simpleComboBox.getSelectedLong());
        
        value = Short.valueOf((short)300);
        simpleComboBox.setSelectedValue(value);
        assertEquals(300, simpleComboBox.getSelectedLong());
        
        value = Byte.valueOf((byte)400);
        simpleComboBox.setSelectedValue(value);
        assertEquals(400, simpleComboBox.getSelectedLong());
        
        try {
            simpleComboBox.setSelectedValue("Invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}
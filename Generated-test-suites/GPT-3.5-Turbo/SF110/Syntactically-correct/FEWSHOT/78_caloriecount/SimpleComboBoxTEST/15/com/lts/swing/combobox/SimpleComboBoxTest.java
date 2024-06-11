package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    
    @Before
    public void setUp() {
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
        simpleComboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
        
        int intValue = 10;
        simpleComboBox.mySimpleModel.setSelectedValue(intValue);
        assertEquals(intValue, simpleComboBox.getSelectedInt());
        
        long longValue = 20L;
        simpleComboBox.mySimpleModel.setSelectedValue(longValue);
        assertEquals((int) longValue, simpleComboBox.getSelectedInt());
        
        short shortValue = 30;
        simpleComboBox.mySimpleModel.setSelectedValue(shortValue);
        assertEquals(shortValue, simpleComboBox.getSelectedInt());
        
        byte byteValue = 40;
        simpleComboBox.mySimpleModel.setSelectedValue(byteValue);
        assertEquals(byteValue, simpleComboBox.getSelectedInt());
        
        Object value = new Object();
        simpleComboBox.mySimpleModel.setSelectedValue(value);
        try {
            simpleComboBox.getSelectedInt();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }
    
    @Test
    public void testGetSelectedLong() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
        
        int intValue = 10;
        simpleComboBox.mySimpleModel.setSelectedValue(intValue);
        assertEquals((long) intValue, simpleComboBox.getSelectedLong());
        
        long longValue = 20L;
        simpleComboBox.mySimpleModel.setSelectedValue(longValue);
        assertEquals(longValue, simpleComboBox.getSelectedLong());
        
        short shortValue = 30;
        simpleComboBox.mySimpleModel.setSelectedValue(shortValue);
        assertEquals((long) shortValue, simpleComboBox.getSelectedLong());
        
        byte byteValue = 40;
        simpleComboBox.mySimpleModel.setSelectedValue(byteValue);
        assertEquals((long) byteValue, simpleComboBox.getSelectedLong());
        
        Object value = new Object();
        simpleComboBox.mySimpleModel.setSelectedValue(value);
        try {
            simpleComboBox.getSelectedLong();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }
}
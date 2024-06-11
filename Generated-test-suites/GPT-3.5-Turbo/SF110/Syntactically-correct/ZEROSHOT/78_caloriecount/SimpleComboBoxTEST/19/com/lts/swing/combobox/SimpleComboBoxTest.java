package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    private SimpleComboBox comboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        
        assertEquals(model, comboBox.getModel());
        assertEquals(model, comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValueWithNullModel() {
        comboBox.mySimpleModel = null;
        
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue() {
        Object value = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.setSelectedValue(value);
        
        assertEquals(value, comboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedIntWithNullValue() {
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(null);
        
        assertEquals(-1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithIntegerValue() {
        int value = 123;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithLongValue() {
        long value = 123L;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals((int) value, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithShortValue() {
        short value = 123;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithByteValue() {
        byte value = 123;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue("invalid");
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLongWithNullValue() {
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(null);
        
        assertEquals(-1L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithIntegerValue() {
        int value = 123;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithLongValue() {
        long value = 123L;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithShortValue() {
        short value = 123;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithByteValue() {
        byte value = 123;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(value);
        
        assertEquals(value, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue("invalid");
        
        comboBox.getSelectedLong();
    }
}
package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    private SimpleComboBox comboBox;
    
    @Before
    public void setUp() {
        comboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        
        assertEquals(model, comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValueWithNullModel() {
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValueWithNonNullModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        comboBox.setSelectedValue(value);
        
        assertEquals(value, comboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedIntWithNullModel() {
        assertEquals(-1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithNonNullModel_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        comboBox.setModel(model);
        
        assertEquals(10, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithNonNullModel_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        comboBox.setModel(model);
        
        assertEquals(100, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithNonNullModel_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 5);
        comboBox.setModel(model);
        
        assertEquals(5, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNonNullModel_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 2);
        comboBox.setModel(model);
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLongWithNullModel() {
        assertEquals(-1L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithNonNullModel_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        comboBox.setModel(model);
        
        assertEquals(10L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithNonNullModel_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        comboBox.setModel(model);
        
        assertEquals(100L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithNonNullModel_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 5);
        comboBox.setModel(model);
        
        assertEquals(5L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNonNullModel_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 2);
        comboBox.setModel(model);
        
        comboBox.getSelectedLong();
    }
}

package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleComboBoxTest {
    
    private SimpleComboBox comboBox;
    
    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }
    
    @Test
    public void testSetModel_SimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());
    }
    
    @Test
    public void testSetModel_ComboBoxModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue_WithoutModel() {
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_WithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        Object value = new Object();
        model.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        Object value = new Object();
        comboBox.setSelectedValue(value);
        assertEquals(value, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_WithoutModel() {
        assertEquals(-1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        int value = 10;
        model.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        long value = 10L;
        model.setSelectedValue(value);
        assertEquals((int)value, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        short value = 10;
        model.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        byte value = 10;
        model.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_IllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        Object value = new Object();
        model.setSelectedValue(value);
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_WithoutModel() {
        assertEquals(-1L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Integer() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        int value = 10;
        model.setSelectedValue(value);
        assertEquals((long)value, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Long() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        long value = 10L;
        model.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Short() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        short value = 10;
        model.setSelectedValue(value);
        assertEquals((long)value, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Byte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        byte value = 10;
        model.setSelectedValue(value);
        assertEquals((long)value, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_IllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        Object value = new Object();
        model.setSelectedValue(value);
        comboBox.getSelectedLong();
    }
}


package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleModel;
    
    @Before
    public void setUp() {
        simpleModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleModel);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue_NullModel() {
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_ValidModel() {
        String value = "Test Value";
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        String value = "Test Value";
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_NullModel() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Integer() {
        int value = 42;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Long() {
        long value = 42L;
        simpleModel.setSelectedValue(value);
        assertEquals((int) value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Short() {
        short value = 42;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_Byte() {
        byte value = 42;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_IllegalValue() {
        String value = "Test Value";
        simpleModel.setSelectedValue(value);
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_NullModel() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Integer() {
        int value = 42;
        simpleModel.setSelectedValue(value);
        assertEquals((long) value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Long() {
        long value = 42L;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Short() {
        short value = 42;
        simpleModel.setSelectedValue(value);
        assertEquals((long) value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_Byte() {
        byte value = 42;
        simpleModel.setSelectedValue(value);
        assertEquals((long) value, simpleComboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_IllegalValue() {
        String value = "Test Value";
        simpleModel.setSelectedValue(value);
        simpleComboBox.getSelectedLong();
    }
}

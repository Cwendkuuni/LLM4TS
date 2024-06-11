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
        assertEquals(model, simpleComboBox.getModel());
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
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidType() {
        simpleComboBox.setSelectedValue("abc");
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedIntWithInteger() {
        Integer value = 10;
        simpleComboBox.setSelectedValue(value);
        assertEquals((int) value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithLong() {
        Long value = 100L;
        simpleComboBox.setSelectedValue(value);
        assertEquals((int) (long) value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithShort() {
        Short value = 5;
        simpleComboBox.setSelectedValue(value);
        assertEquals((int) value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithByte() {
        Byte value = 2;
        simpleComboBox.setSelectedValue(value);
        assertEquals((int) value, simpleComboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        simpleComboBox.setSelectedValue("abc");
        simpleComboBox.getSelectedLong();
    }
    
    @Test
    public void testGetSelectedLongWithInteger() {
        Integer value = 10;
        simpleComboBox.setSelectedValue(value);
        assertEquals((long) value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithLong() {
        Long value = 100L;
        simpleComboBox.setSelectedValue(value);
        assertEquals((long) value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithShort() {
        Short value = 5;
        simpleComboBox.setSelectedValue(value);
        assertEquals((long) value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithByte() {
        Byte value = 2;
        simpleComboBox.setSelectedValue(value);
        assertEquals((long) value, simpleComboBox.getSelectedLong());
    }
}
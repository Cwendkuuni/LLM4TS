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
    public void testGetSelectedValueWithNullModel() {
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        simpleComboBox.setModel(model);
        assertEquals("value", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        simpleComboBox.setSelectedValue("value");
        assertEquals("value", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedIntWithNullModel() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(5);
        simpleComboBox.setModel(model);
        assertEquals(5, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10000000000L);
        simpleComboBox.setModel(model);
        assertEquals(10000000000, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short)9);
        simpleComboBox.setModel(model);
        assertEquals(9, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedIntWithByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte)3);
        simpleComboBox.setModel(model);
        assertEquals(3, simpleComboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLongWithNullModel() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(5);
        simpleComboBox.setModel(model);
        assertEquals(5L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10000000000L);
        simpleComboBox.setModel(model);
        assertEquals(10000000000L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short)9);
        simpleComboBox.setModel(model);
        assertEquals(9L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLongWithByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte)3);
        simpleComboBox.setModel(model);
        assertEquals(3L, simpleComboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedLong();
    }
}
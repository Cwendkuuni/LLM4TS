package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    @Test
    public void testSetModel() {
        ComboBoxModel model = new DefaultComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_withNullModel() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_withNonNullModel() {
        Object value = new Object();
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBoxModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_withNullValue() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_withIntegerValue() {
        Object value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_withLongValue() {
        Object value = (long) 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_withShortValue() {
        Object value = (short) 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_withByteValue() {
        Object value = (byte) 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_withInvalidValue() {
        Object value = new Object();
        simpleComboBoxModel.setSelectedValue(value);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_withNullValue() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_withIntegerValue() {
        Object value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_withLongValue() {
        Object value = (long) 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_withShortValue() {
        Object value = (short) 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_withByteValue() {
        Object value = (byte) 10;
        simpleComboBoxModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_withInvalidValue() {
        Object value = new Object();
        simpleComboBoxModel.setSelectedValue(value);
        simpleComboBox.getSelectedLong();
    }
}
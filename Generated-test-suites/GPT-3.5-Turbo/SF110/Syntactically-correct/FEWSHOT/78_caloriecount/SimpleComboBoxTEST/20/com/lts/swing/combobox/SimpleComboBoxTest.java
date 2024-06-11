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

        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);

        assertNull(comboBox.getSelectedValue());

        Object value = new Object();
        model.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);

        Object value = new Object();
        comboBox.setSelectedValue(value);
        assertEquals(value, model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);

        assertEquals(-1, comboBox.getSelectedInt());

        int intValue = 100;
        model.setSelectedValue(intValue);
        assertEquals(intValue, comboBox.getSelectedInt());

        byte byteValue = 10;
        model.setSelectedValue(byteValue);
        assertEquals(byteValue, comboBox.getSelectedInt());

        short shortValue = 20;
        model.setSelectedValue(shortValue);
        assertEquals(shortValue, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);

        assertEquals(-1L, comboBox.getSelectedLong());

        long longValue = 100L;
        model.setSelectedValue(longValue);
        assertEquals(longValue, comboBox.getSelectedLong());

        byte byteValue = 10;
        model.setSelectedValue(byteValue);
        assertEquals(byteValue, comboBox.getSelectedLong());

        short shortValue = 20;
        model.setSelectedValue(shortValue);
        assertEquals(shortValue, comboBox.getSelectedLong());
    }
}
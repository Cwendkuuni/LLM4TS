package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    private SimpleComboBox comboBox;
    private SimpleComboBoxModel model;

    @Before
    public void setup() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        ComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);

        assertEquals(newModel, comboBox.getModel());
        assertNull(comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(comboBox.getSelectedValue());

        Object value = new Object();
        model.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        comboBox.setSelectedValue(value);

        assertEquals(value, comboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        int intValue = 5;
        model.setSelectedValue(intValue);
        assertEquals(intValue, comboBox.getSelectedInt());

        long longValue = 10L;
        model.setSelectedValue(longValue);
        assertEquals((int) longValue, comboBox.getSelectedInt());

        short shortValue = 3;
        model.setSelectedValue(shortValue);
        assertEquals(shortValue, comboBox.getSelectedInt());

        byte byteValue = 1;
        model.setSelectedValue(byteValue);
        assertEquals(byteValue, comboBox.getSelectedInt());

        assertThrows(IllegalArgumentException.class, () -> {
            comboBox.setSelectedValue("invalid");
            comboBox.getSelectedInt();
        });
    }

    @Test
    public void testGetSelectedLong() {
        int intValue = 5;
        model.setSelectedValue(intValue);
        assertEquals(intValue, comboBox.getSelectedLong());

        long longValue = 10L;
        model.setSelectedValue(longValue);
        assertEquals(longValue, comboBox.getSelectedLong());

        short shortValue = 3;
        model.setSelectedValue(shortValue);
        assertEquals((long) shortValue, comboBox.getSelectedLong());

        byte byteValue = 1;
        model.setSelectedValue(byteValue);
        assertEquals((long) byteValue, comboBox.getSelectedLong());

        assertThrows(IllegalArgumentException.class, () -> {
            comboBox.setSelectedValue("invalid");
            comboBox.getSelectedLong();
        });
    }
}
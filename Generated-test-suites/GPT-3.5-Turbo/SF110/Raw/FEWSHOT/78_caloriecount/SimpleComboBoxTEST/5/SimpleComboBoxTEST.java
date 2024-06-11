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
    public void testGetSelectedValue() {
        assertNull(comboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("foo");
        comboBox.setModel(model);
        assertEquals("foo", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        Object value = "foo";
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        assertEquals(-1, comboBox.getSelectedInt());

        model.setSelectedValue(42);
        assertEquals(42, comboBox.getSelectedInt());

        model.setSelectedValue((long) 42);
        assertEquals(42, comboBox.getSelectedInt());

        model.setSelectedValue((short) 42);
        assertEquals(42, comboBox.getSelectedInt());

        model.setSelectedValue((byte) 42);
        assertEquals(42, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        assertEquals(-1L, comboBox.getSelectedLong());

        model.setSelectedValue(42);
        assertEquals(42L, comboBox.getSelectedLong());

        model.setSelectedValue((long) 42);
        assertEquals(42L, comboBox.getSelectedLong());

        model.setSelectedValue((short) 42);
        assertEquals(42L, comboBox.getSelectedLong());

        model.setSelectedValue((byte) 42);
        assertEquals(42L, comboBox.getSelectedLong());
    }
}
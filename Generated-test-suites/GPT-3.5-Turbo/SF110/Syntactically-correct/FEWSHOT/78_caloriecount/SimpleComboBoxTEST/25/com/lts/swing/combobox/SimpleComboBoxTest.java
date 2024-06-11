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
        model.setSelectedValue("value");
        comboBox.setModel(model);
        assertEquals("value", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        comboBox.setSelectedValue("value");
        assertEquals("value", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        assertEquals(-1, comboBox.getSelectedInt());

        model.setSelectedValue(5);
        assertEquals(5, comboBox.getSelectedInt());

        model.setSelectedValue(7L);
        assertEquals(7, comboBox.getSelectedInt());

        model.setSelectedValue((short) 10);
        assertEquals(10, comboBox.getSelectedInt());

        model.setSelectedValue((byte) 15);
        assertEquals(15, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        assertEquals(-1L, comboBox.getSelectedLong());

        model.setSelectedValue(5);
        assertEquals(5L, comboBox.getSelectedLong());

        model.setSelectedValue(7L);
        assertEquals(7L, comboBox.getSelectedLong());

        model.setSelectedValue((short) 10);
        assertEquals(10L, comboBox.getSelectedLong());

        model.setSelectedValue((byte) 15);
        assertEquals(15L, comboBox.getSelectedLong());
    }
}
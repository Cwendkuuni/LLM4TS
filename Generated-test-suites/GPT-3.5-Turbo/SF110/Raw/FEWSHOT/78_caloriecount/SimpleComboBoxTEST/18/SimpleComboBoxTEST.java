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
        assertEquals("value", comboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();

        model.setSelectedValue(10);
        comboBox.setModel(model);
        assertEquals(10, comboBox.getSelectedInt());

        model.setSelectedValue(100L);
        comboBox.setModel(model);
        assertEquals(100, comboBox.getSelectedInt());

        model.setSelectedValue((short) 5);
        comboBox.setModel(model);
        assertEquals(5, comboBox.getSelectedInt());

        model.setSelectedValue((byte) 2);
        comboBox.setModel(model);
        assertEquals(2, comboBox.getSelectedInt());
        
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();

        model.setSelectedValue(10);
        comboBox.setModel(model);
        assertEquals(10L, comboBox.getSelectedLong());

        model.setSelectedValue(100L);
        comboBox.setModel(model);
        assertEquals(100L, comboBox.getSelectedLong());

        model.setSelectedValue((short) 5);
        comboBox.setModel(model);
        assertEquals(5L, comboBox.getSelectedLong());

        model.setSelectedValue((byte) 2);
        comboBox.setModel(model);
        assertEquals(2L, comboBox.getSelectedLong());
        
        assertNull(comboBox.getSelectedValue());
    }
}
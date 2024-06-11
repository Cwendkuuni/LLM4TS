package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import static org.junit.Assert.*;

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
        assertTrue(simpleComboBox.getModel() instanceof SimpleComboBoxModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        simpleComboBox.setModel(model);

        assertNull(simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedIndex(1);

        assertEquals("Value 2", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value 1");
        model.addElement("Value 2");
        simpleComboBox.setModel(model);

        simpleComboBox.setSelectedValue("Value 2");

        assertEquals(1, simpleComboBox.getSelectedIndex());
    }

    @Test
    public void testGetSelectedInt() {
        assertNull(simpleComboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        simpleComboBox.setModel(model);

        simpleComboBox.setSelectedIndex(1);

        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        assertNull(simpleComboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        simpleComboBox.setModel(model);

        simpleComboBox.setSelectedIndex(1);

        assertEquals(2L, simpleComboBox.getSelectedLong());
    }
}
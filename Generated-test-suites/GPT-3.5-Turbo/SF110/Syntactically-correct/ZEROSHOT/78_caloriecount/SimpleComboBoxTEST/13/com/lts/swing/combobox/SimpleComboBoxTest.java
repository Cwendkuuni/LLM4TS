package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        DefaultComboBoxModel newModel = new DefaultComboBoxModel();
        comboBox.setModel(newModel);
        assertEquals(newModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(comboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);

        assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);

        comboBox.setSelectedValue("Test");

        assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        comboBox.setModel(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        comboBox.setModel(model);

        assertEquals(100L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);

        comboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);

        comboBox.getSelectedLong();
    }

}
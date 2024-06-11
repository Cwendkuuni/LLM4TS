package com.lts.swing.combobox;
import static org.junit.Assert.*;
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
        assertEquals(null, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Object selectedValue = "test";
        comboBox.setSelectedValue(selectedValue);
        assertEquals(selectedValue, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("test");
        comboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("test");
        comboBox.getSelectedLong();
    }
}
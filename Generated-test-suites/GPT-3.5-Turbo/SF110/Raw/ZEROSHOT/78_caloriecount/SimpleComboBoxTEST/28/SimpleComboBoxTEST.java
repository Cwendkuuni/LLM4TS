package com.lts.swing.combobox;

import org.junit.Assert;
import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setModel(model);

        Assert.assertEquals(model, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        Object selectedValue = comboBox.getSelectedValue();

        Assert.assertNull(selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue("Test");

        Assert.assertEquals("Test", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        int selectedInt = comboBox.getSelectedInt();

        Assert.assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        long selectedLong = comboBox.getSelectedLong();

        Assert.assertEquals(-1L, selectedLong);
    }
}
package com.lts.swing.combobox;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        Assert.assertEquals(model, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        Assert.assertNull(simpleComboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("TestValue");
        simpleComboBox.mySimpleModel = model;

        Assert.assertEquals("TestValue", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel = model;

        simpleComboBox.setSelectedValue("TestValue");

        Assert.assertEquals("TestValue", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        Assert.assertEquals(-1, simpleComboBox.getSelectedInt());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel = model;

        model.setSelectedValue(10);

        Assert.assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel = model;

        model.setSelectedValue("Test");

        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        Assert.assertEquals(-1L, simpleComboBox.getSelectedLong());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel = model;

        model.setSelectedValue(100L);

        Assert.assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel = model;

        model.setSelectedValue("Test");

        simpleComboBox.getSelectedLong();
    }
}
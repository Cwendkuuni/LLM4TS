package com.lts.swing.combobox;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    @Test
    public void testSetModel() {
        ComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        Assert.assertEquals(newModel, simpleComboBox.getModel());
        Assert.assertNull(simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        simpleComboBox.mySimpleModel = null;
        Assert.assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        Object selectedValue = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object selectedValue = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.setSelectedValue(selectedValue);
        Assert.assertEquals(selectedValue, simpleComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithNullValue() {
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(null);
        Assert.assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        int selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        long selectedValue = 10L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals((int) selectedValue, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        short selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        byte selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals(selectedValue, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        float selectedValue = 10.0f;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongWithNullValue() {
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(null);
        Assert.assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        int selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals((long) selectedValue, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        long selectedValue = 10L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals(selectedValue, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        short selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals((long) selectedValue, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        byte selectedValue = 10;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        Assert.assertEquals((long) selectedValue, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        float selectedValue = 10.0f;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(selectedValue);
        simpleComboBox.getSelectedLong();
    }
}
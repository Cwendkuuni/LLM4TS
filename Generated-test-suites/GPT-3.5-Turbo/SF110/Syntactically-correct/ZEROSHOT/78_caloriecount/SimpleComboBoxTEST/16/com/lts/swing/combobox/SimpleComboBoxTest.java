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
        ComboBoxModel model = new DefaultComboBoxModel();
        simpleComboBox.setModel(model);
        Assert.assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWithoutModel() {
        Assert.assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithModel() {
        Object value = new Object();
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        Assert.assertEquals(value, simpleComboBoxModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithoutValue() {
        Assert.assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        Integer value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        Long value = 10L;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        Short value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        Byte value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        simpleComboBoxModel.setSelectedValue("invalid");
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongWithoutValue() {
        Assert.assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        Integer value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        Long value = 10L;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        Short value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        Byte value = 10;
        simpleComboBoxModel.setSelectedValue(value);
        Assert.assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        simpleComboBoxModel.setSelectedValue("invalid");
        simpleComboBox.getSelectedLong();
    }
}
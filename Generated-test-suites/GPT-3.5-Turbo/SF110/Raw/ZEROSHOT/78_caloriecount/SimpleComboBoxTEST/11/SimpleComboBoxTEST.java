package com.lts.swing.combobox;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        ComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        Assert.assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NullModel() {
        Object selectedValue = simpleComboBox.getSelectedValue();
        Assert.assertNull(selectedValue);
    }

    @Test
    public void testGetSelectedValue_NotNullModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        simpleComboBox.setModel(model);
        Object selectedValue = simpleComboBox.getSelectedValue();
        Assert.assertEquals("test", selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        simpleComboBox.setSelectedValue("test");
        Object selectedValue = model.getSelectedValue();
        Assert.assertEquals("test", selectedValue);
    }

    @Test
    public void testGetSelectedInt_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(10, selectedInt);
    }

    @Test
    public void testGetSelectedInt_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(10, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(10, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        simpleComboBox.setModel(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(10, selectedInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_IntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(10L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_LongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(10L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(10L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        simpleComboBox.setModel(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(10L, selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedLong();
    }
}
package com.lts.swing.combobox;

import org.junit.Test;
import org.junit.Assert;

public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue_NullModel_ReturnsNull() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        Assert.assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidModel_ReturnsSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option 1");
        model.addElement("Option 2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        Assert.assertEquals("Option 2", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option 1");
        model.addElement("Option 2");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("Option 2");
        Assert.assertEquals(1, comboBox.getSelectedIndex());
    }

    @Test
    public void testGetSelectedInt_NullValue_ReturnsNegativeOne() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        Assert.assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_IntegerValue_ReturnsIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongValue_ReturnsIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10000000000L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(10000000000, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortValue_ReturnsShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 100);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(100, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteValue_ReturnsByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 10);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(10, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidType_ThrowsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_NullValue_ReturnsNegativeOne() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        Assert.assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_IntegerValue_ReturnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongValue_ReturnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10000000000L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(10000000000L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortValue_ReturnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 100);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(100L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteValue_ReturnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 10);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        Assert.assertEquals(10L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidType_ThrowsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        comboBox.getSelectedLong();
    }
}
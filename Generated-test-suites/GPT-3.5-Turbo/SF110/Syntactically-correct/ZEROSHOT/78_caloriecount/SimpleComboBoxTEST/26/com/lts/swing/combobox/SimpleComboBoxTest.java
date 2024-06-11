package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBoxModel model;
    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        ComboBoxModel testModel = new ComboBoxModel() {
            // implementation of ComboBoxModel methods
        };

        comboBox.setModel(testModel);

        assertEquals(testModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        Object selectedValue = new Object();
        model.setSelectedValue(selectedValue);

        assertEquals(selectedValue, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object selectedValue = new Object();
        comboBox.setSelectedValue(selectedValue);

        assertEquals(selectedValue, model.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithNullValue() {
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        Integer intValue = 10;
        model.setSelectedValue(intValue);

        assertEquals(intValue, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        Long longValue = 100L;
        model.setSelectedValue(longValue);

        assertEquals(longValue.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        Short shortValue = 5;
        model.setSelectedValue(shortValue);

        assertEquals(shortValue.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        Byte byteValue = 3;
        model.setSelectedValue(byteValue);

        assertEquals(byteValue.intValue(), comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValueType() {
        Object invalidValue = "Invalid";
        model.setSelectedValue(invalidValue);

        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongWithNullValue() {
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        Integer intValue = 10;
        model.setSelectedValue(intValue);

        assertEquals(intValue.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        Long longValue = 100L;
        model.setSelectedValue(longValue);

        assertEquals(longValue.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        Short shortValue = 5;
        model.setSelectedValue(shortValue);

        assertEquals(shortValue.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        Byte byteValue = 3;
        model.setSelectedValue(byteValue);

        assertEquals(byteValue.longValue(), comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValueType() {
        Object invalidValue = "Invalid";
        model.setSelectedValue(invalidValue);

        comboBox.getSelectedLong();
    }

}
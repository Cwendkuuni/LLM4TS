
package com.lts.swing.combobox;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.ComboBoxModel;

public class SimpleComboBoxTest {

    private SimpleComboBoxModel model;
    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        model.addElement(10);
        model.addElement(20);
        model.addElement(30);

        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel_ValidModel_Success() {
        ComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);
        assertNotNull(comboBox.mySimpleModel);
        assertSame(comboBox.mySimpleModel, newModel);
    }

    @Test
    public void testSetModel_NullModel_Success() {
        ComboBoxModel newModel = null;
        comboBox.setModel(newModel);
        assertNull(comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue_NullModel_ReturnNull() {
        comboBox.mySimpleModel = null;
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidModel_ReturnSelectedValue() {
        String expected = "Test";
        comboBox.mySimpleModel.setSelectedValue(expected);
        assertEquals(expected, comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValueType_ThrowIllegalArgumentException() {
        comboBox.mySimpleModel.setSelectedValue("Test");
        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedInt_NullValue_ReturnNegativeOne() {
        comboBox.mySimpleModel.setSelectedValue(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_IntegerValue_ReturnIntegerValue() {
        int expected = 123;
        comboBox.mySimpleModel.setSelectedValue(expected);
        assertEquals(expected, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongValue_ReturnLongValueAsInteger() {
        long value = 123456789L;
        comboBox.mySimpleModel.setSelectedValue(value);
        assertEquals((int)value, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortValue_ReturnShortValueAsInteger() {
        short value = Short.MAX_VALUE;
        comboBox.mySimpleModel.setSelectedValue(value);
        assertEquals((int)value, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteValue_ReturnByteValueAsInteger() {
        byte value = Byte.MIN_VALUE;
        comboBox.mySimpleModel.setSelectedValue(value);
        assertEquals((int)value, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValueType_ThrowIllegalArgumentException() {
        comboBox.mySimpleModel.setSelectedValue("Test");
        comboBox.getSelectedLong();
    }

    @Test
    public void testGetSelectedLong_NullValue_ReturnNegativeOne() {
        comboBox.mySimpleModel.setSelectedValue(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_IntegerValue_ReturnIntegerValueAsLong() {
        int value = 123;
        comboBox.mySimpleModel.setSelectedValue(value);
        assertEquals((long)value, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongValue_ReturnLongValue() {
        long expected = 123456789L;
        comboBox.mySimpleModel.setSelectedValue(expected);
        assertEquals(expected, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortValue_ReturnShortValueAsLong() {
        short value = Short.MAX_VALUE;
        comboBox.mySimpleModel.setSelectedValue(value);
        assertEquals((long)value, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteValue_ReturnByteValueAsLong() {
        byte value = Byte.MIN_VALUE;
        comboBox.mySimpleModel.setSelectedValue(value);
        assertEquals((long)value, comboBox.getSelectedLong());
    }
}


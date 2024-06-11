package com.lts.swing.combobox;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        ComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);

        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NullModel_ReturnsNull() {
        SimpleComboBox comboBox = new SimpleComboBox(null);

        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidModel_ReturnsSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("Test");

        assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_NullValue_ReturnsNegativeOne() {
        SimpleComboBox comboBox = new SimpleComboBox(null);

        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_IntegerValue_ReturnsIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_LongValue_ReturnsIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ShortValue_ReturnsShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_ByteValue_ReturnsByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_InvalidType_ThrowsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertThrows(IllegalArgumentException.class, comboBox::getSelectedInt);
    }

    @Test
    public void testGetSelectedLong_NullValue_ReturnsNegativeOne() {
        SimpleComboBox comboBox = new SimpleComboBox(null);

        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_IntegerValue_ReturnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_LongValue_ReturnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ShortValue_ReturnsShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_ByteValue_ReturnsByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_InvalidType_ThrowsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertThrows(IllegalArgumentException.class, comboBox::getSelectedLong);
    }
}
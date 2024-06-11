package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleModel;

    @Before
    public void setUp() {
        simpleModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleModel);
    }

    @Test
    public void testSetModel() {
        // Test case for setting SimpleComboBoxModel
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.mySimpleModel);

        // Test case for setting other ComboBoxModel
        ComboBoxModel otherModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(otherModel);
        assertNull(simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        // Test case for null SimpleComboBoxModel
        assertNull(simpleComboBox.getSelectedValue());

        // Test case for non-null SimpleComboBoxModel
        Object value = new Object();
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = new Object();

        // Test case for null SimpleComboBoxModel
        simpleComboBox.setSelectedValue(value);
        assertNull(simpleComboBox.mySimpleModel);
        
        // Test case for non-null SimpleComboBoxModel
        simpleModel.setSelectedValue(null);
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        Object value = 10;

        // Test case for null SimpleComboBoxModel
        assertEquals(-1, simpleComboBox.getSelectedInt());

        // Test case for Integer value
        simpleModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());

        // Test case for Long value
        value = 10L;
        simpleModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());

        // Test case for Short value
        value = (short) 10;
        simpleModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());

        // Test case for Byte value
        value = (byte) 10;
        simpleModel.setSelectedValue(value);
        assertEquals(10, simpleComboBox.getSelectedInt());

        // Test case for invalid value
        value = "invalid";
        simpleModel.setSelectedValue(value);
        try {
            simpleComboBox.getSelectedInt();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testGetSelectedLong() {
        Object value = 10L;

        // Test case for null SimpleComboBoxModel
        assertEquals(-1L, simpleComboBox.getSelectedLong());

        // Test case for Integer value
        value = 10;
        simpleModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());

        // Test case for Long value
        simpleModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());

        // Test case for Short value
        value = (short) 10;
        simpleModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());

        // Test case for Byte value
        value = (byte) 10;
        simpleModel.setSelectedValue(value);
        assertEquals(10L, simpleComboBox.getSelectedLong());

        // Test case for invalid value
        value = "invalid";
        simpleModel.setSelectedValue(value);
        try {
            simpleComboBox.getSelectedLong();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}
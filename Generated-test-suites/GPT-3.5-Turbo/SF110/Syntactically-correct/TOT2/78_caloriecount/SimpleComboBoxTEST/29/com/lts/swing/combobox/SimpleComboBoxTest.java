package com.lts.swing.combobox;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        //Test setting the model to the SimpleComboBox
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);

        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        //Test getting selected value when model is null
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        //Test setting the selected value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("Test");

        assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        //Test getting selected int value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(10, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        //Test getting selected long value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals(100L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntException() {
        //Test getting selected value of incorrect type
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("string");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.getSelectedInt();
    }

}
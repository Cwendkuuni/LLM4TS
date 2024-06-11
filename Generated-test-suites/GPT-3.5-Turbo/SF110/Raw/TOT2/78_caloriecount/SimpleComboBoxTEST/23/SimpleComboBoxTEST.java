package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleModel);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBox.setSelectedValue(5);
        assertEquals(5, simpleComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        simpleComboBox.setSelectedValue(15L);
        assertEquals(15L, simpleComboBox.getSelectedLong());
    }
}
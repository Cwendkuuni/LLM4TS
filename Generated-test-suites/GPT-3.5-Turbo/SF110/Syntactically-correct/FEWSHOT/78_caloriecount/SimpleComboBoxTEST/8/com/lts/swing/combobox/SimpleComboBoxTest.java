
package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
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

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("value");
        simpleComboBox.setModel(model);
        assertEquals("value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);

        simpleComboBox.setSelectedValue("value");
        assertEquals("value", simpleComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);

        model.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());

        model.setSelectedValue((short) 20);
        assertEquals(20, simpleComboBox.getSelectedInt());

        model.setSelectedValue((byte) 30);
        assertEquals(30, simpleComboBox.getSelectedInt());

        assertNull(simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);

        model.setSelectedValue(100L);
        assertEquals(100L, simpleComboBox.getSelectedLong());

        model.setSelectedValue((int) 200);
        assertEquals(200L, simpleComboBox.getSelectedLong());

        model.setSelectedValue((short) 300);
        assertEquals(300L, simpleComboBox.getSelectedLong());

        assertNull(simpleComboBox.getSelectedLong());
    }
}

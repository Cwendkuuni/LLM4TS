package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertTrue(simpleComboBox.getModel() instanceof SimpleComboBoxModel);
        assertSame(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        simpleComboBox.setModel(model);
        assertEquals("test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        simpleComboBox.setSelectedValue("test");
        assertEquals("test", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1, simpleComboBox.getSelectedInt());

        model.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());

        model.setSelectedValue((short) 5);
        assertEquals(5, simpleComboBox.getSelectedInt());

        model.setSelectedValue(1L);
        assertEquals(1, simpleComboBox.getSelectedInt());

        try {
            model.setSelectedValue(1.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1L, simpleComboBox.getSelectedLong());

        model.setSelectedValue(10);
        assertEquals(10L, simpleComboBox.getSelectedLong());

        model.setSelectedValue((short) 5);
        assertEquals(5L, simpleComboBox.getSelectedLong());

        model.setSelectedValue(1L);
        assertEquals(1L, simpleComboBox.getSelectedLong());

        try {
            model.setSelectedValue(1.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
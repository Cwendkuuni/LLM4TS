package com.lts.swing.combobox;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        assertEquals(model, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(comboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);
        assertEquals("Test", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        comboBox.setSelectedValue("Test");
        assertEquals("Test", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(12345);
        comboBox.setModel(model);
        assertEquals(12345, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(12345L);
        comboBox.setModel(model);
        assertEquals(12345, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 12345);
        comboBox.setModel(model);
        assertEquals(12345, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 123);
        comboBox.setModel(model);
        assertEquals(123, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);
        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(12345L);
        comboBox.setModel(model);
        assertEquals(12345L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(12345);
        comboBox.setModel(model);
        assertEquals(12345L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 12345);
        comboBox.setModel(model);
        assertEquals(12345L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 123);
        comboBox.setModel(model);
        assertEquals(123L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        comboBox.setModel(model);
        comboBox.getSelectedLong();
    }

}


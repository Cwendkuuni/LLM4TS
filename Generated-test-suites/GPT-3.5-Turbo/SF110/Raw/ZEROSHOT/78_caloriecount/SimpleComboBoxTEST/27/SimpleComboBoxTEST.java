package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel_ValidModel_ShouldSetSimpleModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testSetModel_NotSimpleModel_ShouldNotSetSimpleModel() {
        ComboBoxModel model = new ComboBoxModel() {
            // implement necessary methods
        };
        simpleComboBox.setModel(model);
        assertNull(simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue_SimpleModelIsNull_ShouldReturnNull() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_SimpleModelNotNull_ShouldReturnSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        simpleComboBox.setModel(model);
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue_ShouldSetSelectedValueInSimpleModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_SelectedValueIsNull_ShouldReturnMinusOne() {
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_SelectedValueIsInteger_ShouldReturnIntegerValue() {
        Integer value = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_SelectedValueIsLong_ShouldReturnIntegerValue() {
        Long value = 5L;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_SelectedValueIsShort_ShouldReturnShortValue() {
        Short value = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_SelectedValueIsByte_ShouldReturnByteValue() {
        Byte value = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_SelectedValueIsInvalid_ShouldThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_SelectedValueIsNull_ShouldReturnMinusOne() {
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_SelectedValueIsInteger_ShouldReturnLongValue() {
        Integer value = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_SelectedValueIsLong_ShouldReturnLongValue() {
        Long value = 5L;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_SelectedValueIsShort_ShouldReturnShortValue() {
        Short value = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_SelectedValueIsByte_ShouldReturnByteValue() {
        Byte value = 5;
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(value);
        simpleComboBox.setModel(model);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_SelectedValueIsInvalid_ShouldThrowIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedLong();
    }

}
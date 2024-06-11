package com.lts.swing.combobox;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleComboBoxTest {

    SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.addItem(1);
        simpleComboBox.addItem(2);
        simpleComboBox.addItem(3);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        assertEquals(null, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBox.setSelectedValue(2);
        assertEquals(2, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

}
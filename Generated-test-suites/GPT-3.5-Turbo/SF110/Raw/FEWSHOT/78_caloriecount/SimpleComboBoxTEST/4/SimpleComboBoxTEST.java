package com.lts.swing.combobox;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
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
        assertNotNull(simpleComboBox);

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());

        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Value");
        simpleComboBox.setModel(model);
        
        assertEquals("Value", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        
        simpleComboBox.setSelectedValue("Value");
        
        assertEquals("Value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);

        int intValue = 10;
        model.setSelectedValue(intValue);
        
        assertEquals(intValue, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);

        long longValue = 100L;
        model.setSelectedValue(longValue);
        
        assertEquals(longValue, simpleComboBox.getSelectedLong());
    }
}
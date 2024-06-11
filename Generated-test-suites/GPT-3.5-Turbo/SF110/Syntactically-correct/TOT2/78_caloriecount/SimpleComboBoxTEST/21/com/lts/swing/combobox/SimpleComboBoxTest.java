package com.lts.swing.combobox;
import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedValue(1);
        assertEquals(1, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1, simpleComboBox.getSelectedInt());
        simpleComboBox.setSelectedValue(5);
        assertEquals(5, simpleComboBox.getSelectedInt());
        
        // Test for IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            simpleComboBox.setSelectedValue(5.5);
            simpleComboBox.getSelectedInt();
        });
    }
    
    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1L, simpleComboBox.getSelectedLong());
        simpleComboBox.setSelectedValue(10L);
        assertEquals(10L, simpleComboBox.getSelectedLong());
        
        // Test for IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            simpleComboBox.setSelectedValue("Test");
            simpleComboBox.getSelectedLong();
        });
    }
}
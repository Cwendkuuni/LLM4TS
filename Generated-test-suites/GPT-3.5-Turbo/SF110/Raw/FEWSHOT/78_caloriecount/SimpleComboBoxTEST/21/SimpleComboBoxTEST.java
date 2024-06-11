package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setup() {
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        
        simpleComboBox.setModel(model);
        
        assertEquals(model, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue_withValidValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value, simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_withNullValue() {
        assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_withIntegerValue() {
        Integer value = 10;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_withLongValue() {
        Long value = 10L;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_withShortValue() {
        Short value = 10;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_withByteValue() {
        Byte value = 10;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value, simpleComboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_withInvalidValue() {
        simpleComboBox.setSelectedValue(new Object());
        
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_withIntegerValue() {
        Integer value = 10;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_withLongValue() {
        Long value = 10L;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_withShortValue() {
        Short value = 10;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_withByteValue() {
        Byte value = 10;
        simpleComboBox.setSelectedValue(value);
        
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_withInvalidValue() {
        simpleComboBox.setSelectedValue(new Object());
        
        simpleComboBox.getSelectedLong();
    }
}